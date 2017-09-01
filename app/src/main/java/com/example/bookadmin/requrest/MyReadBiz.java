package com.example.bookadmin.requrest;

import android.app.Activity;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.MyReadBean;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.interf.OnPageSeListener;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zt on 2017/5/6.
 */

public class MyReadBiz<T> {

    public static final String TAG = "MyReadBiz";

    //    private static Builder builder;
    private Builder builder;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFERSH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private SpotsDialog spotsDialog;

    protected  MyReadBiz(Builder builder){
        this.builder = builder;
        spotsDialog = new SpotsDialog(builder.mActivity, "拼命加载中...");
        initRefreshLayout();

    }

    private void initRefreshLayout(){

        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);

        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.canLoadMore = true;
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(builder.canLoadMore)
                    loadMore();
                else{
//                    ToastUtils.showShortToast(builder.mActivity, "无更多数据");
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 获取builder
     * @return
     */
//    public Builder newBuilder(){
//
//        builder = new Builder();
//        return builder;
//    }

    public void request(){
        requestData();
    }

    private void showDialog(){
        spotsDialog.show();
    }

    private void dismissDialog(){
        spotsDialog.dismiss();
    }

    /**
     * 类builder
     */
    public static class Builder{

        private static Builder mInstance;
        public static  Builder getInstance(){
            return  mInstance;
        }

        public Builder (Activity activity){
            mInstance = this;
            this.mActivity = activity;
        }

        private Activity mActivity;
        private String url;
        private Map<String, String> params;
        private MaterialRefreshLayout mRefreshLayout;
        private boolean canLoadMore;
        private OnPageSeListener onPageSeListener;

        public Builder setUrl(String url){
            this.url = url;
            return this;
        }
        public Builder setParams(Map<String, String> params){
            this.params = params;
            return this;
        }
        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout){
            this.mRefreshLayout = refreshLayout;
            return this;
        }
        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore;
            return this;
        }
        public Builder setOnPageListener(OnPageSeListener onPageListener) {
            this.onPageSeListener = onPageListener;
            return this;
        }

        public MyReadBiz build(){
            valid();
            return new MyReadBiz(this);
        }

        private void valid(){
            if(this.mActivity == null)
                throw  new RuntimeException("content can't be null");
            if(this.url == null)
                throw  new RuntimeException("url can't be  null");
            if(this.params == null)
                throw  new RuntimeException("search can't be  null");
            if(this.mRefreshLayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }
    }

    /**
     * 刷新
     */
    private void refresh(){
        state = STATE_REFERSH;
        builder.params.put(Contants.PAGE, "1");
        requestData();
    }
    /**
     * 加载更多
     */
    private void loadMore(){
        state = STATE_MORE;
        int currPage = Integer.valueOf(builder.params.get(Contants.PAGE));
        currPage = currPage + 1;
        builder.params.put(Contants.PAGE, String.valueOf(currPage));
        requestData();
    }


    /**
     * 请求数据
     * Activity activity, final ClassificationFragment fragment,
     */
    public void requestData(){
        String page = builder.params.get(Contants.PAGE);
        final String search = builder.params.get(Contants.SEARCH);
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + builder.url)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
                .addParams(Contants.PAGE, page)
                .addParams(Contants.SEARCH, search)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        showDialog();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        builder.mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                error();
                                IniterUtils.noIntent(builder.mActivity, null, null);
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            final String data = obj.getString("data");
                            if(code == 200){

                                final List<MyReadBean> myReadBeens = GsonUtil.GsonToArrayList(data, MyReadBean.class);
                                builder.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissDialog();
                                        if(myReadBeens == null || myReadBeens.size() == 0){
                                            ToastUtils.showToastInCenter(builder.mActivity, 1, "加载不到数据", Toast.LENGTH_SHORT);
                                            builder.canLoadMore = false;
                                        }else {
                                            success(myReadBeens);
                                        }
                                    }
                                });
                            } else if(code == 521) {
                                builder.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(builder.mActivity, 1, "没有数据", Toast.LENGTH_SHORT);
                                        dismissDialog();
                                        noData();
                                    }
                                });
                            } else if(code == 536){
                                builder.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(builder.mActivity, 1, "无借阅历史", Toast.LENGTH_SHORT);
                                        dismissDialog();
                                        noData();
                                    }
                                });
                            } else{

                                error();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }



    private void noData(){
        if(STATE_REFERSH == state){
            builder.mRefreshLayout.finishRefresh();
        }else if(STATE_MORE == state){
            builder.canLoadMore = false;
            builder.mRefreshLayout.finishRefreshLoadMore();
        }
    }
    private void error(){
        dismissDialog();
        if(STATE_REFERSH == state){
            builder.mRefreshLayout.finishRefresh();
        }else if(STATE_MORE == state){
            builder.canLoadMore = false;
            builder.mRefreshLayout.finishRefreshLoadMore();
        }
    }
    private void success(List<MyReadBean> myReadBeens){
        if(STATE_NORMAL == state){
            if(builder.onPageSeListener != null) {
                builder.onPageSeListener.load(myReadBeens);
            }
        }else if(STATE_REFERSH == state){
            builder.mRefreshLayout.finishRefresh();
            if(builder.onPageSeListener != null) {
                builder.onPageSeListener.refresh(myReadBeens);
            }
        }else if(STATE_MORE == state){
            builder.mRefreshLayout.finishRefreshLoadMore();
            if(builder.onPageSeListener != null){
                builder.onPageSeListener.loadMore(myReadBeens);
            }
        }
    }


}
