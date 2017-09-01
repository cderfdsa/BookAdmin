package com.example.bookadmin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.adapter.SecondGridViewAdapter;
import com.example.bookadmin.adapter.TopicGridViewAdapter;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.BookResult;
import com.example.bookadmin.bean.DateBean;
import com.example.bookadmin.bean.FirstOrapin;
import com.example.bookadmin.bean.LtTime;
import com.example.bookadmin.bean.Orapiin;
import com.example.bookadmin.bean.Orapiout;
import com.example.bookadmin.bean.PassDetail;
import com.example.bookadmin.bean.ShopInBook;
import com.example.bookadmin.bean.ShoppingCart;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.fragment.CartFragment;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.requrest.CaurserBiz;
import com.example.bookadmin.requrest.GetCasepass;
import com.example.bookadmin.requrest.OrapinFBiz;
import com.example.bookadmin.requrest.OrapioutBiz;
import com.example.bookadmin.tools.CartProvider;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.PreferencesUtils;
import com.example.bookadmin.tools.utils.TimeUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.CustomDialog;
import com.example.bookadmin.widget.RecyclerScrollview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-06-13.
 */

public class LibActivity extends IMBaseActivity implements View.OnClickListener {


    private int state;
    private List<ShoppingCart> carts;
    private List<ShopInBook> stillShopInBooks;

    private RecyclerScrollview scrollLayout;
    private Toolbar toolbar;
    private TextView gsName, gsTime, tvToborrow, tvStaystill, txtTotal, txtParticulars;
    private Button btnOrder;
    private GridView gvBorrow, gvStill;
    private LinearLayout llBorrow, llStill;
    private RelativeLayout reParticulars, locationLayout, clockLayout;

    private TopicGridViewAdapter topicGridViewAdapter;
    private SecondGridViewAdapter stillGridViewAdapter;

    private ApiAddress mApiAddress;
    private DateBean mDateBean;
    private LtTime mLtTime;

    private Orapiout mOrapiout;
    private List<Orapiin> mOrapiins;

