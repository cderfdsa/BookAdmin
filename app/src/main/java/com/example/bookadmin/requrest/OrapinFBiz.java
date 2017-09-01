package com.example.bookadmin.requrest;

import android.app.Activity;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.FirstOrapin;
import com.example.bookadmin.bean.InBookBean;
import com.example.bookadmin.bean.Orapiin;
import com.example.bookadmin.bean.Pack;
import com.example.bookadmin.bean.ShopInBook;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-16.
 */

public class OrapinFBiz {

    public static final String TAG = "OrapinFBiz";

    private Activity activity;
    private List<ShopInBook> mBooks;
    private ApiAddress mApiAddress;

    public OrapinFBiz(Activity activity, List<ShopInBook> mBooks, ApiAddress mApiAddress) {

        this.activity = activity;
        this.mBooks = mBooks;
        this.mApiAddress = mApiAddress;

    }


    public void requestOrapin(final RequestOrapiinResult requstOrapiinResult) {//还书
        String result = jointJsonBook(mBooks);
        LogUtils.i("还书参数 book : " + result);
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.ORAPIN)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("address", Contants.getLibAddressId())
                .addParams("id", UserInfo.getInstance().getUserId())
//                    .addParams("password", caurser.getCu_password())
//                    .addParams("grogshop", String.valueOf(mApiAddress.getGs_id()))
                .addParams("book", result)
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
                            if (code == 210) {
                                final List<FirstOrapin> firstOrapins = iter(data);
                                LogUtils.i("还书第一次返回 data : " + data);
                                LogUtils.i("还书第一次包装实体集合 firstOrapins : " + firstOrapins.toString());
//                                activity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
                                if (firstOrapins != null && firstOrapins.size() > 0) {
//                                                showDialog(firstOrapins, requstOrapiinResult);
                                    requestOrapinTwo(firstOrapins, requstOrapiinResult);
                                }
//                                    }
//                                });
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                            ToastUtils.showShortToast(activity, "还书第一次请求错误：" + code);
                                        LogUtils.i("还书第一次请求错误：" + code);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

//    private void showDialog(final List<FirstOrapin> firstOrapins, final RequestOrapiinResult requstOrapiinResult){
//        StringBuilder showName = new StringBuilder();
//        showName.append("如有包装");
//        for(int i = 0; i < firstOrapins.size(); i++){
//            FirstOrapin firstOrapin = firstOrapins.get(i);
//            String name = firstOrapin.getPack().getPackName();
//            if(!name.equals("-1")){
//                if(i == firstOrapins.size() - 1) {
//                    showName.append(name);
//                }else{
//                    showName.append(name + "、");
//                }
//            }
//        }
//        showName.append("未还，请将包装一起放到书柜中");
//        AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                .setMessage(showName)
//                .create();
//        alertDialog.show();
//
//    }

    private void requestOrapinTwo(final List<FirstOrapin> firstOrapins, final RequestOrapiinResult requstOrapiinResult) {
        String result = jointJsonOrapin(firstOrapins);
        LogUtils.i("还书第二次参数 book : " + result);
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.ORAPIN)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("address", Contants.getLibAddressId())
                .addParams("id", UserInfo.getInstance().getUserId())
//                .addParams("password", caurser.getCu_password())
//                .addParams("case", caurser.getCu_case())
                .addParams("grogshop", String.valueOf(mApiAddress.getGs_id()))
                .addParams("book", result)
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
                            LogUtils.i("还书第二次成功 data : " + data);
                            if (code == 211) {
                                JSONObject jsonObject = new JSONObject(data);
                                Iterator iterator = jsonObject.keys();
                                String key = "";
                                while (iterator.hasNext()) {
                                    key = iterator.next().toString();
                                }
                                if (key.equals("success")) {
                                    String success = jsonObject.getString("success");
                                    final List<Orapiin> orapiins = GsonUtil.GsonToArrayList(success.toString(), Orapiin.class);
                                    LogUtils.i("还书第二次成功 outObj : " + orapiins.toString());
                                    if (orapiins != null) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                requstOrapiinResult.requstOrapiinResult(orapiins, firstOrapins);
                                            }
                                        });
                                    }
                                } else if (key.equals("error")) {
                                    String error = jsonObject.getString("error");
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            ToastUtils.showShortToast(activity, "");
                                            requstOrapiinResult.requstOrapiinResultError(mBooks);
                                        }
                                    });
                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            ToastUtils.showShortToast(activity, "错误：" + code);
                                            LogUtils.i("第二次请求还书错误：" + code);
                                        }
                                    });
                                }

                            } else if (code == 210) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        requstOrapiinResult.requstNodata();
                                    }
                                });
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        ToastUtils.showShortToast(activity, "还书第二次错误：" + code);
                                        LogUtils.i("第二次请求还书错误：" + code);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    public static List<FirstOrapin> iter(String data) throws JSONException {//{"10":{"3":{"pack":"123","0":52},"11":{"pack":"wwe978","0":36}}}
//        data = "{\"10\":{\"21\":{\"pack\":\"-1\",\"0\":1},\"3\":{\"pack\":\"123\",\"0\":52}}}";
        List<FirstOrapin> firstOrapins = new ArrayList<>();

        JSONObject dataJson = new JSONObject(data);
        Iterator dataIter = dataJson.keys();

        while (dataIter.hasNext()) {


            String dataKey = (String) dataIter.next();//10
            String dataValue = dataJson.getString(dataKey);

//            JSONArray jsonArray = new JSONArray(dataValue);
//            JSONObject packJson = jsonArray.getJSONObject(0);
            JSONObject packJson = new JSONObject(dataValue);
            Iterator packIter = packJson.keys();
            while (packIter.hasNext()) {
                FirstOrapin firstOrapin = new FirstOrapin();
                firstOrapin.setAddress(dataKey);

                String packKey = (String) packIter.next();
                String packValue = packJson.getString(packKey);

                Pack pack = new Pack();
                pack.setPackId(packKey);
                if (packValue != null && !packValue.equals("null")) {
                    JSONObject arrJson = new JSONObject(packValue);
                    Iterator arrIter = arrJson.keys();

                    List<String> strings = new ArrayList<>();
                    while (arrIter.hasNext()) {
                        String arrKey = (String) arrIter.next();
                        String arrValue = arrJson.getString(arrKey);

                        if (arrKey.equals("pack")) {
                            pack.setPackName(arrValue);
                        } else {
                            strings.add(arrValue);
                            pack.setBook_id(strings);
                        }
                    }
                }
                firstOrapin.setPack(pack);
                firstOrapins.add(firstOrapin);
            }

        }
        return firstOrapins;
    }

    private static String jointJsonOrapin(List<FirstOrapin> firstOrapins) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"code\":\"1\"");
        for (int i = 0; i < firstOrapins.size(); i++) {
            FirstOrapin orapin = firstOrapins.get(i);
            //{"code":"1","0":{"pack":"5","library":"5","0":"5","0":"5"}}
            Pack pack = orapin.getPack();
            String name = pack.getPackName();

            List<String> bookIss = pack.getBook_id();
            if (name.equals("-1")) {
                builder.append(",\"" + i + "\":{\"pack\":\" " + "-1" + " \",\"library\":\" " + orapin.getAddress() + "\"");
            } else {
                builder.append(",\" " + i + "\":{\"pack\":\" " + pack.getPackId() + " \",\"library\":\" " + orapin.getAddress() + "\"");
            }
            for (int j = 0; j < bookIss.size(); j++) {
                builder.append(",\" " + j + " \":\" " + bookIss.get(j) + " \"");

            }

            builder.append("}");
        }
        builder.append("}");
        return builder.toString().replace(" ", "");
    }

    private static String jointJsonBook(List<ShopInBook> mBooks) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"code\":\"0\"");
        //{"code":"0","0":1,"1":2,"2":3,"3":4,"4":5}

        for (int i = 0; i < mBooks.size(); i++) {
            ShopInBook book = mBooks.get(i);
            InBookBean bookBean = book.getInBookBean();
            builder.append(",\" " + i + "\":" + bookBean.getB_id());
        }
        builder.append("}");
        return builder.toString().replace(" ", "");
    }


    private RequestOrapiinResult requestOrapiinResult;

    public void setRequstOrapinFResult(RequestOrapiinResult requestOrapiinResult) {
        this.requestOrapiinResult = requestOrapiinResult;
    }

    public interface RequestOrapiinResult {
        void requstOrapiinResult(List<Orapiin> orapiins, List<FirstOrapin> firstOrapins);

        void requstOrapiinResultError(List<ShopInBook> shopInBooks);

        void requstNodata();
    }


}
