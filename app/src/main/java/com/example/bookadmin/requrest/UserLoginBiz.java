package com.example.bookadmin.requrest;

import android.app.Activity;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.interf.UserRequestCallback;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.CipherUtil;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.tools.UserInfoCache;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by zt on 2017/5/6.
 */

public class UserLoginBiz {

    public static final String TAG = "UserLoginBiz";

    /**
     * 请求数据
     * Username：用户名
     * Password：密码
     * Phone：手机号
     * Activity activity, final ClassificationFragment fragment,
     */
    public static void requestData(final Activity activity, final String input, final String password, final UserRequestCallback callback){
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.USERLOGIN)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("username", input)
                .addParams("password", CipherUtil.getAESInfo(password))
//                .addParams("phone", userEntity.getPhone())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("onError");
                        callback.handleQueryError();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            final int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if(code == 200){
                                JSONObject jUser = new JSONObject(data);
                                String userId = jUser.getString("id");
                                String name = jUser.getString("name");
                                String phone = jUser.getString("phone");
                                String identifier = jUser.getString("identifier");
                                String sigId = jUser.getString("sig");

                                UserInfo.getInstance().setUserId(userId);
                                UserInfo.getInstance().setNickname(name);
                                UserInfo.getInstance().setPhone(phone);
                                UserInfo.getInstance().setPassword(password);
                                UserInfo.getInstance().setId(identifier);
                                UserInfo.getInstance().setUserSig(sigId);

                                UserInfoCache.saveCache(activity);

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.handleQueryResult(code);
                                    }
                                });
                            }else if(code == 510){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(activity, 1, "用户或密码错误", Toast.LENGTH_SHORT);
                                        callback.handleQueryResultError();
                                    }
                                });
                            }else if(code == 502 || code == 503){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(activity, 1, "用户名称或密码为空", Toast.LENGTH_SHORT);
                                        callback.handleQueryResultError();
                                    }
                                });
                            }else if(code == 521){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(activity, 1, "用户或密码错误", Toast.LENGTH_SHORT);
                                        callback.handleQueryResultError();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
