package com.example.bookadmin.requrest;

import android.app.Activity;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.interf.UserRequestCallback;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by zt on 2017/5/6.
 */

public class UserAddBiz {

    public static final String TAG = "UserAddBiz";

    /**
     * 请求数据
     * Username：用户名
     * Password：密码
     * Phone：手机号
     * Activity activity, final ClassificationFragment fragment,
     */
    public static void requestData(final Activity activity, String nickName, String password, String phone,
                                   final UserRequestCallback callback){
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.USERADD)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("username", nickName)
                .addParams("password", password)
                .addParams("phone", phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i( "onError");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.handleQueryError();
                            }
                        });

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            final int code = obj.getInt("code");
                            String value = obj.getString("value");
                            final String data = obj.getString("data");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(code == 200){
                                            callback.handleQueryResult(code);
                                    }else{
                                        callback.handleQueryResultError();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
