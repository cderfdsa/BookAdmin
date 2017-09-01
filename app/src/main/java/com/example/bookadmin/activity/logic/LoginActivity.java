package com.example.bookadmin.activity.logic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.BaseActivity;
import com.example.bookadmin.im.MyTLSService;
import com.example.bookadmin.requrest.UserLoginBiz;
import com.example.bookadmin.interf.UserRequestCallback;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.TextUtiles;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.ClearEditText;

import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2017-05-09.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolBar;
    private Button loginBrn;
    private ClearEditText mEtxtName;
    //    private ClearEditText mEtxtPhone;
    private ClearEditText mEtxtPwd;
    private TextView tvReg;
    private TextView tvForget;

    private SpotsDialog spotsDialog;

    private MyTLSService tlsService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolBar = (Toolbar) findViewById(R.id.login_bar);
        loginBrn = (Button) findViewById(R.id.btn_login);
        mEtxtName = (ClearEditText) findViewById(R.id.etxt_name);
//        mEtxtPhone = (ClearEditText) findViewById(R.id.etxt_phone);
        mEtxtPwd = (ClearEditText) findViewById(R.id.etxt_pwd);
        tvReg = (TextView) findViewById(R.id.txt_toReg);
        tvForget = (TextView) findViewById(R.id.tv_forget);

        loginBrn.setOnClickListener(this);
        tvReg.setOnClickListener(this);
        tvForget.setOnClickListener(this);
//        setToolBarReplaceActionBar();

        tlsService = MyTLSService.getInstance();
    }

    /**
     * 用toolBar替换ActionBar
     */
//    private void setToolBarReplaceActionBar() {
//        setSupportActionBar(mToolBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void login(){
        String name = mEtxtName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showToastInCenter(LoginActivity.this, 1, "请输入用户名", Toast.LENGTH_SHORT);
            return;
        }
        if(TextUtiles.checkName(name)){
            ToastUtils.showToastInCenter(LoginActivity.this, 1, "不允许输入特殊符号！", Toast.LENGTH_SHORT);
            return;
        }
        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.showToastInCenter(LoginActivity.this, 1, "请输入密码", Toast.LENGTH_SHORT);
            return;
        }

//        final UserEntity userEntity = new UserEntity("", name, CipherUtil.getAESInfo(pwd), "");
        spotsDialog = new SpotsDialog(this, "正在验证");
        spotsDialog.show();
        UserLoginBiz.requestData(this, name, pwd, new UserRequestCallback() {
            @Override
            public void handleQueryResult(int code) {
                if(spotsDialog != null && spotsDialog.isShowing()){
                    spotsDialog.dismiss();
                }
                if(code == 200){

                    MyTLSService.getInstance().setLastErrno(0);
//                    ToastUtils.showToastInCenter(LoginActivity.this, 2, "登录成功", Toast.LENGTH_SHORT);
                    setResult(Contants.LOGIN_RESULTCODE);
                    finish();

                }

            }

            @Override
            public void handleQueryResultError() {
                if(spotsDialog != null && spotsDialog.isShowing()){
                    spotsDialog.dismiss();
                }
                MyTLSService.getInstance().setLastErrno(-1);
            }

            @Override
            public void handleQueryError() {
                IniterUtils.noIntent(LoginActivity.this, null, spotsDialog);
                MyTLSService.getInstance().setLastErrno(-1);
            }
        });
    }

    private void reg(){
        Intent intent = new Intent(this, RegActivity.class);
        startActivityForResult(intent, Contants.REQUEST_LOGIN_CODE);
    }

    private void forget(){
        Intent intent = new Intent(this, ForgetActivity.class);
        startActivityForResult(intent, Contants.REQUEST_LOGIN_FORGET_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.txt_toReg:
                reg();
                break;
            case R.id.tv_forget:
                forget();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Contants.REQUEST_LOGIN_CODE){
//            if(resultCode == Contants.RESULT_CODE_REG) {
//                if (BookApplication.getInstance().getIntent() == null) {
//                    setResult(Contants.RESULT_CODE_LOGIN_REG);
//                    finish();
//                } else {
//                    BookApplication.getInstance().jumpToTargetActivity(LoginActivity.this);
//                    finish();
//                }
//            }
        }else if(requestCode == Contants.REQUEST_LOGIN_FORGET_CODE){
            if(resultCode == Contants.RESULT_FORGET) {
                String pass = data.getStringExtra("password");
                LogUtils.i(pass);
            }
        }
    }

}
