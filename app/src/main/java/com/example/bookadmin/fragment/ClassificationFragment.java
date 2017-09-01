package com.example.bookadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.example.bookadmin.activity.BookAttrDetailActivity;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.SearchActivity;
import com.example.bookadmin.adapter.BaseAdapter;
import com.example.bookadmin.adapter.ClassificationAdapter;
import com.example.bookadmin.bean.BookAttr;
import com.example.bookadmin.bean.BookTypeBean;
import com.example.bookadmin.bean.FirstTypeParam;
import com.example.bookadmin.bean.OrderType;
import com.example.bookadmin.bean.ScreenAll;
import com.example.bookadmin.bean.ScreenPublish;
import com.example.bookadmin.requrest.BookAttrBiz;
import com.example.bookadmin.requrest.BookTypeBiz;
import com.example.bookadmin.interf.OnPageListener;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.widget.expant.ClaLinerLayout;
import com.example.bookadmin.widget.expant.ExpandTabView;
import com.example.bookadmin.widget.expant.ScreenRelativeLayout;
import com.example.bookadmin.widget.expant.SortRelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-08.
 */

public class ClassificationFragment extends BaseFragment implements OnPageListener<BookAttr> {

    public static final String TAG = "ClassificationFragment";

    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout materialRefreshLayout;
    private ExpandTabView expandTabView;

    private RelativeLayout relativeLayout;
    private Button btnAgain;

    private ImageView searchView;

    private ClassificationAdapter classificationAdapter;

    private FirstTypeParam firstTypeParam;

    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ClaLinerLayout claLinerLayout;
    private SortRelativeLayout sortRelativeLayout;
    private ScreenRelativeLayout screenRelativeLayout;

