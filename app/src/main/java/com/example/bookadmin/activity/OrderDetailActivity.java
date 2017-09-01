package com.example.bookadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.adapter.BaseAdapter;
import com.example.bookadmin.bean.BookResult;
import com.example.bookadmin.bean.DetailExpress;
import com.example.bookadmin.bean.DetailOrderBooks;
import com.example.bookadmin.bean.DetailOrderSimple;
import com.example.bookadmin.bean.OrderState;
import com.example.bookadmin.bean.PassDetail;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.requrest.GetCasepass;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.TimeUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.tools.deserializer.DetailDeserializerExpress;
import com.example.bookadmin.tools.deserializer.DetailDeserializerOrder;
import com.example.bookadmin.adapter.OrderDetailAdapter;
import com.example.bookadmin.widget.FullyLinearLayoutManager;
import com.example.bookadmin.widget.RecyclerScrollview;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-05-25.
 */

public class OrderDetailActivity extends IMBaseActivity {

    @ViewInject(R.id.recyclerScrollview)
    RecyclerScrollview recyclerScrollview;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.od_state1)
    TextView odState1;
    @ViewInject(R.id.od_state2)
    TextView odState2;
    @ViewInject(R.id.od_state3)
    TextView odState3;
    @ViewInject(R.id.od_state4)
    TextView odState4;
    @ViewInject(R.id.od_state5)
    TextView odState5;
    @ViewInject(R.id.od_id)
    TextView odId;
    @ViewInject(R.id.od_name)
    TextView odName;
    @ViewInject(R.id.od_baddress)
    TextView odbAddress;
    @ViewInject(R.id.od_laddress)
    TextView odlAddress;
    @ViewInject(R.id.od_time)
    TextView odTime;
    @ViewInject(R.id.od_recycler)
    RecyclerView recyclerView;

    @ViewInject(R.id.lineat_l)
    LinearLayout linearl;
    @ViewInject(R.id.lineat_b)
    LinearLayout linearb;
    @ViewInject(R.id.linear_pass)
    LinearLayout linearPass;
    @ViewInject(R.id.linear_state)
    LinearLayout linerState;
    @ViewInject(R.id.linear_case)
    LinearLayout linearCase;
    @ViewInject(R.id.od_btn_pass)
    Button btnPass;
    @ViewInject(R.id.od_btn_case)
    Button btnCase;

    @ViewInject(R.id.od_case_date)
    TextView caseDate;
    @ViewInject(R.id.od_case_time)
    TextView caseTime;
    @ViewInject(R.id.od_case_bxname)
    TextView caseBxname;
    @ViewInject(R.id.od_case_pass)
    TextView casePass;
    @ViewInject(R.id.od_bz_sn)
    TextView bzsn;
    @ViewInject(R.id.showpass)
    LinearLayout showpass;
    @ViewInject(R.id.showtime)
    LinearLayout showtime;
    @ViewInject(R.id.linear_address)
    LinearLayout linear_address;

    private String orderCode;
    private String rpId;

    private List<DetailOrderBooks> detailOrderBooks;
    private DetailExpress detailExpress;
    private DetailOrderSimple detailOrderSimple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        ViewUtils.inject(this);

        rpId = getIntent().getStringExtra("rp_id");
        orderCode = getIntent().getStringExtra("order_code");
        setToolBarReplaceActionBar();
        initData();
        if (orderCode.equals(Contants.ORDER_CODE_IN)) {//还书,获取密码
            linearPass.setVisibility(View.VISIBLE);
        } else if (orderCode.equals(Contants.ORDER_CODE_OUT)) {
            linearPass.setVisibility(View.GONE);
        }
        linearCase.setVisibility(View.GONE);
        recyclerScrollview.setVisibility(View.GONE);
        getPassWord();
    }


    private void setToolBarReplaceActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setOdState() {
//        StringBuilder builderState = new StringBuilder();
        if (detailExpress != null) {
            OrderState orderState1 = detailExpress.getOrderState1();
            if (orderState1 != null) {
                String name = orderState1.getUser();
                String sTime = TimeUtils.formarTime(orderState1.getTime());
//                builderState.append("\n " + sTime + "     " + name + "已下单");
                odState1.setVisibility(View.VISIBLE);
                odState1.setText(sTime + "     " + name + "已下单");
            }
            OrderState orderState2 = detailExpress.getOrderState2();
            if (orderState2 != null) {
                String name = orderState2.getUser();
                String sTime = TimeUtils.formarTime(orderState2.getTime());
//                builderState.append("\n " + sTime + "     " + name + "已打包");
                odState2.setVisibility(View.VISIBLE);
                odState2.setText(sTime + "     " + name + "已打包");
            }
            OrderState orderState3 = detailExpress.getOrderState3();
            if (orderState3 != null) {
                String name = orderState3.getUser();
                String sTime = TimeUtils.formarTime(orderState3.getTime());
//                builderState.append("\n " + sTime + "     " + name + "正在配送");
                odState3.setVisibility(View.VISIBLE);
                odState3.setText(sTime + "     " + name + "正在配送");
            }
            OrderState orderState4 = detailExpress.getOrderState4();
            if (orderState4 != null) {
                String name = orderState4.getUser();
                String sTime = TimeUtils.formarTime(orderState4.getTime());
//                builderState.append("\n " + sTime + "     " + name + "已放入书柜");
                odState4.setVisibility(View.VISIBLE);
                odState4.setText(sTime + "     " + name + "已放入书柜");
            }
            OrderState orderState5 = detailExpress.getOrderState5();
            if (orderState5 != null) {
                String name = orderState5.getUser();
                String sTime = TimeUtils.formarTime(orderState5.getTime());
//                builderState.append("\n " + sTime + "     " + name + "已取书");
                odState5.setVisibility(View.VISIBLE);
                odState5.setText(sTime + "     " + name + "已取书");
            }
            if(orderState1 != null || orderState2 != null || orderState3 != null || orderState4 != null || orderState5 != null) {
                linerState.setVisibility(View.VISIBLE);
            }else{
                linerState.setVisibility(View.GONE);
            }
        }

    }



    private void setOdOther() {
        odId.setText(detailOrderSimple.getRp_name());
    if(detailOrderSimple.getGs_name() != null) {
        odName.setText(detailOrderSimple.getGs_name());
        linear_address.setVisibility(View.VISIBLE);
    }else{
        linear_address.setVisibility(View.GONE);
    }
        String sTime = TimeUtils.formarTime(detailOrderSimple.getRp_intime());
        odTime.setText(sTime);
    }

    private void setbAddress() {
        StringBuilder builderbAddress = new StringBuilder();
        String bregion1 = detailOrderSimple.getB1_region();
        if (bregion1 != null) {
            builderbAddress.append(bregion1);
        }
        String bregion2 = detailOrderSimple.getB2_region();
        if (bregion2 != null) {
            builderbAddress.append("  " + bregion2);
        }
        String bregion3 = detailOrderSimple.getB3_region();
        if (bregion3 != null) {
            builderbAddress.append("  " + bregion3);
        }
        String adName = detailOrderSimple.getAd_name();
        if (adName != null) {
            builderbAddress.append("  " + adName);
        }
//        String gsName = detailOrderSimple.getGs_name();
//        if (gsName != null) {
//            builderbAddress.append("  " + gsName);
//        }
        if (builderbAddress.length() > 0) {
            odbAddress.setText(builderbAddress.toString());
        } else {
            linearb.setVisibility(View.GONE);
        }
    }

    private void setlAddress() {
        StringBuilder builderlAddress = new StringBuilder();
        String lregion1 = detailOrderSimple.getL1_region();
        if (lregion1 != null) {
            builderlAddress.append(lregion1);
        }
        String lregion2 = detailOrderSimple.getL2_region();
        if (lregion2 != null) {
            builderlAddress.append("  " + lregion2);
        }
        String lregion3 = detailOrderSimple.getL3_region();
        if (lregion3 != null) {
            builderlAddress.append("  " + lregion3);
        }
        String laddress = detailOrderSimple.getL_address();
        if (laddress != null) {
            builderlAddress.append("  " + laddress);
        }
        String lname = detailOrderSimple.getL_name();
        if (lname != null) {
            builderlAddress.append("  " + lname);
        }
        if (builderlAddress.length() > 0) {
            odlAddress.setText(builderlAddress.toString());
        } else {
            linearl.setVisibility(View.GONE);
        }
    }

    private void setListAdapter() {
        OrderDetailAdapter mAdapter = new OrderDetailAdapter(this, detailOrderBooks);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OrderDetailActivity.this, BookAttrDetailActivity.class);

                intent.putExtra(Contants.BS_ID, detailOrderBooks.get(position).getBs_id());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void orderCase(){
        if (orderCode.equals(Contants.ORDER_CODE_IN)) {//还书,获取密码
            String rpBcode = detailOrderSimple.getRp_bcode();
            if(rpBcode.equals("0")){//显示重新获取
                linearPass.setVisibility(View.GONE);
                linearCase.setVisibility(View.VISIBLE);
            }else if(rpBcode.equals("6")){//显示获取密码
                linearPass.setVisibility(View.VISIBLE);
                linearCase.setVisibility(View.GONE);
            }else{
                linearPass.setVisibility(View.GONE);
                linearCase.setVisibility(View.GONE);
            }

        } else if (orderCode.equals(Contants.ORDER_CODE_OUT)) {//借书
            String rpBcode = detailOrderSimple.getRp_bcode();
            if(rpBcode.equals("0")){//显示重新获取
                linearPass.setVisibility(View.GONE);
                linearCase.setVisibility(View.VISIBLE);
            }else if(rpBcode.equals("4")){//显示获取密码
                linearPass.setVisibility(View.VISIBLE);
                linearCase.setVisibility(View.GONE);
            }else{
                linearPass.setVisibility(View.GONE);
                linearCase.setVisibility(View.GONE);
            }
        }
//        linearPass.setVisibility(View.VISIBLE);//xiugai
    }


    private void initData() {

        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.ORDER_DETAIL)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("order", rpId)
                .addParams("code", orderCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        IniterUtils.noIntent(OrderDetailActivity.this, recyclerScrollview, null);
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
                                JSONObject jsonObject = new JSONObject(data);
                                String order = jsonObject.getString("order");
                                String books = jsonObject.getString("books");
                                String express = jsonObject.getString("express");

                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.registerTypeAdapter(DetailExpress.class, new DetailDeserializerOrder());
                                Gson gsonOrder = gsonBuilder.create();
                                detailOrderSimple = gsonOrder.fromJson(order, DetailOrderSimple.class);

                                gsonBuilder.registerTypeAdapter(DetailExpress.class, new DetailDeserializerExpress());
                                Gson gsonExpress = gsonBuilder.create();
                                detailExpress = gsonExpress.fromJson(express, DetailExpress.class);

                                detailOrderBooks = GsonUtil.GsonToArrayList(books, DetailOrderBooks.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerScrollview.setVisibility(View.VISIBLE);
                                        setOdState();
                                        setOdOther();
                                        setbAddress();
                                        setlAddress();
                                        setListAdapter();
                                        orderCase();
                                    }
                                });

                            } else if (code == 521) {
                                recyclerScrollview.setVisibility(View.VISIBLE);
                                ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "密码失效,请重新预约书柜", Toast.LENGTH_SHORT);
                                linearPass.setVisibility(View.GONE);
                                linearCase.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    private void getPassWord(){
        GetCasepass.getPassWord(this, rpId, orderCode, new GetCasepass.IGetCaseListener() {
            @Override
            public void getPassSuccess(PassDetail passDetail, List<BookResult> bookResults) {
                final String cuDate = passDetail.getCu_date();
                final String startTime = passDetail.getLt_starttime();
                final String endTime = passDetail.getLt_endtime();
                caseDate.setText(cuDate);
                caseTime.setText(startTime + " --- " + endTime);
                showpass.setVisibility(View.VISIBLE);
                showtime.setVisibility(View.VISIBLE);
                caseBxname.setText(passDetail.getBx_name());
                casePass.setText(passDetail.getCu_password());
                StringBuilder sb = new StringBuilder();
                if(bookResults != null && bookResults.size() > 0) {
                    for (BookResult bookResult : bookResults) {
                        if(bookResult.getBz_sn() != null && !bookResult.getBz_sn().equals("null")) {
                            sb.append(bookResult.getBz_sn() + " ");
                        }
                    }
                }
                if(sb.length() > 0) {
                    bzsn.setText(sb.toString());
                }else{
                    bzsn.setText("无");
                }
            }

            @Override
            public void getPassOverdue() {

            }

            @Override
            public void getPassFailure() {

            }
        });
    }

    @OnClick(R.id.od_btn_pass)
    public void getPassWord(View view) {
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
                        IniterUtils.noIntent(OrderDetailActivity.this, null, null);
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
                                if(orderCode.equals(Contants.ORDER_CODE_OUT)){//借书
                                    JSONArray jsonArray = datajson.getJSONArray("out");
                                    if(jsonArray != null){
                                        bookResults = GsonUtil.GsonToArrayList(jsonArray.toString(), BookResult.class);
                                    }
                                }else if(orderCode.equals(Contants.ORDER_CODE_IN)){//还书
                                    JSONArray jsonArray = datajson.getJSONArray("in");
                                    if(jsonArray != null){
                                        bookResults = GsonUtil.GsonToArrayList(jsonArray.toString(), BookResult.class);
                                    }
                                }
                                final String cuDate = passDetail.getCu_date();
                                final String startTime = passDetail.getLt_starttime();
                                final String endTime = passDetail.getLt_endtime();

                                String startStr = cuDate + " " + startTime;
                                String endStr = cuDate + " " + endTime;
                                long start = TimeUtils.formatCaseTimeTots(startStr);
                                long end = TimeUtils.formatCaseTimeTots(endStr);
                                long current = System.currentTimeMillis();

                                final List<BookResult> finalBookResults = bookResults;

                                if(current > start && current < end){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            caseDate.setText(cuDate);
                                            caseTime.setText(startTime + " --- " + endTime);
                                            showpass.setVisibility(View.VISIBLE);
                                            showtime.setVisibility(View.VISIBLE);
                                            caseBxname.setText(passDetail.getBx_name());
                                            casePass.setText(passDetail.getCu_password());
                                            StringBuilder sb = new StringBuilder();
                                            if(finalBookResults != null && finalBookResults.size() > 0) {
                                                for (BookResult bookResult : finalBookResults) {
                                                    if(bookResult.getBz_sn() != null && !bookResult.getBz_sn().equals("null")) {
                                                        sb.append(bookResult.getBz_sn() + " ");
                                                    }
                                                }
                                            }
                                            if(sb.length() > 0) {
                                                bzsn.setText(sb.toString());
                                            }else{
                                                bzsn.setText("无");
                                            }
                                        }
                                    });
                                }else if(current > end){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "预约书柜已过期，请重新预约！", Toast.LENGTH_SHORT);
                                            linearPass.setVisibility(View.GONE);
                                            linearCase.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else if(current < start){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "预约书柜时间未到！", Toast.LENGTH_SHORT);
                                            caseDate.setText(cuDate);
                                            caseTime.setText(startTime + " --- " + endTime);
                                            showtime.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }



