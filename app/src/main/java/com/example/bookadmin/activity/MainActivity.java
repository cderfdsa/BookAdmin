package com.example.bookadmin.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.bookadmin.R;
import com.example.bookadmin.bean.Tab;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.fragment.BorrowFragment;
import com.example.bookadmin.fragment.CartFragment;
import com.example.bookadmin.fragment.ChatFragment;
import com.example.bookadmin.fragment.ClassificationFragment;
import com.example.bookadmin.fragment.MimeFragment;
import com.example.bookadmin.im.chat.MessageEvent;
import com.example.bookadmin.im.group.GroupInfo;
import com.example.bookadmin.im.init.TlsBusiness;
import com.example.bookadmin.receiver.NetWorkStateReceiver;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.tools.UserInfoCache;
import com.example.bookadmin.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-04.
 */

public class MainActivity extends IMBaseActivity implements BorrowFragment.OnDelShowListener, CartFragment.OnCartDelShowListener {

    private List<Tab> mTabs = new ArrayList<>(3);
    private LayoutInflater mInflater;
    private FragmentTabHost mTabhost;

    private MimeFragment mimeFragment;
//    private BorrowFragment borrowFragment;
    private CartFragment cartFragment;

    private long exitTime = 0;

    private boolean showDel = false;

    private String strTab;

    NetWorkStateReceiver netWorkStateReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        super.onResume();
        String type = getIntent().getStringExtra("bookattr");
        if (type != null) {
            if (mTabhost != null) {
                mTabhost.setCurrentTab(1);

//                refDataCart();
            }
            getIntent().removeExtra("bookattr");
            strTab = getString(R.string.bookborrow);
        } else {
            if (mTabhost != null) {
                if (strTab.equals(getString(R.string.bookborrow))) {
                    mTabhost.setCurrentTab(1);
                } else if (strTab.equals(getString(R.string.mime))) {
                    mTabhost.setCurrentTab(3);
                } else if (strTab.equals(getString(R.string.bookselection))) {
                    mTabhost.setCurrentTab(0);
                } else if (strTab.equals(getString(R.string.chat))) {
                    mTabhost.setCurrentTab(2);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        super.onPause();
    }

    private void initTab() {

        Tab tab_selection = new Tab(ClassificationFragment.class, R.string.bookselection, R.drawable.selector_icon_home);
//        Tab tab_borrow = new Tab(BorrowFragment.class, R.string.bookborrow, R.drawable.selector_icon_cart);
        Tab tab_borrow = new Tab(CartFragment.class, R.string.bookborrow, R.drawable.selector_icon_cart);
        Tab tab_chat = new Tab(ChatFragment.class, R.string.chat, R.drawable.selector_icon_chat);
        Tab tab_mime = new Tab(MimeFragment.class, R.string.mime, R.drawable.selector_icon_mine);

        mTabs.add(tab_selection);
        mTabs.add(tab_borrow);
        mTabs.add(tab_chat);
        mTabs.add(tab_mime);

        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(), null);
        }
        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                strTab = tabId;
                if (tabId.equals(getString(R.string.bookborrow))) {
//                    refDataStill();
                    refDataCart();
                }
                if (tabId.equals(getString(R.string.mime))) {
                    rdfDateMime();
                    changeDelIcon();
                }
                if (tabId.equals(getString(R.string.bookselection))) {
                    changeDelIcon();
                }
            }
        });
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        setCurrentPage(0);
    }

    public void setCurrentPage(int tab) {
        mTabhost.setCurrentTab(tab);
        switch (tab) {
            case 0:
                strTab = getString(R.string.bookselection);
                break;
            case 1:
                strTab = getString(R.string.bookborrow);
                break;
            case 2:
                strTab = getString(R.string.chat);
                break;
            case 4:
                strTab = getString(R.string.mime);
                break;
        }
    }


    private void changeDelIcon() {
        if (showDel) {
//            refBorrowShow();
            refCartShow();
            showDel = false;
        }
    }

    private View buildIndicator(Tab tab) {

        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
        if (tab.getIcon() != -1) {

            img.setBackgroundResource(tab.getIcon());
        }
        text.setText(tab.getTitle());

        return view;
    }


//    private void refDataStill() {
//
//        if (borrowFragment == null) {
//
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.bookborrow));
//            if (fragment != null) {
//                borrowFragment = (BorrowFragment) fragment;
//                borrowFragment.loadData();
//            }
//        } else {
//            borrowFragment.loadData();
//
//        }
//    }

    private void refDataCart() {

        if (cartFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.bookborrow));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.loadData();
            }
        } else {
            cartFragment.loadData();

        }
    }

    private void rdfDateMime() {
        if (mimeFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.mime));
            if (fragment != null) {
                mimeFragment = (MimeFragment) fragment;
                mimeFragment.refData();
            }
        } else {
            mimeFragment.refData();

        }
    }

//    private void refBorrowShow() {
//
//        if (borrowFragment == null) {
//
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.bookborrow));
//            if (fragment != null) {
//                borrowFragment = (BorrowFragment) fragment;
//                borrowFragment.refShow();
//            }
//        } else {
//            borrowFragment.refShow();
//
//        }
//    }

    private void refCartShow() {

        if (cartFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.bookborrow));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refShow();
            }
        } else {
            cartFragment.refShow();

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (showDel) {
//                refBorrowShow();
                refCartShow();
                showDel = false;
                return false;
            } else {
                // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
                if (System.currentTimeMillis() - exitTime > 2000) {
                    ToastUtils.showShortToast(this, "再按一次退出程序");
                    // 将系统当前的时间赋值给exitTime
                    exitTime = System.currentTimeMillis();
                } else {
                    exitApp();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用程序的方法，发送退出程序的广播
     */
    private void exitApp() {
        Intent intent = new Intent();
        intent.setAction("net.loonggg.exitapp");
        this.sendBroadcast(intent);
    }


    @Override
    public void onDelShow(boolean isShow) {
        showDel = isShow;
    }

    @Override
    public void onCartDelShow(boolean isShow) {
        showDel = isShow;
    }


}