    private BookAttrBiz.Builder builder;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classification, container, false);
        initView(view);
        initValue();
        initListener();

        return view;
    }


    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);
        expandTabView = (ExpandTabView) view.findViewById(R.id.expandtab_view);
        searchView = (ImageView) view.findViewById(R.id.search_mag_icon);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        relativeLayout = (RelativeLayout) view.findViewById(R.id.id_relative_layout);
        btnAgain = (Button) view.findViewById(R.id.btn_again);
    }

    private void initValue(){
        claLinerLayout = new ClaLinerLayout(getActivity());
        sortRelativeLayout = new SortRelativeLayout(getActivity());
        screenRelativeLayout = new ScreenRelativeLayout(getActivity());
        claLinerLayout.setAdapter();
        sortRelativeLayout.setAdapter();
        screenRelativeLayout.setAdapter();

        mViewArray.add(claLinerLayout);
        mViewArray.add(sortRelativeLayout);
//        mViewArray.add(null);
        mViewArray.add(screenRelativeLayout);

        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("全部分类");
        mTextArray.add("销量");
//        mTextArray.add("价格");
        mTextArray.add("筛选");

        expandTabView.setValue(mTextArray, mViewArray);
    }



    private void initListener(){
        claLinerLayout.setOnSelectListener(new ClaLinerLayout.OnSelectListener() {
            @Override
            public void getValue(BookTypeBean bookTypeBean) {
                onRefreshType(claLinerLayout, bookTypeBean);
                if(bookTypeBean.getS_id() == -1){
                    firstTypeParam.setBooktype("");
                    builder.setFirstTypeParam(firstTypeParam).build().request();
                }else {
                    firstTypeParam.setBooktype(String.valueOf(bookTypeBean.getS_id()));
                    builder.setFirstTypeParam(firstTypeParam).build().request();
                }
            }
        });
        sortRelativeLayout.setOnSelectListener(new SortRelativeLayout.OnSelectListener() {

            @Override
            public void getValue(OrderType orderType, String showText, int pos) {
                onRefresh(sortRelativeLayout, showText);
                firstTypeParam.setOrdertype(String.valueOf(orderType.getOt_sn()));
                LogUtils.i("orderType ： " + orderType.getOt_sn());
                builder.setFirstTypeParam(firstTypeParam).build().request();
            }
        });

        expandTabView.setOnPriceSelectListener(new ExpandTabView.OnPriceSelectListener() {//价格
            @Override
            public void getValue(String distance, String showText) {
                firstTypeParam.setOrdertype(distance);
                builder.setFirstTypeParam(firstTypeParam).build().request();
            }
        });
        screenRelativeLayout.setOnSelectListener(new ScreenRelativeLayout.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                onRefresh(screenRelativeLayout, showText);
            }
        });
        screenRelativeLayout.setOnFilterSureListener(new ScreenRelativeLayout.OnFilterSureListener() {
            @Override
            public void OnFilterSure(String publish, String author, String title, String price) {
                setFirstTypeParam(1, "", "", publish, author, title, price);
                builder.setFirstTypeParam(firstTypeParam).build().request();
                expandTabView.filterPopDissmiss();
            }

            @Override
            public void OnFilterSureNull() {
                setFirstTypeParam(1, "", "", "", "", "", "");
                builder.setFirstTypeParam(firstTypeParam).build().request();
                expandTabView.filterPopDissmiss();
//                expandTabView.setTButton();
            }
        });
    }

    private void onRefreshType(View view, BookTypeBean bookTypeBean) {

        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(bookTypeBean.getS_name())) {
            expandTabView.setTitle(bookTypeBean.getS_name(), position);
        }
    }

    private void onRefresh(View view, String showText) {

        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void init() {

        loadExpandtab();


        firstTypeParam = new FirstTypeParam();
        firstTypeParam.setAddress(Contants.getLibAddressId());
        setFirstTypeParam(1, "", "", "", "", "", "");
        firstTypeParam.setSearch("");
        builder = new BookAttrBiz.Builder(getActivity())
                .setFirstTypeParam(firstTypeParam)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setRefreshLayout(materialRefreshLayout)
                .setRelativeLayout(relativeLayout)
                .setBtnAgain(btnAgain)
                .setExpandTabView(expandTabView)
                .setFragment(this)
                .setType(1);
        builder.build().request();
    }

    public void loadExpandtab() {
        BookTypeBiz.requestData(getActivity(), new BookTypeBiz.BookStypeRequestCallback(){
            @Override
            public void handleQueryResult(ArrayList<BookTypeBean> groups, SparseArray<LinkedList<BookTypeBean>> children) {
                claLinerLayout.notifyData(groups, children);
            }
        });

        BookTypeBiz.requestBookOrder(getActivity(), new BookTypeBiz.BookOrderRequestCallback() {
            @Override
            public void handleQueryResult(List<OrderType> orderTypes) {
                OrderType orderType = new OrderType();
                orderType.setOt_sn(5);
                orderType.setOt_zhus("价格");
                orderTypes.add(orderType);
                sortRelativeLayout.notifyData(orderTypes);
            }
        });

        BookTypeBiz.requestBookScreen(getActivity(), new BookTypeBiz.BookScreenRequestCallback() {
            @Override
            public void handleQueryResult(List<ScreenPublish> publishes, List<ScreenAll> authors, List<ScreenAll> titles) {
               screenRelativeLayout.notifyData(publishes, authors, titles);
            }
        });
    }


    private void setAdapter(List<BookAttr> bookAttrs) {
        classificationAdapter = new ClassificationAdapter(getActivity(), bookAttrs);
        classificationAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookAttr bookAttr = classificationAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), BookAttrDetailActivity.class);

                intent.putExtra(Contants.BS_ID, bookAttr.getBs_id());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(classificationAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }


    @Override
    public void load(List<BookAttr> datas, FirstTypeParam param) {
        if(datas != null) {
            setAdapter(datas);
        }else{
            datas = new ArrayList<>();
            setAdapter(datas);
            ToastUtils.showToastInCenter(getActivity(), 1, "没有数据", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void refresh(List<BookAttr> datas, FirstTypeParam param) {
        if(datas!= null) {
            classificationAdapter.refreshData(datas);
            mRecyclerView.scrollToPosition(0);
        }else{
            datas = new ArrayList<>();
            setAdapter(datas);
            ToastUtils.showToastInCenter(getActivity(), 1, "没有数据", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void loadMore(List<BookAttr> datas, FirstTypeParam param) {
        if(datas != null) {
            classificationAdapter.loadMoreData(datas);
            mRecyclerView.scrollToPosition(classificationAdapter.getDatas().size());
        }else{
            ToastUtils.showToastInCenter(getActivity(), 1, "没有更多数据", Toast.LENGTH_SHORT);
        }
    }

    private void setFirstTypeParam(int page, String booktype, String ordertype, String publish, String author, String title, String price){
        firstTypeParam.setPage(page);
        firstTypeParam.setBooktype(booktype);
        firstTypeParam.setOrdertype(ordertype);
        firstTypeParam.setPublish(publish);
        firstTypeParam.setAuthor(author);
        firstTypeParam.setTitle(title);
        firstTypeParam.setPrices(price);
    }

    @Override
    public void onPause() {
        super.onPause();
        expandTabView.onPressBack();
    }

}
