package com.example.bookadmin.requrest;

import android.app.Activity;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.Orapiout;
import com.example.bookadmin.bean.ShoppingCart;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-05-16.
 */

public class OrapioutBiz {

    public static final String TAG = "OrapioutBiz";


    public static void requestOrapiout(final Activity activity, List<ShoppingCart> carts, ApiAddress mApiAddress,
                                 final RequstOrapioutResult requstOrapioutResult) {
            String jsonresult = jointJson(carts);
            LogUtils.i("借书参数 book : " + jsonresult);
            OkHttpUtils
                    .post()
                    .url(Contants.API.BASE_URL + Contants.API.ORAPIOUT)
                    .addParams("token", BookApplication.getInstance().getKey())
                    .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                    .addParams("address", Contants.getLibAddressId())
                    .addParams("id", UserInfo.getInstance().getUserId())
//                    .addParams("grogshop", String.valueOf(mApiAddress.getGs_id()))
//                    .addParams("password", caurser.getCu_password())
//                    .addParams("case", caurser.getCu_case())
                    .addParams("book", jsonresult)
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onResponse(String response, int id) {
                            JSONObject obj;
                            try {//借
                                obj = new JSONObject(response);
                                final int code = obj.getInt("code");
                                String value = obj.getString("value");
                                String data = obj.getString("data");
                                if (code == 200) {
                                    final Orapiout orapiout = GsonUtil.GsonToBean(data, Orapiout.class);
                                    LogUtils.i("借书返回 data : " + data);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            requstOrapioutResult.requstOrapioutResult(orapiout);
                                        }
                                    });
                                } else if (code == 527) {//全被占用或预约失败
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToastInCenter(activity, 1, "没有图书", Toast.LENGTH_SHORT);
                                            requstOrapioutResult.requstOrapioutError();
                                        }
                                    });
                                }else{
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            ToastUtils.showShortToast(activity, "错误：" + code);
                                            requstOrapioutResult.requstOrapioutError();
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


    }

    private RequstOrapioutResult requstOrapioutResult;

    public void setRequstOrapioutResult(RequstOrapioutResult requstOrapioutResult) {
        this.requstOrapioutResult = requstOrapioutResult;
    }

    public interface RequstOrapioutResult {
        void requstOrapioutResult(Orapiout orapiout);
        void requstOrapioutError();
    }

    private static String jointJson(List<ShoppingCart> carts) {
        String jsonresult = "";//定义返回字符串
        try {
            JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象
            for (ShoppingCart cart : carts) {
                JSONObject object = new JSONObject();

                object.put("book", cart.getBs_id());//向pet对象里面添加值
//                object.put("number", cart.getCount());//向pet对象里面添加值
                object.put("number", "1");//向pet对象里面添加值
                jsonarray.put(object);
            }
            jsonresult = jsonarray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonresult;
    }


}
