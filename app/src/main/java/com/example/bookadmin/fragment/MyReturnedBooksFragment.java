package com.example.bookadmin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.activity.me.MyOrderActivity;
import com.example.bookadmin.activity.OrderDetailActivity;
import com.example.bookadmin.R;
import com.example.bookadmin.adapter.MyOrderInAdapter;
import com.example.bookadmin.bean.OrderIn;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.deserializer.BookDeserializerIn;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-25.
 * 还书
 */
public class MyReturnedBooksFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.id_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.listview)
    ListView listview;
    @ViewInject(R.id.tv_nodata)
    TextView tvNodata;

    private ArrayList<OrderIn> orderIns= new ArrayList<>();
    private MyOrderInAdapter myOrderInAdapter;

    private SpotsDialog mDialog;

    private static final int REFRESH_COMPLETE = 0X111;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    init();
                    mSwipeLayout.setRefreshing(false);
                    break;

            }
        };
    };

    public interface OnInLoadListener {
        void onInLoad(boolean isLoad);
    }

    private OnInLoadListener onInLoadListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onInLoadListener = (MyOrderActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderin, container, false);
        ViewUtils.inject(this, view);

        mSwipeLayout.setOnRefreshListener(this);
//        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        myOrderInAdapter = new MyOrderInAdapter(getActivity(), orderIns);

        listview.setAdapter(myOrderInAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("rp_id", orderIns.get(position).getRp_id());
                intent.putExtra("order_code", Contants.ORDER_CODE_IN);
                startActivity(intent);
            }
        });
        myOrderInAdapter.setInClickListener(new MyOrderInAdapter.InClickListener() {
            @Override
            public void InClick(int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("rp_id", orderIns.get(position).getRp_id());
                intent.putExtra("order_code", Contants.ORDER_CODE_IN);
                startActivity(intent);
            }
        });

        mDialog = new SpotsDialog(getActivity(), "拼命加载中....");
        return view;
    }

    @Override
    public void init() {
        mDialog.show();
        try {
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.ORDERIN_USER)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        if(onInLoadListener != null){
                            onInLoadListener.onInLoad(false);
                        }
                        IniterUtils.noIntent(getActivity(), null, mDialog);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if(!data.equals("\"false\"")) {
                                if (code == 200) {

                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(OrderIn.class, new BookDeserializerIn());
                                    Gson gson = gsonBuilder.create();

                                    JsonArray array = new JsonParser().parse(data).getAsJsonArray();

                                    if(orderIns.size() > 0){
                                        orderIns.clear();
                                    }
                                    if (gson != null) {
                                        for (final JsonElement elem : array) {
                                            OrderIn t = gson.fromJson(elem, OrderIn.class);
                                            orderIns.add(t);
                                        }
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissDialog();
                                            myOrderInAdapter.notifyDataSetChanged();
                                        }
                                    });

                                } else if (code == 521) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissDialog();
                                            tvNodata.setVisibility(View.VISIBLE);
                                            listview.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissDialog();
                                        tvNodata.setVisibility(View.VISIBLE);
                                        listview.setVisibility(View.GONE);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if(onInLoadListener != null){
                                onInLoadListener.onInLoad(false);
                            }
                        }
                    }

                });

        }catch (Exception e){
            LogUtils.e(e.toString());
        }
    }

    private void dismissDialog() {
        if(mDialog !=null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        if(onInLoadListener != null){
            onInLoadListener.onInLoad(true);
        }
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
    }


}
