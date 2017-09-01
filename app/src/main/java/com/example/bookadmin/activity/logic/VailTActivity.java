package com.example.bookadmin.activity.logic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.bookadmin.tools.utils.CipherUtil;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-19.
 */

public class VailTActivity extends BaseActivity implements View.OnClickListener {


    private String phone;
    private String u_token;

    private ClearEditText etnewPass;
    private ClearEditText etagnPass;
    private TextView btnRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        phone = getIntent().getStringExtra("phone");
        u_token = getIntent().getStringExtra("u_token");

        etnewPass = (ClearEditText) findViewById(R.id.newpassone);
        etagnPass = (ClearEditText) findViewById(R.id.newpasstwo);
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRight:
                final String newPass = etnewPass.getText().toString();
                String agnPass = etagnPass.getText().toString();
                if (TextUtils.isEmpty(newPass)) {
                    ToastUtils.showToastInCenter(VailTActivity.this, 1, "请填写密码", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(agnPass)) {
                    ToastUtils.showToastInCenter(VailTActivity.this, 1, "请填写确认密码", Toast.LENGTH_SHORT);
                    return;
                }
                if(!newPass.equals(agnPass)){
                    ToastUtils.showToastInCenter(VailTActivity.this, 1, "两次密码不一致", Toast.LENGTH_SHORT);
                    return;
                }

                OkHttpUtils
                        .post()
                        .url(Contants.API.BASE_URL + Contants.API.PASS)
                        .addParams("token", BookApplication.getInstance().getKey())
                        .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                        .addParams("key", u_token)
                        .addParams("phone", phone)
                        .addParams("password", CipherUtil.getAESInfo(newPass))
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            final int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if (code == 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(VailTActivity.this, 2, "密码修改成功", Toast.LENGTH_SHORT);
                                        Intent intent = new Intent();
                                        intent.putExtra("password", newPass);
                                        setResult(Contants.RESULT_VAILT, intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }
}
