package com.example.bookadmin.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.ClockActivity;
import com.example.bookadmin.activity.logic.LoginActivity;
import com.example.bookadmin.activity.MainActivity;
import com.example.bookadmin.activity.MapActivity;
import com.example.bookadmin.activity.me.MyOrderActivity;
import com.example.bookadmin.adapter.SecondGridViewAdapter;
import com.example.bookadmin.adapter.TopicGridViewAdapter;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.DateBean;
import com.example.bookadmin.bean.FirstOrapin;
import com.example.bookadmin.bean.InBookBean;
import com.example.bookadmin.bean.LtTime;
import com.example.bookadmin.bean.Orapiin;
import com.example.bookadmin.bean.Orapiout;
import com.example.bookadmin.bean.ShopInBook;
import com.example.bookadmin.bean.ShoppingCart;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.requrest.CaurserBiz;
import com.example.bookadmin.requrest.InBookBiz;
import com.example.bookadmin.interf.InbookRequestCallback;
import com.example.bookadmin.requrest.OrapinFBiz;
import com.example.bookadmin.requrest.OrapioutBiz;
import com.example.bookadmin.requrest.ValidationBiz;
import com.example.bookadmin.tools.CartProvider;
import com.example.bookadmin.tools.utils.DensityUtil;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.PreferencesUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.CustomDialog;
import com.example.bookadmin.widget.RecyclerScrollview;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2017-06-05.
 */

