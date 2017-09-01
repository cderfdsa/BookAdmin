package com.example.bookadmin.requrest;

import android.app.Activity;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.LibAddress;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-05-18.
 */

public class LoadAddress {

    public static final String TAG = "LoadAddress";

    private Activity activity;

    public LoadAddress (Activity activity){
        this.activity = activity;
    }

    public void loadLibAdress(final RequestMainLib requestMainLib){
        //获取定位
        OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写入超时时间
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", BookApplication.getInstance().getKey());
        builder.add("number", String.valueOf(BookApplication.getInstance().getNumber()));
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Contants.API.BASE_URL + Contants.API.APILIBRARY)
                .post(formBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i( "连接服务器失败");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestMainLib.requestLibAddressFaile();
//                        ToastUtils.showToastInCenter(activity, 1, "当前定位失败", Toast.LENGTH_SHORT);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    LogUtils.i( "定位连接服务器成功");
                    String str = response.body().string();
                    JSONObject obj;
                    try {
                        obj = new JSONObject(str);
                        int code = obj.getInt("code");
                        String value = obj.getString("value");
                        String data = obj.getString("data");

                        if (code == 200) {
                            final List<LibAddress> libAddresses = GsonUtil.GsonToArrayList(data, LibAddress.class);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    requestMainLib.requestLibAddress(libAddresses);
                                }
                            });
                        }else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    requestMainLib.requestLibAddressFaile();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestMainLib.requestLibAddressFaile();
                            }
                        });
                    }
                }else{
                    LogUtils.i("连接服务器失败");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestMainLib.requestLibAddressFaile();
                        }
                    });
                }
            }
        });




        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.APILIBRARY)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("onError");

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }

    private RequestMainLib requestMainLib;

    public void setRequestMainLib(LoadAddress.RequestMainLib requestMainLib) {
        this.requestMainLib = requestMainLib;
    }

    public interface RequestMainLib {
        void requestLibAddress(List<LibAddress> libAddresses);
        void requestLibAddressFaile();
    }
}
