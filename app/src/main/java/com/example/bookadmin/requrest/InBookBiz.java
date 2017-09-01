package com.example.bookadmin.requrest;

import android.app.Activity;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.InBookBean;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.interf.InbookRequestCallback;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-11.
 */

public class InBookBiz {

    public static final String TAG = "InBookBiz";

    public static void loadInBook(final Activity activity, final InbookRequestCallback callback) {
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.INBOOK)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
                .addParams("address", Contants.getLibAddressId())
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
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if(code == 200){

                                final List<InBookBean> inBookBeens =  GsonUtil.GsonToArrayList(data, InBookBean.class);
                                final List<InBookBean> stayInBookBeens = new ArrayList<InBookBean>();
                                final List<InBookBean> contInBookBeens = new ArrayList<InBookBean>();
                                for(int i = 0; i < inBookBeens.size(); i++){
                                    InBookBean inBookBean = inBookBeens.get(i);
                                    int ub_code = inBookBean.getUb_code();
                                    int b_code = inBookBean.getB_code();
                                    if(b_code == 2) {
                                        if (ub_code == 1) {
                                            stayInBookBeens.add(inBookBean);
                                        } else if (ub_code == 2) {
                                            stayInBookBeens.add(inBookBean);
                                        }
                                    }
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.handleInbookQueryResult(stayInBookBeens, contInBookBeens);
                                    }
                                });

                            }else if(code == 520){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        ToastUtils.showShortToast(activity, "地区id不存在");
                                        LogUtils.i("地区id不存在");
                                    }
                                });
                            }else if(code == 521){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        ToastUtils.showShortToast(activity, "没有待还的图书");
                                        LogUtils.i("没有待还的图书");
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.noInbookQueryResult();
                                            }
                                        });
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
