package com.example.bookadmin.requrest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.DateBean;
import com.example.bookadmin.bean.LtTime;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-15.
 */

public class OrderGs {

    public static final String TAG = "OrderGs";


    public static void grogshopTime(final Activity activity, int gsId, final DateBean dateBean,
                                    final boolean isAlse, final OnLtTimeListener onLtTimeListener){
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.GROGSHOP_TIME)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("grogshop", String.valueOf(gsId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("onError");
                        onLtTimeListener.OnGropError();
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
                                final List<LtTime>ltTimes = GsonUtil.GsonToArrayList(data, LtTime.class);

                                boolean isToday = TimeUtils.IsToday(dateBean);
                                if(isToday){

                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    long currentTime = System.currentTimeMillis();
                                    String strCurrent = sdf.format(currentTime);
                                    long currentTs = TimeUtils.formatTimeTots(strCurrent);

                                    for(int i = 0; i < ltTimes.size(); i++){
                                        LtTime ltTime = ltTimes.get(i);
                                        String starttime = ltTime.getLt_starttime();
                                        long startTs = TimeUtils.formatTimeTots(starttime);
                                        if(isAlse){//只有还书
                                            if(startTs - currentTs < 0){
                                                ltTimes.remove(i);
                                                i = i - 1;
                                            }
                                        }else{
                                            if(startTs - currentTs < 60 * 60 * 3 * 1000){
                                                ltTimes.remove(i);
                                                i = i - 1;
                                            }
                                        }
                                    }

                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(ltTimes.size() > 0){

                                            if(onLtTimeListener != null) {
                                                onLtTimeListener.OnLtTime(ltTimes);
                                            }
                                        }else{
                                            if(onLtTimeListener != null){
                                                onLtTimeListener.OnGropTimeNoTime();
                                            }
                                        }
//                                        try {
//                                            createTimeDialog(activity, ltTimes, dateBean, isAlse, onGroupTimeListener);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
                                    }
                                });
                            }else if(code == 521){//不存在数据

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

//    private static void createTimeDialog(Activity activity, List<LtTime> ltTimes, final DateBean dateBean, boolean isAlse,
//                                         final OnGroupTimeListener onGroupTimeListener) throws ParseException {
//
//        boolean isToday = TimeUtils.IsToday(dateBean);
//        if(isToday){
//
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//            long currentTime = System.currentTimeMillis();
//            String strCurrent = sdf.format(currentTime);
//            long currentTs = TimeUtils.formatTimeTots(strCurrent);
//
//            for(int i = 0; i < ltTimes.size(); i++){
//                LtTime ltTime = ltTimes.get(i);
//                String starttime = ltTime.getLt_starttime();
//                long startTs = TimeUtils.formatTimeTots(starttime);
//                if(isAlse){//只有还书
//                    if(startTs - currentTs < 0){
//                        ltTimes.remove(i);
//                        i = i - 1;
//                    }
//                }else{
//                    if(startTs - currentTs < 60 * 60 * 3 * 1000){
//                        ltTimes.remove(i);
//                        i = i - 1;
//                    }
//                }
//            }
//
//        }
//        if(ltTimes.size() > 0){
//
//            TimeLogDialog timeLogDialog = new TimeLogDialog(activity, ltTimes);
//            timeLogDialog.show();
//            timeLogDialog.setOnLtTimeClickListener(new TimeLogDialog.OnLtTimeClickListener() {
//                @Override
//                public void OnLtTimeClick(LtTime ltTime) {
//                    if(onGroupTimeListener != null){
//                        onGroupTimeListener.OnGroupTime(ltTime);
//                    }
//                }
//            });
//        }else{
//            if(onGroupTimeListener != null){
//                onGroupTimeListener.OnGropTimeNoTime();
//            }
//        }
//
//    }


//    public interface OnGroupTimeListener{
//        void OnGroupTime(LtTime ltTime);
//        void OnGropTimeNoTime();
//    }

    public interface OnLtTimeListener{
        void OnLtTime(List<LtTime> ltTimes);
        void OnGropTimeNoTime();
        void OnGropError();
    }


    public static void common(final Activity activity, final RequstDialogResult requstDialogResult){
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.APIADDRESS)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
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
                            if (code == 200) {
                                final List<ApiAddress> apiAddresses = GsonUtil.GsonToArrayList(data, ApiAddress.class);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        createDialog(activity, apiAddresses, requstDialogResult);
                                    }
                                });
                            }else if(code == 521){//不存在数据

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static void createDialog(Activity activity, final List<ApiAddress> apiAddresses, final RequstDialogResult requstDialogResult){
        String[] addressGrop = new String[apiAddresses.size()];
        for(int i = 0; i < apiAddresses.size(); i++){
            addressGrop[i] = apiAddresses.get(i).getGs_name();
        }
        new AlertDialog.Builder(activity)
                .setSingleChoiceItems(addressGrop, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requstDialogResult.requestDialogResult(apiAddresses.get(which));
                    }
                })
                .show();
    }


    public static void nearby(final Activity activity, final RequstDialogResult requstDialogResult){
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.COMGROPSHOP)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
//                .addParams("id", BookApplication.getInstance().getUserEntity().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("onError");
                        requstDialogResult.requestApiError();
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
                                final List<ApiAddress> apiAddresses = GsonUtil.GsonToArrayList(data, ApiAddress.class);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        requstDialogResult.requestApiAddress(apiAddresses);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }




    private RequstDialogResult requstDialogResult;

    public void setRequstDialogResult(RequstDialogResult requstDialogResult) {
        this.requstDialogResult = requstDialogResult;
    }

    public interface RequstDialogResult{
        void requestDialogResult(ApiAddress apiAddress);
        void requestApiAddress(List<ApiAddress> apiAddresses);
        void requestApiError();
    }
}
