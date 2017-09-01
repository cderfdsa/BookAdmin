package com.example.bookadmin.activity.logic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.BaseActivity;
import com.example.bookadmin.requrest.UserAddBiz;
import com.example.bookadmin.interf.UserRequestCallback;
import com.example.bookadmin.tools.utils.CipherUtil;
import com.example.bookadmin.tools.CountTimerView;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.ClearEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2017/5/9.
 */

public class RegSecondActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolBar;
    private TextView mTxtTip;
    private Button mBtnResend;
    private TextView btnRight;
    private ClearEditText mEtCode;

    private String phone;
    private String name;
    private String pwd;
    private String countryCode;

    private CountTimerView countTimerView;

    private SpotsDialog dialog;
    private SMSSecondEvenHanlder evenHanlder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTxtTip = (TextView) findViewById(R.id.txtTip);
        mBtnResend = (Button) findViewById(R.id.btn_reSend);
        btnRight = (TextView) findViewById(R.id.btnRight);
        mEtCode = (ClearEditText) findViewById(R.id.edittxt_code);

        mBtnResend.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        setToolBarReplaceActionBar();

        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        SMSSDK.initSDK(this, Contants.API.MOB_SMS_APPKEY, Contants.API.MOB_SMS_APPSECRECT);

        evenHanlder = new SMSSecondEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this, "正在校验验证码");
        SMSSDK.getVerificationCode("+" + countryCode, phone);
    }

    /**
     * 用toolBar替换ActionBar
     */
    private void setToolBarReplaceActionBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    private void doReg(){
//        final UserEntity userEntity = new UserEntity("", name, DESUtil.encode(Contants.DES_KEY, pwd), phone);
        UserAddBiz.requestData(this, name, CipherUtil.getAESInfo(pwd), phone, new UserRequestCallback() {
            @Override
            public void handleQueryResult(int code) {
//                BookApplication application = BookApplication.getInstance();
//                application.putUser(userEntity);
                ToastUtils.showToastInCenter(RegSecondActivity.this, 2, "用户注册成功！", Toast.LENGTH_SHORT);
                    setResult(Contants.RESULT_CODE_SECOND_REG);
                    finish();
            }

            @Override
            public void handleQueryResultError() {
                ToastUtils.showToastInCenter(RegSecondActivity.this, 1, "验证错误,请重新获取验证码", Toast.LENGTH_SHORT);
                mEtCode.setText("");
            }

            @Override
            public void handleQueryError() {
                IniterUtils.noIntent(RegSecondActivity.this, null, null);
            }
        });
    }

    private  void submitCode(){

        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.showToastInCenter(RegSecondActivity.this, 1, getString(R.string.smssdk_write_identify_code), Toast.LENGTH_SHORT);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode,phone, vCode);
        dialog.show();
    }

    private void again(){
        SMSSDK.getVerificationCode("+" + countryCode, phone);
        countTimerView = new CountTimerView(mBtnResend, R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog = new SpotsDialog(this, "正在重新获取验证码");
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRight:
                submitCode();
//                doReg();
                break;
            case R.id.btn_reSend:
                again();
                break;
        }
    }

    class SMSSecondEvenHanlder extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");
//                            ToastUtils.show(RegSecondActivity.this,"验证成功"+phone+",country:"+country);
                            doReg();
                            dialog = new SpotsDialog(RegSecondActivity.this, "正在提交注册信息");
//                            dialog.show();;
                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                            // 请求验证码后，跳转到验证码填写页面
//
//                            afterVerificationCodeRequested((Boolean) data);
                        }
                    } else {
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.showToastInCenter(RegSecondActivity.this, 1, des, Toast.LENGTH_SHORT);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }

    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
        }
    }

}
