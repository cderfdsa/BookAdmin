package com.example.bookadmin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.adapter.BaseAdapter;
import com.example.bookadmin.adapter.BaseViewHolder;
import com.example.bookadmin.adapter.BookArrtAdapter;
import com.example.bookadmin.adapter.SimpleAdapter;
import com.example.bookadmin.bean.BookAttr;
import com.example.bookadmin.bean.FirstTypeParam;
import com.example.bookadmin.requrest.BookAttrBiz;
import com.example.bookadmin.interf.OnPageListener;
import com.example.bookadmin.tools.utils.PreferencesUtils;
import com.example.bookadmin.tools.utils.SearchHistoryUtils;
import com.example.bookadmin.tools.utils.TextUtiles;
import com.example.bookadmin.tools.utils.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2017-05-08.
 */

public class SearchActivity extends IMBaseActivity implements OnPageListener<BookAttr>, SearchView.OnQueryTextListener, View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout materialRefreshLayout;

    private BookArrtAdapter bookArrtAdapter;
    private FirstTypeParam param;

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    private RelativeLayout historyLayout;
    private TextView tvClear;
    private RecyclerView historyView;

    private Handler handler = new Handler();
    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            getTxt(mSearchAutoComplete.getText().toString());
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_view);
        historyLayout = (RelativeLayout) findViewById(R.id.history_layout);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        historyView = (RecyclerView) findViewById(R.id.history_view);
        tvClear.setOnClickListener(this);
        initToolbar();

        param = new FirstTypeParam();
        param.setAddress(Contants.getLibAddressId());
        param.setBooktype("");
        param.setOrdertype("");
        param.setPage(1);
        param.setSearch("");
        param.setPublish("");
        param.setAuthor("");
        param.setTitle("");
        param.setPrices("");
//        loadData();
        loadHistory();
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchAutoComplete.isShown()) {
                    closeImm();
                } else {
                    finish();
                }
            }
        });
    }

    private void closeImm() {
        try {
            mSearchAutoComplete.setText("");
            Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
            method.setAccessible(true);
            method.invoke(mSearchView);
            mSearchView.clearFocus();
            mSearchView.setIconifiedByDefault(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchView.setQueryHint("搜索相关图书");

        try {
            Class cls = Class.forName("android.support.v7.widget.SearchView");
            Field field = cls.getDeclaredField("mSearchSrcTextView");
            field.setAccessible(true);
            TextView tv  = (TextView) field.get(mSearchView);
            Class[] clses = cls.getDeclaredClasses();
            for(Class cls_ : clses) {
                if(cls_.toString().endsWith("android.support.v7.widget.SearchView$SearchAutoComplete")) {
                    Class targetCls = cls_.getSuperclass().getSuperclass().getSuperclass().getSuperclass();
                    Field cuosorIconField = targetCls.getDeclaredField("mCursorDrawableRes");
                    cuosorIconField.setAccessible(true);
                    cuosorIconField.set(tv, R.drawable.cursor_color);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
        //设置输入框提示文字样式
        mSearchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.background_light));
        mSearchAutoComplete.setTextSize(14);
        //设置触发查询的最少字符数（默认2个字符才会触发查询）
        mSearchAutoComplete.setThreshold(1);
        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(true);
        mSearchView.setIconifiedByDefault(false);
        //修改搜索框控件间的间隔（这样只是为了更加接近网易云音乐的搜索框）
        LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 30;
        search_edit_frame.setLayoutParams(params);

        mSearchView.requestFocusFromTouch();
        mSearchView.setFocusable(true);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.requestFocus();

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconifiedByDefault(false);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void loadData() {
        BookAttrBiz<BookAttr> bookAttrBiz = new BookAttrBiz.Builder(this)
                .setFirstTypeParam(param)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setRefreshLayout(materialRefreshLayout)
                .setType(2)
                .build();

        bookAttrBiz.request();

    }

    private void loadHistory() {
        final List<String> historys = SearchHistoryUtils.readHistory(this);
        if (historys != null && historys.size() > 0) {
            historyLayout.setVisibility(View.VISIBLE);
            HistoryAdapter historyAdapter = new HistoryAdapter(this, historys);
            historyView.setAdapter(historyAdapter);
            historyAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String strHis = historys.get(position);
                    mSearchView.setIconifiedByDefault(false);
                    mSearchAutoComplete.setText(strHis);
                    getTxt(strHis);
                }
            });
            historyView.setLayoutManager(new GridLayoutManager(this, 2));
            historyView.setItemAnimator(new DefaultItemAnimator());
            historyView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } else {
            historyLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (mSearchAutoComplete.isShown()) {
                closeImm();
                return false;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getTxt(String txt) {
        if (txt == null || txt.length() == 0) {
            txt = "";
        }
        if (TextUtiles.checkName(txt)) {
            ToastUtils.showToastInCenter(SearchActivity.this, 1, "不允许输入特殊符号！", Toast.LENGTH_SHORT);
            return;
        }
        param.setSearch(txt);
        loadData();
    }

    private void setAdapter(final List<BookAttr> bookAttrs) {
        bookArrtAdapter = new BookArrtAdapter(this, bookAttrs);
        bookArrtAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookAttr bookAttr = bookAttrs.get(position);
                Intent intent = new Intent(SearchActivity.this, BookAttrDetailActivity.class);

                intent.putExtra(Contants.BS_ID, bookAttr.getBs_id());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(bookArrtAdapter);

//        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    public void load(List<BookAttr> datas, FirstTypeParam param) {
        setAdapter(datas);
    }

    @Override
    public void refresh(List<BookAttr> datas, FirstTypeParam param) {
        bookArrtAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
        closeImm();
    }

    @Override
    public void loadMore(List<BookAttr> datas, FirstTypeParam param) {
        bookArrtAdapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(bookArrtAdapter.getDatas().size());
        closeImm();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        getTxt(query);
        SearchHistoryUtils.insertHistory(this, query);
        loadHistory();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("")) {
            if (delayRun != null) {
                //每次editText有变化的时候，则移除上次发出的延迟线程
                handler.removeCallbacks(delayRun);
            }
            //延迟800ms，如果不再输入字符，则执行该线程的run方法
            handler.postDelayed(delayRun, 800);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_clear) {
            PreferencesUtils.putString(this, Contants.SEARCH_JSON, "");
            loadHistory();
        }
    }


    class HistoryAdapter extends SimpleAdapter<String> {

        public HistoryAdapter(Context context, List<String> historys) {
            super(context, R.layout.item_history, historys);
        }

        @Override
        protected void convert(BaseViewHolder viewHoder, String item, int pos) {
            viewHoder.getTextView(R.id.tv_history).setText(item);
        }
    }
}