public class BorrowFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerScrollview scrollLayout;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private TextView btnRight, gsName, gsTime, tvToborrow, tvgoborrow, tvStaystill, tvGostill, tvTorenew, tvGorenew, txtParticulars;
    private Button btnborrow;
    private GridView gvBorrow, gvStill, gvRenew;
    private LinearLayout llBorrow, llStill, llRenew;
    private RelativeLayout reParticulars, locationLayout, clockLayout;

    private CartProvider cartProvider;

    private List<ShoppingCart> carts;
    private List<ShopInBook> stillShopInBooks;
    private List<ShopInBook> renewShopInBooks;

    private TopicGridViewAdapter topicGridViewAdapter;
    private SecondGridViewAdapter stillGridViewAdapter;
    private SecondGridViewAdapter renewGridViewAdapter;

    private OnDelShowListener onDelShowListener;

    private ApiAddress mApiAddress;
    private DateBean mDateBean;
    private LtTime mLtTime;

    public static int JH_STATIC = 1;
    public static final int HAVE_BORROW_HAVR_ALSO = 1;//有借有还
    public static final int NOT_BORROW_HAVR_ALSO = 2;//不借只还
    public static final int HAVE_BORROW_NOT_ALSO = 3;//只借不还
    public static final int NOT_BORROW_NOT_ALSO = 4;//

    private Orapiout mOrapiout;//借书后返回订单
    private List<Orapiin> mOrapiins;

    private StringBuilder stringBuilder;

    private SpotsDialog spotsDialog;

    private long chooseTime = 0;

    public interface OnDelShowListener {
        void onDelShow(boolean isShow);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onDelShowListener = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);
        initView(view);
        stringBuilder = new StringBuilder();
        spotsDialog = new SpotsDialog(getActivity(), "拼命加载中...");
        return view;
    }

    private void initView(View view) {
        scrollLayout = (RecyclerScrollview) view.findViewById(R.id.id_scroll_layout);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.id_relative_layout);
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        btnRight = (TextView) view.findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
        gsName = (TextView) view.findViewById(R.id.gs_name);
        gsTime = (TextView) view.findViewById(R.id.gs_time);
        txtParticulars = (TextView) view.findViewById(R.id.txt_particulars);
        tvToborrow = (TextView) view.findViewById(R.id.tv_toborrow);
        tvgoborrow = (TextView) view.findViewById(R.id.tv_goborrow);
        tvgoborrow.setOnClickListener(this);
        tvStaystill = (TextView) view.findViewById(R.id.tv_staystill);
        tvGostill = (TextView) view.findViewById(R.id.tv_gostill);
        tvTorenew = (TextView) view.findViewById(R.id.tv_torenew);
        tvGorenew = (TextView) view.findViewById(R.id.tv_gorenew);
        gvBorrow = (GridView) view.findViewById(R.id.grid_borrow);
        gvStill = (GridView) view.findViewById(R.id.grid_still);
        gvRenew = (GridView) view.findViewById(R.id.grid_renew);
        llBorrow = (LinearLayout) view.findViewById(R.id.id_ll_borrow);
        llStill = (LinearLayout) view.findViewById(R.id.id_ll_still);
        llRenew = (LinearLayout) view.findViewById(R.id.id_ll_renew);
        reParticulars = (RelativeLayout) view.findViewById(R.id.re_particulars);
        reParticulars.setVisibility(View.GONE);
        locationLayout = (RelativeLayout) view.findViewById(R.id.location_layout);
        locationLayout.setOnClickListener(this);
        clockLayout = (RelativeLayout) view.findViewById(R.id.clock_layout);
        clockLayout.setOnClickListener(this);
        btnborrow = (Button) view.findViewById(R.id.btn_borrow);
        btnborrow.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadData();
    }

    public void loadData() {

        if (UserInfo.getInstance().getId() == null) {
            startLoginActivity();
            return;
        }
        cartProvider = new CartProvider(getActivity());

//        UserEntity userEntity = BookApplication.getInstance().getUserEntity();
//        if (userEntity != null) {
//            if (user.getUserId() != null) {
        List<ShoppingCart> shoppingCartList = cartProvider.getAll();
        loadShoppingCart(shoppingCartList);
        InBookBiz.loadInBook(getActivity(), new InbookRequestCallback() {
            @Override
            public void handleInbookQueryResult(List<InBookBean> stayInBookBeens, List<InBookBean> contInBookBeens) {
                List<ShopInBook> shopInBookList = changeBookToShop(stayInBookBeens);
                setStayAdapter(shopInBookList);
            }

            @Override
            public void noInbookQueryResult() {
                List<ShopInBook> shopInBookList = new ArrayList<ShopInBook>();
                setStayAdapter(shopInBookList);
            }
        });
//            }
//        } else {
//            setNull();
//        }
        List<ShopInBook> shopInBooks = new ArrayList<>();
        setRenewAdapter(shopInBooks);

        reParticulars.setVisibility(View.GONE);
        mApiAddress = null;
        mDateBean = null;
        mLtTime = null;
        gsName.setText("");
        gsTime.setText("");

        initState();
        setState();
    }

    @Override
    public void init() {
        loadData();
    }

    public void startLoginActivity() {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessage("您未登录账号，登录后可借还图书");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                scrollLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("登录", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, Contants.REQUEST_LOGIN);
                dialog.dismiss();
            }
        });
        builder.create().show();
//        ToastUtils.showToastInCenter(getActivity(), 1, "登录后借还图书", Toast.LENGTH_SHORT);
    }

    private void setNull() {
        llStill.setVisibility(View.GONE);
        llRenew.setVisibility(View.GONE);
        stillShopInBooks = null;
        renewShopInBooks = null;

        stillGridViewAdapter = null;
        renewGridViewAdapter = null;
    }