    private long chooseTime = 0;
    private StringBuilder stringBuilder;
    private SpotsDialog spotsDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);

        state = getIntent().getIntExtra("JH_STATIC", -1);
        carts = (List<ShoppingCart>) getIntent().getSerializableExtra("carts");
        stillShopInBooks = (List<ShopInBook>) getIntent().getSerializableExtra("stillShopInBooks");

        initView();
        setToolBarReplaceActionBar();
        stringBuilder = new StringBuilder();
        spotsDialog = new SpotsDialog(LibActivity.this, "拼命加载中...");

        if (carts != null && carts.size() > 0) {
            setTodoAdapter();
            cycleTodo();
        }
        if (stillShopInBooks != null && stillShopInBooks.size() > 0) {
            setStayAdapter();
            cycleStay();
        }

    }


    private void initView() {
        Contants.griditemwidth = (int) ((int) Contants.displayHeight / 5 * Contants.aspectRatio);
        scrollLayout = (RecyclerScrollview) findViewById(R.id.id_scroll_layout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        gsName = (TextView) findViewById(R.id.gs_name);
        gsTime = (TextView) findViewById(R.id.gs_time);
        txtParticulars = (TextView) findViewById(R.id.txt_particulars);
        tvToborrow = (TextView) findViewById(R.id.tv_toborrow);
        tvStaystill = (TextView) findViewById(R.id.tv_staystill);
        gvBorrow = (GridView) findViewById(R.id.grid_borrow);
        gvStill = (GridView) findViewById(R.id.grid_still);
        llBorrow = (LinearLayout) findViewById(R.id.id_ll_borrow);
        llBorrow.setVisibility(View.GONE);
        llStill = (LinearLayout) findViewById(R.id.id_ll_still);
        llStill.setVisibility(View.GONE);
        reParticulars = (RelativeLayout) findViewById(R.id.re_particulars);
        reParticulars.setVisibility(View.GONE);
        locationLayout = (RelativeLayout) findViewById(R.id.location_layout);
        locationLayout.setOnClickListener(this);
        clockLayout = (RelativeLayout) findViewById(R.id.clock_layout);
        clockLayout.setOnClickListener(this);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        btnOrder = (Button) findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(this);
    }

    private void setToolBarReplaceActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog.Builder builder = new CustomDialog.Builder(LibActivity.this);
                builder.setMessage("确定要离开吗");
                builder.setPositiveButton("我在想想", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("去意已决", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void setTodoAdapter() {
        int size = carts.size();
        if (size > 0) {
            tvToborrow.setText(getString(R.string.toBorrow) + carts.size() + getString(R.string.capital));
            llBorrow.setVisibility(View.VISIBLE);
        } else {
            tvToborrow.setText("");
            llBorrow.setVisibility(View.GONE);
        }
        int gridviewWidth = (Contants.griditemwidth + Contants.griditempadding) * size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gvBorrow.setLayoutParams(params); // 重点
        gvBorrow.setColumnWidth(Contants.griditemwidth); // 重点
        gvBorrow.setHorizontalSpacing(Contants.griditempadding); // 间距
        gvBorrow.setStretchMode(GridView.NO_STRETCH);
        gvBorrow.setNumColumns(size); // 重点
        gvBorrow.clearDisappearingChildren(); //删除屏幕上残存动画
        topicGridViewAdapter = new TopicGridViewAdapter(LibActivity.this, carts, false);
//        topicGridViewAdapter.setOnImdelListener(new TopicGridViewAdapter.OnImdelListener() {
//            @Override
//            public void onImClick(View v, final int position) {
//                CustomDialog.Builder builder = new CustomDialog.Builder(LibActivity.this);
//                builder.setMessage("确定要删除这1本图书吗？");
//                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        ShoppingCart cart = shoppingCartList.get(position);
//                        shoppingCartList.remove(position);
//                        cartProvider.delete(cart);
//                        dialog.dismiss();
//                        if (shoppingCartList.size() > 0) {
//                            setTodoAdapter(shoppingCartList, true);
//                        } else {
//                            setTodoAdapter(shoppingCartList, false);
//                        }
//                    }
//                });
//                builder.create().show();
//            }
//        });
        gvBorrow.setAdapter(topicGridViewAdapter);
//        gvBorrow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                // TODO Auto-generated method stub
//                if (topicGridViewAdapter != null) {
//                    topicGridViewAdapter.setIsShowDelete(true);
//                    //显示删除图标
//                    if (onCartDelShowListener != null) {
//                        onCartDelShowListener.onCartDelShow(true);
//                    }
//                    tvgoborrow.setText("点击取消");
//                }
//                return true;
//            }
//        });
//        gvBorrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                goBorrowDeldismiss();
//            }
//        });
    }


    /**
     * 设置book的adapter
     */
    private void setStayAdapter() {
        int size = stillShopInBooks.size();
        if (size > 0) {
            tvStaystill.setText(getString(R.string.stayStill) + stillShopInBooks.size() + getString(R.string.capital));
            llStill.setVisibility(View.VISIBLE);
        } else {
            tvStaystill.setText("");
            llStill.setVisibility(View.GONE);
        }
        int gridviewWidth = (Contants.griditemwidth + Contants.griditempadding) * size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gvStill.setLayoutParams(params); // 重点
        gvStill.setColumnWidth(Contants.griditemwidth); // 重点
        gvStill.setHorizontalSpacing(Contants.griditempadding); // 间距
        gvStill.setStretchMode(GridView.NO_STRETCH);
        gvStill.setNumColumns(size); // 重点
        gvStill.clearDisappearingChildren(); //删除屏幕上残存动画
        stillGridViewAdapter = new SecondGridViewAdapter(LibActivity.this, stillShopInBooks);
        gvStill.setAdapter(stillGridViewAdapter);
//        gvStill.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                ShopInBook shopInBook = shopInBookList.get(position);
//                shopInBook.setState(ShopInBook.STATE_BORROW);
//
//                List<ShopInBook> shopInBooks = new ArrayList<ShopInBook>();
//                shopInBooks.addAll(renewShopInBooks);
//                shopInBooks.add(shopInBook);
//                setRenewAdapter(shopInBooks);
//
//                shopInBookList.remove(position);
//                setStayAdapter(shopInBookList);
//                return true;
//            }
//        });
    }

    private void cycleTodo() {
        if (carts != null && carts.size() > 0) {
            float totalPrice = 0;
            for (ShoppingCart cart : carts) {
                float price = cart.getBs_price();
                totalPrice = totalPrice + price;
            }
            txtTotalappend(String.valueOf(totalPrice) + " 元 ");
        }
    }

    private void cycleStay() {
        if (stillShopInBooks != null && stillShopInBooks.size() > 0) {
            for (ShopInBook inBook : stillShopInBooks) {
                inBook.getInBookBean().getBs_price();
            }
            txtTotalappend("还书" + stillShopInBooks.size() + "本 ");
        }
    }

    private void txtTotalappend(String txt) {
        txtTotal.setText(txtTotal.getText() + " " + txt);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            CustomDialog.Builder builder = new CustomDialog.Builder(LibActivity.this);
            builder.setMessage("确定要离开吗");
            builder.setPositiveButton("我在想想", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("去意已决", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_layout:
                Intent intentMap = new Intent(LibActivity.this, MapActivity.class);
                startActivityForResult(intentMap, Contants.REQUEST_MAP);
                break;
            case R.id.clock_layout:
                if (mApiAddress == null) {
                    ToastUtils.showToastInCenter(LibActivity.this, 1, "请先选择书柜地址", Toast.LENGTH_SHORT);
                    return;
                }
                if (state == CartFragment.HAVE_BORROW_HAVR_ALSO || state == CartFragment.HAVE_BORROW_NOT_ALSO) {
                    startClockActivity(false);
                } else if (state == CartFragment.NOT_BORROW_HAVR_ALSO) {
                    startClockActivity(true);
                }
                break;
            case R.id.btn_order:
                if (System.currentTimeMillis() - chooseTime < 2000) {
                    chooseTime = System.currentTimeMillis();
                } else {
                    if (mApiAddress == null) {
                        ToastUtils.showToastInCenter(LibActivity.this, 1, getString(R.string.filladdress), Toast.LENGTH_SHORT);
                        return;
                    }
                    if (mDateBean == null || mLtTime == null) {
                        ToastUtils.showToastInCenter(LibActivity.this, 1, "请选择日期与时间", Toast.LENGTH_SHORT);
                        return;
                    }
                    determine();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Contants.REQUEST_MAP:
                if (resultCode == Contants.MAP_RESULTCODE) {
                    ApiAddress apiAddress = (ApiAddress) data.getSerializableExtra(Contants.MAP_ADDRESS);
                    mApiAddress = apiAddress;
                    gsName.setText(apiAddress.getGs_name());
                    mDateBean = null;
                    mLtTime = null;
                    gsTime.setText("");
                }
                break;
            case Contants.REQUEST_CLOCK:
                if (resultCode == Contants.CLOCK_RESULTCODE) {
                    mDateBean = (DateBean) data.getSerializableExtra(Contants.CLOCK_DATE);
                    mLtTime = (LtTime) data.getSerializableExtra(Contants.CLOCK_TIME);
                    gsTime.setText(mDateBean.getStrDate() + "  " + mLtTime.getLt_starttime() + "---" + mLtTime.getLt_endtime());
                    reParticulars.setVisibility(View.VISIBLE);
                    txtParticulars.setText("注意：\n        书柜的使用时间为" + mDateBean.getStrDate() + "  " +
                            mLtTime.getLt_starttime() + " - " + mLtTime.getLt_endtime() + "\n        书柜预约时间未到或预约时间过期，书柜不能使用" +
                            "\n        书柜预约成功后可到我的订单里面查看书柜密码");
                }
                break;
        }
    }

    private void determine() {
        spotsDialog.show();
        switch (state) {
            case CartFragment.HAVE_BORROW_HAVR_ALSO:
                alsoBook();
                break;
            case CartFragment.NOT_BORROW_HAVR_ALSO:
                borrBook();
                break;
            case CartFragment.HAVE_BORROW_NOT_ALSO://no
                alsoBook();
                break;
        }
    }


    private void startClockActivity(boolean isAlso) {
        Intent intentClock = new Intent(LibActivity.this, ClockActivity.class);
        intentClock.putExtra("gs_id", mApiAddress.getGs_id());
        intentClock.putExtra("isAlso", isAlso);
        startActivityForResult(intentClock, Contants.REQUEST_CLOCK);
    }


    private void alsoBook() {
        OrapioutBiz.requestOrapiout(LibActivity.this, carts, mApiAddress, new OrapioutBiz.RequstOrapioutResult() {
            @Override
            public void requstOrapioutResult(Orapiout orapiout) {
                mOrapiout = orapiout;
                stringBuilder.append("\n" + carts.size() + " 本图书借阅成功");
                if (state == CartFragment.HAVE_BORROW_HAVR_ALSO) {
                    borrBook();
                } else {
                    boooCase();
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        CartProvider cartProvider = new CartProvider(LibActivity.this);
                        for (ShoppingCart shoppingCart : carts) {
                            cartProvider.delete(shoppingCart);
                        }
                    }
                }.start();

            }

            @Override
            public void requstOrapioutError() {
                stringBuilder.append("\n " + carts.size() + " 本图书借阅失败");
                updateStillFragment();
            }
        });

    }

    private void borrBook() {
        OrapinFBiz orapinFBiz = new OrapinFBiz(LibActivity.this, stillShopInBooks, mApiAddress);
        orapinFBiz.requestOrapin(new OrapinFBiz.RequestOrapiinResult() {
            @Override
            public void requstOrapiinResult(List<Orapiin> orapiins, List<FirstOrapin> firstOrapins) {
                mOrapiins = orapiins;
                stringBuilder.append("\n");

                StringBuilder showName = new StringBuilder();
                showName.append("如有包装");
                for (int i = 0; i < firstOrapins.size(); i++) {
                    FirstOrapin firstOrapin = firstOrapins.get(i);
                    String name = firstOrapin.getPack().getPackName();
                    if (!name.equals("-1")) {
                        if (i == firstOrapins.size() - 1) {
                            showName.append(name);
                        } else {
                            showName.append(name + "、");
                        }
                    }else{
                        if(showName.substring(showName.length() - 1, showName.length()).equals("、")){
                            showName.delete(showName.length() - 1, showName.length());
                        }
                    }
                }
                showName.append("未还，请将包装一起放到书柜中");
                stringBuilder.append(showName.toString());

                boooCase();
            }

            @Override
            public void requstOrapiinResultError(List<ShopInBook> shopInBooks) {
                boooCase();
            }

            @Override
            public void requstNodata() {
                stringBuilder.append("\n图书下单成功");
                updateStillFragment();
            }
        });
    }

    private void boooCase() {
        CaurserBiz.requsetCaurser(LibActivity.this, mDateBean, mLtTime, String.valueOf(mApiAddress.getGs_id()), mOrapiins, mOrapiout,
                new CaurserBiz.RequstCaurserResult() {
                    @Override
                    public void requstCaurserResult() {
                        stringBuilder.append("\n书柜预约成功");
                        PreferencesUtils.putString(LibActivity.this, Contants.APIADDRESS_JSON, GsonUtil.GsonString(mApiAddress));

                        getPass();
                    }

                    @Override
                    public void requstCaurserResultError(int code) {
                        if (code == 546) {
                            ToastUtils.showToastInCenter(LibActivity.this, 1, "此书柜全被占用，请选择其他地址的书柜", Toast.LENGTH_SHORT);
                            if (spotsDialog != null && spotsDialog.isShowing()) {
                                spotsDialog.dismiss();
                            }
                        } else {
                            updateStillFragment();
                        }
                    }
                });
    }

    private void getPass(){//还书 Contants.ORDER_CODE_IN
//        mOrapiins, mOrapiout
        String rpId = "";
        String orderCode = "";
        switch (state) {
            case CartFragment.HAVE_BORROW_HAVR_ALSO:
                if(mOrapiout != null){
                    rpId = mOrapiout.getRp_id();
                    orderCode = Contants.ORDER_CODE_OUT;
                }
                break;
            case CartFragment.NOT_BORROW_HAVR_ALSO:
                if(mOrapiins != null && mOrapiins.size() > 0){
                    rpId = mOrapiins.get(0).getRp_id();
                    orderCode = Contants.ORDER_CODE_IN;
                }
                break;
            case CartFragment.HAVE_BORROW_NOT_ALSO://no
                if(mOrapiout != null){
                    rpId = mOrapiout.getRp_id();
                    orderCode = Contants.ORDER_CODE_OUT;
                }
                break;
        }
        GetCasepass.getPassWord(this, rpId, orderCode, new GetCasepass.IGetCaseListener() {
            @Override
            public void getPassSuccess(PassDetail passDetail, List<BookResult> bookResults) {
                final String cuDate = passDetail.getCu_date();
                final String startTime = passDetail.getLt_starttime();
                final String endTime = passDetail.getLt_endtime();
                stringBuilder.append("\n书柜预约日期为" + cuDate);
                stringBuilder.append("\n书柜预约时间为" + startTime + " --- " + endTime);
                stringBuilder.append("\n书柜预约地址为" + passDetail.getBx_name());
                stringBuilder.append("\n书柜预约密码为" + passDetail.getCu_password());

                updateStillFragment();
            }

            @Override
            public void getPassOverdue() {
                stringBuilder.append("\n书柜密码已过期");
                updateStillFragment();
            }

            @Override
            public void getPassFailure() {
                stringBuilder.append("\n书柜密码获取已失败");
                updateStillFragment();
            }
        });
    }

    private void updateStillFragment() {
        if (spotsDialog != null && spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        Intent intent = new Intent();
        intent.putExtra("sb", stringBuilder.toString());
        setResult(Contants.LIB_RESULTCODE, intent);
        finish();
    }


}
