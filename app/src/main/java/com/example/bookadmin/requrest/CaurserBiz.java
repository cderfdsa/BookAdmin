package com.example.bookadmin.requrest;

import android.app.Activity;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.DateBean;
import com.example.bookadmin.bean.LtTime;
import com.example.bookadmin.bean.Orapiin;
import com.example.bookadmin.bean.Orapiout;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-16.
 */

public class CaurserBiz {

    public static final String TAG = "CaurserBiz";

    public static void requsetCaurser(final Activity activity, DateBean dateBean, LtTime ltTime, String grogshop,
                                       List<Orapiin> orapiins, Orapiout orapiout, final RequstCaurserResult requstCaurserResult) {
        String order = jointJson(orapiout, orapiins);
        LogUtils.i("预约书柜参数 grogshop : " + order);
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.CAUSER)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
                .addParams("order", order)
                .addParams("date", dateBean.getStrDate())
                .addParams("time", ltTime.getLt_id())
                .addParams("grogshop", grogshop)
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
                            String data = obj.getString("data");
                            if (code == 200) {
//                                final Caurser caurser = GsonUtil.GsonToBean(data, Caurser.class);
                                //{"out":[{"out_code":1,"out_order":35,"out_case":"2"}],"in":null}
                                LogUtils.i("书柜预约成功 data : " + data);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        requstCaurserResult.requstCaurserResult();
                                    }
                                });
                            } else if (code == 546) {//全被占用或预约失败
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        requstCaurserResult.requstCaurserResultError(code);
                                    }
                                });
                            } else if(code == 511){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        requstCaurserResult.requstCaurserResult();
                                    }
                                });
                            }else{
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        requstCaurserResult.requstCaurserResultError(code);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static String jointJson(Orapiout orapiout, List<Orapiin> orapiins) {
        String result = "";
        String in = "";
        String out = "";
        if(orapiout != null){
            out = orapiout.getRp_id();
        }
        if(orapiins != null && orapiins.size() > 0){
            for(Orapiin orapiin : orapiins){
                in = in + orapiin.getRp_id() + ",";
            }
            if(in.endsWith(",")){
               in = in.substring(0, in.length() - 1);
            }
        }
        result = "{\"out\":["+ out + "],\"in\":[" + in + "]}";
        return result.replace(" ", "");
    }


    public interface RequstCaurserResult{
        void requstCaurserResult();
        void requstCaurserResultError(int code);
    }
}