//    public void refData(){
//        cartProvider = new CartProvider(getActivity());
//
//        List<ShoppingCart> shoppingCartList = cartProvider.getAll();
//        loadShoppingCart(shoppingCartList);
//
//        UserEntity userEntity = BookApplication.getInstance().getUserEntity();
//        if (userEntity != null) {
//            if (userEntity.getUserId() != null) {
//                InBookBiz.loadInBook(getActivity(), new InbookRequestCallback() {
//                    @Override
//                    public void handleInbookQueryResult(List<InBookBean> stayInBookBeens, List<InBookBean> contInBookBeens) {
//                        List<ShopInBook> shopInBookList = new ArrayList<ShopInBook>();
//                        if(renewShopInBooks.size() > 0){
//                            List<InBookBean> inBooks = new ArrayList<InBookBean>();
//                            for(ShopInBook shopInBook : renewShopInBooks){
//                                inBooks.add(shopInBook.getInBookBean());
//                            }
//                            stayInBookBeens.removeAll(inBooks);
//
//                            shopInBookList = changeBookToShop(stayInBookBeens);
//
//                        } else{
//                            List<ShopInBook> shopInBooks = new ArrayList<>();
//                            setRenewAdapter(shopInBooks);
//                            shopInBookList = changeBookToShop(stayInBookBeens);
//                        }
//                        setStayAdapter(shopInBookList);
//                    }
//
//                    @Override
//                    public void noInbookQueryResult() {
//                        List<ShopInBook> shopInBookList = new ArrayList<ShopInBook>();
//                        setStayAdapter(shopInBookList);
//                    }
//                });
//            }
//        }else{
//            setNull();
//        }
//
//        reParticulars.setVisibility(View.GONE);
//        mApiAddress = null;
//        mDateBean = null;
//        mLtTime = null;
//        gsName.setText("");
//        bsDate.setText("");
//        bsTime.setText("");
//    }

    /**
     * 将InBookBean的集合转变为ShopInBook集合，添加续借与待还状态
     *
     * @param stayInBookBeens
     * @return
     */
    @NonNull
    private List<ShopInBook> changeBookToShop(List<InBookBean> stayInBookBeens) {
        List<ShopInBook> shopInBookList = new ArrayList<ShopInBook>();
        for (int i = 0; i < stayInBookBeens.size(); i++) {
            InBookBean inBookBean = stayInBookBeens.get(i);
            ShopInBook shopInBook = new ShopInBook();
            shopInBook.setState(ShopInBook.STATE_STILL);
            shopInBook.setInBookBean(inBookBean);
            shopInBookList.add(shopInBook);
        }
        return shopInBookList;
    }


    /**
     * 判断购物车中物品是否已经失效，失效直接删除，显示没有失效的
     *
     * @param shoppingCartList
     */
    private void loadShoppingCart(List<ShoppingCart> shoppingCartList) {
        if (shoppingCartList != null) {
            ValidationBiz validationBiz = new ValidationBiz(getActivity());
            validationBiz.validationCart(shoppingCartList, new ValidationBiz.ValidationRequestShopping() {
                @Override
                public void shoppingValidationSuccess(List<ShoppingCart> carts) {
                    setTodoAdapter(carts, false);
                }

                @Override
                public void shoppingValidationError(List<ShoppingCart> carts) {
                    setTodoAdapter(carts, false);
                }
            });
        } else {
            shoppingCartList = new ArrayList<>();
            setTodoAdapter(shoppingCartList, false);
        }
    }

    /**
     * 设置购物车的adapter
     *
     * @param shoppingCartList
     */
    private void setTodoAdapter(final List<ShoppingCart> shoppingCartList, boolean isShowDelete) {
        carts = shoppingCartList;
        int size = shoppingCartList.size();
        if (size > 0) {
            tvToborrow.setText(getString(R.string.toBorrow) + shoppingCartList.size() + getString(R.string.capital));
            if (isShowDelete) {
                tvgoborrow.setText("点击取消");
            } else {
                tvgoborrow.setText(getString(R.string.longPressRemove));
            }
            llBorrow.setVisibility(View.VISIBLE);
        } else {
            tvToborrow.setText("");
            tvgoborrow.setText("");
            llBorrow.setVisibility(View.GONE);
        }
        int length = DensityUtil.dip2px(getActivity(), 114);
        int gridviewWidth = (length + 50) * size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gvBorrow.setLayoutParams(params); // 重点
        gvBorrow.setColumnWidth(length); // 重点
        gvBorrow.setHorizontalSpacing(50); // 间距
        gvBorrow.setStretchMode(GridView.NO_STRETCH);
        gvBorrow.setNumColumns(size); // 重点
        gvBorrow.clearDisappearingChildren(); //删除屏幕上残存动画
        topicGridViewAdapter = new TopicGridViewAdapter(getActivity(), shoppingCartList, isShowDelete);
        topicGridViewAdapter.setOnImdelListener(new TopicGridViewAdapter.OnImdelListener() {
            @Override
            public void onImClick(View v, final int position) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("确定要删除这1本图书吗？");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ShoppingCart cart = shoppingCartList.get(position);
                        shoppingCartList.remove(position);
                        cartProvider.delete(cart);
                        dialog.dismiss();
                        if (shoppingCartList.size() > 0) {
                            setTodoAdapter(shoppingCartList, true);
                        } else {
                            setTodoAdapter(shoppingCartList, false);
                        }
                    }
                });
                builder.create().show();
            }
        });
        gvBorrow.setAdapter(topicGridViewAdapter);
        gvBorrow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                if (topicGridViewAdapter != null) {
                    topicGridViewAdapter.setIsShowDelete(true);
                    //显示删除图标
                    if (onDelShowListener != null) {
                        onDelShowListener.onDelShow(true);
                    }
                    tvgoborrow.setText("点击取消");
                }
                return true;
            }
        });
        gvBorrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goBorrowDeldismiss();
            }
        });
        initState();
        setState();
    }


    /**
     * 设置book的adapter
     *
     * @param shopInBookList
     */
    private void setStayAdapter(final List<ShopInBook> shopInBookList) {
        stillShopInBooks = shopInBookList;
        int size = shopInBookList.size();
        if (size > 0) {
            tvStaystill.setText(getString(R.string.stayStill) + shopInBookList.size() + getString(R.string.capital));
            tvGostill.setText(getString(R.string.clickRenew));
            llStill.setVisibility(View.VISIBLE);
        } else {
            tvStaystill.setText("");
            tvGostill.setText("");
            llStill.setVisibility(View.GONE);
        }
        int length = DensityUtil.dip2px(getActivity(), 114);
        int gridviewWidth = (length + 50) * size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gvStill.setLayoutParams(params); // 重点
        gvStill.setColumnWidth(length); // 重点
        gvStill.setHorizontalSpacing(50); // 间距
        gvStill.setStretchMode(GridView.NO_STRETCH);
        gvStill.setNumColumns(size); // 重点
        gvStill.clearDisappearingChildren(); //删除屏幕上残存动画
        stillGridViewAdapter = new SecondGridViewAdapter(getActivity(), shopInBookList);
        gvStill.setAdapter(stillGridViewAdapter);
        gvStill.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                ShopInBook shopInBook = shopInBookList.get(position);
                shopInBook.setState(ShopInBook.STATE_BORROW);

                List<ShopInBook> shopInBooks = new ArrayList<ShopInBook>();
                shopInBooks.addAll(renewShopInBooks);
                shopInBooks.add(shopInBook);
                setRenewAdapter(shopInBooks);

                shopInBookList.remove(position);
                setStayAdapter(shopInBookList);
                return true;
            }
        });
        initState();
        setState();
    }

    private void setRenewAdapter(final List<ShopInBook> shopInBooks) {
        renewShopInBooks = shopInBooks;
        int size = shopInBooks.size();
        if (size > 0) {
            tvTorenew.setText(getString(R.string.stayRenew) + shopInBooks.size() + getString(R.string.capital));
            tvGorenew.setText(getString(R.string.clickStill));
            llRenew.setVisibility(View.VISIBLE);
        } else {
            tvTorenew.setText("");
            tvGorenew.setText("");
            llRenew.setVisibility(View.GONE);
        }
        int length = DensityUtil.dip2px(getActivity(), 114);
        int gridviewWidth = (length + 50) * size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gvRenew.setLayoutParams(params); // 重点
        gvRenew.setColumnWidth(length); // 重点
        gvRenew.setHorizontalSpacing(50); // 间距
        gvRenew.setStretchMode(GridView.NO_STRETCH);
        gvRenew.setNumColumns(size); // 重点
        gvRenew.clearDisappearingChildren(); //删除屏幕上残存动画
        renewGridViewAdapter = new SecondGridViewAdapter(getActivity(), shopInBooks);
        gvRenew.setAdapter(renewGridViewAdapter);
        gvRenew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                ShopInBook shopInBook = shopInBooks.get(position);
                shopInBook.setState(ShopInBook.STATE_STILL);

                List<ShopInBook> shopInBookList = new ArrayList<ShopInBook>();
                shopInBookList.addAll(stillShopInBooks);
                shopInBookList.add(shopInBook);
                setStayAdapter(shopInBookList);

                shopInBooks.remove(position);
                setRenewAdapter(shopInBooks);
                return true;
            }
        });
    }

    private void goBorrowDeldismiss() {
        if (tvgoborrow.getText().equals("点击取消")) {
            topicGridViewAdapter.setIsShowDelete(false);
            tvgoborrow.setText("长按去除");
        }
    }

    /**
     * mainActivity调用
     */
    public void refShow() {
        if (topicGridViewAdapter != null) {
            topicGridViewAdapter.setIsShowDelete(false);
        }
        tvgoborrow.setText("长按去除");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_borrow:
                ((MainActivity) getActivity()).setCurrentPage(0);
                break;
            case R.id.tv_goborrow:
                if (JH_STATIC != NOT_BORROW_NOT_ALSO) {
                    goBorrowDeldismiss();
                } else {
                    ToastUtils.showToastInCenter(getActivity(), 1, "没有借还的图书", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.location_layout:
                initState();
                if (JH_STATIC == NOT_BORROW_NOT_ALSO) {
                    ToastUtils.showToastInCenter(getActivity(), 1, "没有借还的图书", Toast.LENGTH_SHORT);
                    return;
                }
                Intent intentMap = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(intentMap, Contants.REQUEST_MAP);
                break;
            case R.id.clock_layout:
                if (mApiAddress == null) {
                    ToastUtils.showToastInCenter(getActivity(), 1, "请先选择书柜地址", Toast.LENGTH_SHORT);
                    return;
                }
                initState();
                if (JH_STATIC == NOT_BORROW_NOT_ALSO) {
                    ToastUtils.showToastInCenter(getActivity(), 1, "没有借还的图书", Toast.LENGTH_SHORT);
                    return;
                }
                if (JH_STATIC == HAVE_BORROW_HAVR_ALSO || JH_STATIC == HAVE_BORROW_NOT_ALSO) {
                    startClockActivity(false);
                } else if (JH_STATIC == NOT_BORROW_HAVR_ALSO) {
                    startClockActivity(true);
                }
                break;
            case R.id.btnRight:
//                UserEntity userEntity = BookApplication.getInstance().getUserEntity();
//                if (userEntity != null) {
//                    if (userEntity.getUserId() != null) {
                if (JH_STATIC == NOT_BORROW_NOT_ALSO) {
                    ToastUtils.showToastInCenter(getActivity(), 1, "没有借阅书", Toast.LENGTH_SHORT);
                    return;
                }
                if (System.currentTimeMillis() - chooseTime < 2000) {
                    chooseTime = System.currentTimeMillis();
                } else {
                    if (mApiAddress == null) {
                        ToastUtils.showToastInCenter(getActivity(), 1, getString(R.string.filladdress), Toast.LENGTH_SHORT);
                        return;
                    }
                    if (mDateBean == null || mLtTime == null) {
                        ToastUtils.showToastInCenter(getActivity(), 1, "请选择日期与时间", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (JH_STATIC == HAVE_BORROW_NOT_ALSO || JH_STATIC == HAVE_BORROW_HAVR_ALSO) {
                        detectionCart();
                    } else {
                        determine();
                    }
                }
//                    }
//                } else {
//
//                }
                break;
        }
    }

    private void startClockActivity(boolean isAlso) {
        Intent intentClock = new Intent(getActivity(), ClockActivity.class);
        intentClock.putExtra("gs_id", mApiAddress.getGs_id());
        intentClock.putExtra("isAlso", isAlso);
        startActivityForResult(intentClock, Contants.REQUEST_CLOCK);
    }

    private void detectionCart() {
        ValidationBiz validationBiz = new ValidationBiz(getActivity());
        validationBiz.validationCart(carts, new ValidationBiz.ValidationRequestShopping() {
            @Override
            public void shoppingValidationSuccess(List<ShoppingCart> carts) {
                setTodoAdapter(carts, false);
                initState();
                determine();
            }

            @Override
            public void shoppingValidationError(List<ShoppingCart> carts) {
                setTodoAdapter(carts, false);
                if (carts.size() > 0) {
                    initState();
                    determine();
                } else {
                    ToastUtils.showToastInCenter(getActivity(), 1, "此图书失效", Toast.LENGTH_SHORT);
                }
            }
        });
    }

//    public void chooseTime(boolean isAlso) {
//        OrderGs.grogshopTime(getActivity(), mApiAddress.getGs_id(), mDateBean, isAlso, new OrderGs.OnGroupTimeListener() {
//            @Override
//            public void OnGroupTime(LtTime ltTime) {
//                mLtTime = ltTime;
//                bsTime.setText(ltTime.getLt_starttime() + "-" + ltTime.getLt_endtime());
//                reParticulars.setVisibility(View.VISIBLE);
//                txtParticulars.setText("注意：\n        书柜的使用时间为" + mDateBean.getStrDate() + "  " +
//                        ltTime.getLt_starttime() + " - " + ltTime.getLt_endtime() + "\n        书柜预约时间未到或预约时间过期，书柜不能使用" +
//                        "\n        书柜预约成功后可到我的订单里面查看书柜密码");
//            }
//
//            @Override
//            public void OnGropTimeNoTime() {
//                boolean isToday = TimeUtils.IsToday(mDateBean);
//                if (isToday) {
//                    ToastUtils.showToastInCenter(getActivity(), 1, "今天没有合适的时间段，请您另选择日期", Toast.LENGTH_SHORT);
//                } else {
//                    ToastUtils.showToastInCenter(getActivity(), 1, "此书柜预约已满，请您另选择书柜", Toast.LENGTH_SHORT);
//                }
//            }
//        });
//    }

//    public void chooseAdress() {
//        new AlertDialog.Builder(getActivity()).setTitle("")
//                .setSingleChoiceItems(address, 0, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        switch (which) {
//                            case 0:
//                                addNegative();
//                                break;
//                            case 1:
//                                addPositive();
//                                break;
//                        }
//                    }
//                }).setNegativeButton("", null)
//                .show();
//    }

    /**
     * 常用地址选择
     */
//    private void addNegative() {
//        OrderGs.common(getActivity(), new OrderGs.RequstDialogResult() {
//            @Override
//            public void requestDialogResult(ApiAddress apiAddress) {
//                mApiAddress = apiAddress;
//                gsName.setText(mApiAddress.getGs_name());
//                mDateBean = null;
//                bsDate.setText("");
//                mLtTime = null;
//                bsTime.setText("");
//            }
//
//            @Override
//            public void requestApiAddress(List<ApiAddress> apiAddresses) {
//
//            }
//        });
//    }

    /**
     * \高德地图选择地点
     */
//    private void addPositive() {
//        Intent intent = new Intent(getActivity(), MapActivity.class);
//        startActivityForResult(intent, Contants.REQUEST_MAP);
//    }
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
            case Contants.REQUEST_LOGIN:
                if (resultCode == Contants.LOGIN_RESULTCODE) {
                    loadData();
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

    private void initState() {
        if (carts != null && carts.size() > 0 && stillShopInBooks != null && stillShopInBooks.size() > 0) {//有书还
            JH_STATIC = HAVE_BORROW_HAVR_ALSO;
        } else if (carts != null && carts.size() > 0 && (stillShopInBooks == null || stillShopInBooks.size() == 0)) {
            JH_STATIC = HAVE_BORROW_NOT_ALSO;
        } else if ((carts == null || carts.size() == 0) && stillShopInBooks != null && stillShopInBooks.size() > 0) {
            JH_STATIC = NOT_BORROW_HAVR_ALSO;
        } else if ((carts == null || carts.size() == 0) && (stillShopInBooks == null || stillShopInBooks.size() == 0)) {
            JH_STATIC = NOT_BORROW_NOT_ALSO;
        }
    }

    private void setState() {
        if (JH_STATIC == NOT_BORROW_NOT_ALSO) {
            scrollLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            scrollLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
    }

    private void determine() {
        spotsDialog.show();
        switch (JH_STATIC) {
            case HAVE_BORROW_HAVR_ALSO:
                alsoBook();
                break;
            case NOT_BORROW_HAVR_ALSO:
                borrBook();
                break;
            case HAVE_BORROW_NOT_ALSO://no
                alsoBook();
                break;
            case NOT_BORROW_NOT_ALSO:
                ToastUtils.showToastInCenter(getActivity(), 1, "没有借还书", Toast.LENGTH_SHORT);
                break;
        }
    }

    /**
     * 借书
     */
    private void alsoBook() {
        OrapioutBiz.requestOrapiout(getActivity(), carts, mApiAddress, new OrapioutBiz.RequstOrapioutResult() {
            @Override
            public void requstOrapioutResult(Orapiout orapiout) {
                mOrapiout = orapiout;
                stringBuilder.append("\n " + carts.size() + " 本图书借阅成功");
                if (JH_STATIC == HAVE_BORROW_HAVR_ALSO) {
                    //借书成功后还书
                    borrBook();
                } else {
                    boooCase();
                }
            }

            @Override
            public void requstOrapioutError() {
                updateStillFragment();
            }
        });

    }

    private void borrBook() {
        //还书
        OrapinFBiz orapinFBiz = new OrapinFBiz(getActivity(), stillShopInBooks, mApiAddress);
        orapinFBiz.requestOrapin(new OrapinFBiz.RequestOrapiinResult() {
            @Override
            public void requstOrapiinResult(List<Orapiin> orapiins, List<FirstOrapin> firstOrapins) {
                mOrapiins = orapiins;
                stringBuilder.append("\n请准时将书放到书柜中");

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
                    }
                }
                showName.append("未还，请将包装一起放到书柜中");
                stringBuilder.append("\n" + showName.toString());

                //还书成功后预约书柜
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
        CaurserBiz.requsetCaurser(getActivity(), mDateBean, mLtTime, String.valueOf(mApiAddress.getGs_id()), mOrapiins, mOrapiout,
                new CaurserBiz.RequstCaurserResult() {
                    @Override
                    public void requstCaurserResult() {
                        stringBuilder.append("\n书柜预约成功!");
                        PreferencesUtils.putString(getActivity(), Contants.APIADDRESS_JSON, GsonUtil.GsonString(mApiAddress));
                        updateStillFragment();
                    }

                    @Override
                    public void requstCaurserResultError(int code) {
                        if (code == 546) {
                            ToastUtils.showToastInCenter(getActivity(), 1, "此书柜全被占用，请选择其他地址的书柜", Toast.LENGTH_SHORT);
                            updateStillFragment();
                        } else {
                            updateStillFragment();
                        }
                    }
                });
    }


    private void updateStillFragment() {
        if (spotsDialog != null && spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        CartProvider cartProvider = new CartProvider(getActivity());
        for (ShoppingCart shoppingCart : carts) {
            cartProvider.delete(shoppingCart);
        }
        loadData();
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessage("完成订单" + stringBuilder.toString());
        builder.setPositiveButton("继续选书", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) getActivity()).setCurrentPage(0);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("查看订单", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.create().show();
        stringBuilder.delete(0, stringBuilder.length());
    }

}
