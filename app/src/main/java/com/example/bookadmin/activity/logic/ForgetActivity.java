package com.example.bookadmin.activity.logic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.BaseActivity;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-19.
 */

public class ForgetActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    Toolbar mToolBar;
    @ViewInject(R.id.txtCountry)
    TextView mTxtCountry;
    @ViewInject(R.id.txtCountryCode)
    TextView mTxtCountryCode;
    @ViewInject(R.id.edittxt_phone)
    ClearEditText mEtxtPhone;
    @ViewInject(R.id.btnRight)
    TextView btnNext;

    private String u_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget);
        ViewUtils.inject(this);

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

    @OnClick(R.id.btnRight)
    private void vail(View view) {
        //检查手机号码
        final String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        checkPhoneNum(phone, code);

        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.SAVE)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("phone", phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            final int code = obj.getInt("code");
                            String value = obj.getString("value");
                            final String data = obj.getString("data");
                            if (code == 200) {
                                JSONObject jsonObject = new JSONObject(data);
                                u_token = jsonObject.getString("u_token");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //成功发验证码
                                        startVail();
                                    }
                                });
                            }else{
                                if(code == 521 || code == 509){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToastInCenter(ForgetActivity.this, 1, "该手机号不存在用户", Toast.LENGTH_SHORT);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void startVail() {
        String code = mTxtCountryCode.getText().toString().trim();
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");

        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(ForgetActivity.this, ForgetSecondActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("u_token", u_token);
        intent.putExtra("countryCode", code);

        startActivityForResult(intent, Contants.REQUEST_FORGET_ONE);
    }


    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToastInCenter(ForgetActivity.this, 1, "请输入手机号码", Toast.LENGTH_SHORT);
            return;
        }
        if (code == "86") {
            if (phone.length() != 11) {
                ToastUtils.showToastInCenter(ForgetActivity.this, 1, "手机号码长度不对", Toast.LENGTH_SHORT);
                return;
            }
        }
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            ToastUtils.showToastInCenter(ForgetActivity.this, 1, "您输入的手机号码格式不正确", Toast.LENGTH_SHORT);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Contants.REQUEST_FORGET_ONE:
                if (resultCode == Contants.RESULT_FORGET_SECOND) {
                    setResult(Contants.RESULT_FORGET, data);
                    finish();
                }
                break;
        }
    }

}
