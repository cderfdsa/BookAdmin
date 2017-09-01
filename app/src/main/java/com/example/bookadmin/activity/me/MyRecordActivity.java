package com.example.bookadmin.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.BookAttrDetailActivity;
import com.example.bookadmin.activity.IMBaseActivity;
import com.example.bookadmin.adapter.BaseAdapter;
import com.example.bookadmin.adapter.MyReadAdapter;
import com.example.bookadmin.bean.MyReadBean;
import com.example.bookadmin.requrest.MyReadBiz;
import com.example.bookadmin.interf.OnPageSeListener;
import com.example.bookadmin.tools.utils.TextUtiles;
import com.example.bookadmin.tools.utils.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-05-26.
 */

public class MyRecordActivity extends IMBaseActivity implements OnPageSeListener<MyReadBean>, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout materialRefreshLayout;

    private MyReadAdapter myReadAdapter;

    private Map<String, String> params;

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecord);

        initView();
        initToolbar();

        inspect();
    }

    private void initView(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_view);

    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchView.setQueryHint("搜索我的图书记录");

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
        mSearchView.setIconifiedByDefault(true);
        //修改搜索框控件间的间隔（这样只是为了更加接近网易云音乐的搜索框）
        LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 30;
        search_edit_frame.setLayoutParams(params);

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconifiedByDefault(false);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK) {
            clickBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clickBack(){
        if (mSearchAutoComplete.isShown()) {
            try {
                mSearchAutoComplete.setText("");
                Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                method.setAccessible(true);
                method.invoke(mSearchView);
                mSearchView.setIconifiedByDefault(true);
                mSearchView.clearFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }



    private void setAdapter(final List<MyReadBean> myReadBeens){
        myReadAdapter = new MyReadAdapter(this, myReadBeens);
        myReadAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyReadBean readBean = myReadBeens.get(position);
                Intent intent = new Intent(MyRecordActivity.this, BookAttrDetailActivity.class);

                intent.putExtra(Contants.BS_ID, readBean.getBs_id());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(myReadAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void inspect(){
        params = new HashMap<>();
        params.put(Contants.PAGE, "1");
        params.put(Contants.SEARCH, "");
        loadData();
    }


    private void loadData(){
        MyReadBiz.Builder builder = new MyReadBiz.Builder(this)
                .setLoadMore(true)
                .setUrl(Contants.API.MYREAD)
                .setRefreshLayout(materialRefreshLayout)
                .setOnPageListener(this)
                .setParams(params);
        builder.build().request();
    }


    @Override
    public void load(List<MyReadBean> datas) {
        setAdapter(datas);
    }

    @Override
    public void refresh(List<MyReadBean> datas) {
        myReadAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<MyReadBean> datas) {
        myReadAdapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(myReadAdapter.getDatas().size());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query == null || query.length() == 0){
            query = "";
        }
        if(TextUtiles.checkName(query)){
            ToastUtils.showToastInCenter(MyRecordActivity.this, 1, "不允许输入特殊符号", Toast.LENGTH_SHORT);
            return false;
        }
        params.put(Contants.SEARCH, query);
        loadData();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}
