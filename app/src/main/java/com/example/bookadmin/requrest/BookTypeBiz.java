package com.example.bookadmin.requrest;

import android.app.Activity;
import android.util.SparseArray;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.BookTypeBean;
import com.example.bookadmin.bean.OrderType;
import com.example.bookadmin.bean.ScreenAll;
import com.example.bookadmin.bean.ScreenPublish;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-09.
 */

public class BookTypeBiz {

    public static final String TAG = "BookTypeBiz";

    /**
     * 请求数据
     * address：书库地址id
     * booktype：图书分类（nll）s_id
     * ordertype：排序分类（1）
     * page：页数（1）
     * search：搜索（null）书名、作者
     * Activity activity, final ClassificationFragment fragment,
     */
    public static void requestData(final Activity activity,  final BookStypeRequestCallback callback){

        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.BOOKSTYPE)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {

                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if(code == 200){
                                final ArrayList<BookTypeBean> groups = new ArrayList<BookTypeBean>();
                                BookTypeBean b = new BookTypeBean(-1, "", "默认", -1, "", "", "");
                                groups.add(b);
                                final SparseArray<LinkedList<BookTypeBean>> children = new SparseArray<LinkedList<BookTypeBean>>();
                                final List<BookTypeBean> BookTypeBeans = GsonUtil.GsonToArrayList(data, BookTypeBean.class);
                                for(int i = 0; i < BookTypeBeans.size(); i++){
                                    BookTypeBean bookTypeBean = BookTypeBeans.get(i);
                                    int sParentid = bookTypeBean.getS_parentid();
                                    if(sParentid == 0){

                                        groups.add(bookTypeBean);
                                        BookTypeBeans.remove(i);
                                        i--;
                                    }
                                }
                                for(int i = 0; i < groups.size(); i++){
                                    BookTypeBean bookTypeBean = groups.get(i);
                                    int parentid = bookTypeBean.getS_id();
                                    LinkedList<BookTypeBean> tItem = new LinkedList<BookTypeBean>();
                                    if(parentid == -1){
                                        BookTypeBean b1 = new BookTypeBean(-1, "", "默认", -1, "", "", "");
                                        tItem.add(b1);
                                    }else {

                                        for (int j = 0; j < BookTypeBeans.size(); j++) {
                                            BookTypeBean bean = BookTypeBeans.get(j);
                                            int s_p = bean.getS_parentid();
                                            if (s_p == parentid) {
                                                tItem.add(bean);
                                                BookTypeBeans.remove(j);
                                                j--;
                                            }

                                        }
                                    }
                                    children.put(i, tItem);
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.handleQueryResult(groups, children);
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    public static void requestBookOrder(final Activity activity,  final BookOrderRequestCallback callback){

        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.BOOKORDER)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");

                            if(code == 200){
                                List<OrderType>orderTypes = GsonUtil.GsonToArrayList(data, OrderType.class);
                                final List<OrderType>orderTypes2 = new ArrayList<OrderType>();
                                for(OrderType orderType : orderTypes){
                                    if(orderType.getOt_zhus().equals("默认")){
                                        orderTypes2.add(orderType);
                                    }else if(orderType.getOt_zhus().equals("畅销度正序")){
                                        orderType.setOt_zhus("热销");
                                        orderTypes2.add(orderType);
                                    }else if(orderType.getOt_zhus().equals("好评正序")){
                                        orderType.setOt_zhus("好评");
                                        orderTypes2.add(orderType);
                                    }else if(orderType.getOt_zhus().equals("最新正序")){
                                        orderType.setOt_zhus("最新");
                                        orderTypes2.add(orderType);
                                    }

                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.handleQueryResult(orderTypes2);
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void requestBookScreen(final Activity activity,  final BookScreenRequestCallback callback){

        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.SCREEN)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");

                            if(code == 200){

                                JSONObject object = new JSONObject(data);
                                String publish = object.getString("publish");
                                final List<ScreenPublish>Publishs = GsonUtil.GsonToArrayList(publish, ScreenPublish.class);
                                for(ScreenPublish pub : Publishs){
                                    pub.setChecked(false);
                                }

                                JSONArray arrayAuthor = new JSONArray();
                                arrayAuthor = object.getJSONArray("author");
                                final List<ScreenAll>authors = new ArrayList<ScreenAll>();
                                for(int i = 0; i < arrayAuthor.length(); i++){
                                    String var = (String) arrayAuthor.get(i);
                                    ScreenAll screenAuthor = new ScreenAll();
                                    screenAuthor.setName(var);
                                    screenAuthor.setChecked(false);
                                    authors.add(screenAuthor);
                                }

                                JSONArray arrayTitle = new JSONArray();
                                arrayTitle = object.getJSONArray("title");
                                final List<ScreenAll> tieles = new ArrayList<ScreenAll>();
                                for(int i = 0; i < arrayTitle.length(); i++){
                                    String var = (String) arrayTitle.get(i);
                                    ScreenAll screenTitle = new ScreenAll();
                                    screenTitle.setName(var);
                                    screenTitle.setChecked(false);
                                    tieles.add(screenTitle);
                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.handleQueryResult(Publishs, authors, tieles);
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }


    public interface BookScreenRequestCallback {
        public void handleQueryResult(List<ScreenPublish> publishes, List<ScreenAll>authors, List<ScreenAll>titles);
    }

    public interface BookStypeRequestCallback {
        public void handleQueryResult(ArrayList<BookTypeBean> groups, SparseArray<LinkedList<BookTypeBean>> children);
    }

    public interface BookOrderRequestCallback {
        public void handleQueryResult(List<OrderType>orderTypes);
    }

}
