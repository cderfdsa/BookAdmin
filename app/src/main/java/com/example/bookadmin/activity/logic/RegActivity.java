package com.example.bookadmin.activity.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.BaseActivity;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.TextUtiles;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.ClearEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.SMSSDK;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-09.
 */

public class RegActivity extends BaseActivity implements View.OnClickListener {

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    private Toolbar mToolBar;
    private TextView mTxtCountry;
    private TextView mTxtCountryCode;
    private ClearEditText mEtxtPhone;
    private ClearEditText mEtxtName;
    private ClearEditText mEtxtPwd;
    private TextView btnRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTxtCountry = (TextView) findViewById(R.id.txtCountry);
        mTxtCountryCode = (TextView) findViewById(R.id.txtCountryCode);
        mEtxtPhone = (ClearEditText) findViewById(R.id.edittxt_phone);
        mEtxtName = (ClearEditText) findViewById(R.id.edittxt_name);
        mEtxtPwd = (ClearEditText) findViewById(R.id.edittxt_pwd);
        btnRight = (TextView) findViewById(R.id.btnRight);

        btnRight.setOnClickListener(this);
        setToolBarReplaceActionBar();
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

    /**
     * 请求验证码后，跳转到验证码填写页面
     */
    private void afterVerificationCodeRequested() {
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();
        String name = mEtxtName.getText().toString().trim();

        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(this, RegSecondActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        intent.putExtra("name", name);
        intent.putExtra("countryCode", code);

        startActivityForResult(intent, Contants.REQUEST_REG_CODE);
    }

    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }
        if (country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }
        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }

    private void getCode() {

        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        String name = mEtxtName.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();

        checkPhoneNum(phone, code);
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "请输入用户名", Toast.LENGTH_SHORT);
            return;
        }
        if(name.length() < 2) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "用户名过短，建议3 - 10位", Toast.LENGTH_SHORT);
            return;
        }
        if(name.length() > 10) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "用户名过长，建议3 - 10位", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtiles.checkName(name)) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "不允许输入特殊符号！", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "请输入密码", Toast.LENGTH_SHORT);
            return;
        }

        checkServiceName(name, phone);

//        SMSSDK.getVerificationCode(code, phone);
    }

    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "请输入手机号码", Toast.LENGTH_SHORT);
            return;
        }
        if (code == "86") {
            if (phone.length() != 11) {
                ToastUtils.showToastInCenter(RegActivity.this, 1, "手机号码长度不对", Toast.LENGTH_SHORT);
                return;
            }
        }
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            ToastUtils.showToastInCenter(RegActivity.this, 1, "您输入的手机号码格式不正确", Toast.LENGTH_SHORT);
            return;
        }
    }

    private void checkServiceName(String name, String phone) {
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.USER_NAMEPHONE)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("username", name)
                .addParams("phone", phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        IniterUtils.noIntent(RegActivity.this, null, null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            final int code = obj.getInt("code");
                            String value = obj.getString("value");
                            final String data = obj.getString("data");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (code){
                                        case 200:
                                            afterVerificationCodeRequested();
                                            break;
                                        case 504:
                                            ToastUtils.showToastInCenter(RegActivity.this, 1, "手机号有误", Toast.LENGTH_SHORT);
                                            break;
                                        case 506:
                                            ToastUtils.showToastInCenter(RegActivity.this, 1, "用户名或手机号已存在", Toast.LENGTH_SHORT);
                                            break;
                                    }
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRight:
                getCode();
                break;
        }
    }

//    class SMSEvenHanlder extends EventHandler {
//        @Override
//        public void afterEvent(final int event, final int result, final Object data) {
//            runOnUiThread(new Runnable() {//17181015121
//                @Override
//                public void run() {
//                    if (result == SMSSDK.RESULT_COMPLETE) {
//                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
//                            //返回支持发送验证码的国家列表
//                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
//                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                            // 请求验证码后，跳转到验证码填写页面
//
//                            afterVerificationCodeRequested((Boolean) data);
//                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                            //提交验证码成功
//                        }
//                    } else {
//
//                        // 根据服务器返回的网络错误，给toast提示
//                        try {
//                            ((Throwable) data).printStackTrace();
//                            Throwable throwable = (Throwable) data;
//
//                            JSONObject object = new JSONObject(throwable.getMessage());
//                            String des = object.optString("detail");
//                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.showShortToast(RegActivity.this, des);
//                                return;
//                            }
//                        } catch (Exception e) {
//                            SMSLog.getInstance().w(e);
//                        }
//                    }
//
//                }
//            });
//
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contants.REQUEST_REG_CODE) {
            if (resultCode == Contants.RESULT_CODE_SECOND_REG) {
                    setResult(Contants.RESULT_CODE_REG);
                    finish();
            }
        }
    }


}
