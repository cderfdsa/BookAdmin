package com.example.bookadmin.requrest;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.BookResult;
import com.example.bookadmin.bean.PassDetail;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.TimeUtils;
import com.example.bookadmin.tools.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-06-30.
 */

public class GetCasepass {


    public static void getPassWord(final Activity activity, String rpId, final String orderCode, final IGetCaseListener iGetCaseListener) {
        LogUtils.e("getPassWord");
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.CASEPASS)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
                .addParams("order", rpId)
                .addParams("code", orderCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        IniterUtils.noIntent(activity, null, null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if (code == 200) {
                                JSONObject datajson = new JSONObject(data);
                                JSONObject casejson = datajson.getJSONObject("case");
                                final PassDetail passDetail = GsonUtil.GsonToBean(casejson.toString(), PassDetail.class);
                                List<BookResult> bookResults = null;
                                if (orderCode.equals(Contants.ORDER_CODE_OUT)) {//借书
                                    JSONArray jsonArray = datajson.getJSONArray("out");
                                    if (jsonArray != null) {
                                        bookResults = GsonUtil.GsonToArrayList(jsonArray.toString(), BookResult.class);
                                    }
                                } else if (orderCode.equals(Contants.ORDER_CODE_IN)) {//还书
                                    JSONArray jsonArray = datajson.getJSONArray("in");
                                    if (jsonArray != null) {
                                        bookResults = GsonUtil.GsonToArrayList(jsonArray.toString(), BookResult.class);
                                    }
                                }

                                final List<BookResult> finalBookResults = bookResults;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iGetCaseListener.getPassSuccess(passDetail, finalBookResults);
                                    }
                                });
                            } else if (code == 521) {//没有密码
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iGetCaseListener.getPassFailure();
                                    }
                                });

                            } else if (code == 516) {//没有密码
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (orderCode.equals(Contants.ORDER_CODE_OUT)) {//借书
                                            iGetCaseListener.getPassOverdue();
                                        } else if (orderCode.equals(Contants.ORDER_CODE_IN)) {//还书
                                            iGetCaseListener.getPassFailure();
                                        }
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface IGetCaseListener {
        void getPassSuccess(PassDetail passDetail, List<BookResult> bookResults);

        void getPassOverdue();

        void getPassFailure();
    }

}