//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {

//                                        String time = passDetail.getCu_time();
//                                        long index = (System.currentTimeMillis() / 1000 - Long.parseLong(time));
//                                        if( index >= 0 && index <= 24 * 60 * 60) {
//
//                                            odPass.setText("密码：" + passDetail.getCu_password());
////                                            obadname.setText("地址：" + passDetail.getAd_name());
//                                            obgsname.setText("地址：" + passDetail.getGs_name());
//                                            obbxname.setText("柜号：" + passDetail.getBx_name());
//                                            odCuTime.setText(TimeUtils.formarTime(time));
//                                        }else if(index < 0){//有书柜
//                                            ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "预约书柜时间未到！", Toast.LENGTH_SHORT);
//                                            odCuTime.setText("书柜预约时间：" + TimeUtils.formarTime(time));
//                                        }else{//重新预约
//                                            ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "预约书柜已过期，请重新预约！", Toast.LENGTH_SHORT);
//                                            linearPass.setVisibility(View.GONE);
//                                            linearCase.setVisibility(View.VISIBLE);
//                                        }
//                                    }
//                                });
                            } else if (code == 521) {//没有密码
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "预约书柜已过期，请重新预约！", Toast.LENGTH_SHORT);
                                        linearPass.setVisibility(View.GONE);
                                        linearCase.setVisibility(View.VISIBLE);
                                    }
                                });

                            } else if (code == 516) {//没有密码
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(orderCode.equals(Contants.ORDER_CODE_OUT)){//借书
                                            ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "此书柜已过期", Toast.LENGTH_SHORT);
                                            linearPass.setVisibility(View.GONE);
                                            linearCase.setVisibility(View.GONE);
                                        }else if(orderCode.equals(Contants.ORDER_CODE_IN)){//还书
                                            ToastUtils.showToastInCenter(OrderDetailActivity.this, 1, "预约书柜已失效，请重新预约！", Toast.LENGTH_SHORT);
                                            linearPass.setVisibility(View.GONE);
                                            linearCase.setVisibility(View.VISIBLE);
                                        }


                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @OnClick(R.id.od_btn_case)
    public void orderCase(View view) {
        Intent intent = new Intent(this, AgainCaseActivity.class);
        intent.putExtra("order_Id", rpId);
        intent.putExtra("order_code", orderCode);
        startActivityForResult(intent, Contants.REQUEST_ORDER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Contants.REQUEST_ORDER_CODE){
            if(resultCode == Contants.RESULT_ORDER_CODE){
                initData();
            }

        }
    }
}
