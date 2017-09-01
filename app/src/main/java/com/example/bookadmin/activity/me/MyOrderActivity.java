package com.example.bookadmin.activity.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.example.bookadmin.R;
import com.example.bookadmin.activity.BaseActivity;
import com.example.bookadmin.activity.IMBaseActivity;
import com.example.bookadmin.fragment.MyLibraryOrderFragment;
import com.example.bookadmin.fragment.MyReturnedBooksFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-25.
 */

public class MyOrderActivity extends IMBaseActivity implements MyLibraryOrderFragment.OnOutLoadListener, MyReturnedBooksFragment.OnInLoadListener{

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;


    @ViewInject(R.id.toolbar_tab)
    TabLayout toolbarTab;

    @ViewInject(R.id.main_vp_container)
    ViewPager mViewPager;

    private ViewPagerAdapter myPagerAdapter;
    private ArrayList<Fragment> fragments;

    private boolean isOutload = false;
    private boolean isInload = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);

        ViewUtils.inject(this);

        setToolBarReplaceActionBar();

        initFrag();
        setViewPagerAdapter();
        setTabBindViewPager();
    }


    private void setToolBarReplaceActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOutload || isInload){

                }else{
                    finish();
                }
            }
        });
    }

    private void initFrag() {
        fragments = new ArrayList<>();
        fragments.add(new MyLibraryOrderFragment());
        fragments.add(new MyReturnedBooksFragment());
    }

    private void setViewPagerAdapter() {
        myPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, fragments);
        mViewPager.setAdapter(myPagerAdapter);
    }

    private void setTabBindViewPager() {
        //tablayout和viewpager建立联系方式一：tab与viewpager之间的相互绑定
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(toolbarTab));
        toolbarTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //tablayout和viewpager建立联系方式二： 使用此方法Tablayout中的TabItem设置icon无效
        // （android:icon="@drawable/tab_selector" ）只能使用 android:text="分享"
        //  toolbarTab.setupWithViewPager(mViewPager);
    }

    @Override
    public void onOutLoad(boolean isLoad) {
        isOutload = isLoad;
    }

    @Override
    public void onInLoad(boolean isLoad) {
        isInload = isLoad;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if(isOutload || isInload){
//                isOutload = false;
//                isInload = false;
                return true;
            }else{
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private ArrayList<Fragment> mFragments;

        private String tabTitles[] = new String[]{"预约", "开柜"};


        public ViewPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment>fragments) {
            super(fm);
            this.mContext = context;
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }



}
