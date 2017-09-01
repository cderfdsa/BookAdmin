package com.example.bookadmin.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.activity.MainActivity;
import com.example.bookadmin.activity.logic.LoginActivity;
import com.example.bookadmin.activity.me.MyOrderActivity;
import com.example.bookadmin.activity.me.MyRecordActivity;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.im.LoginBusiness;
import com.example.bookadmin.widget.DialogUtil;
import com.tencent.imsdk.TIMCallBack;

/**
 * Created by Administrator on 2017-05-04.
 */

public class MimeFragment extends BaseFragment implements View.OnClickListener {


//    private Toolbar toolbar;
//    private ImageView headIv;
//    private LinearLayout headLayout;
//    private AppBarLayout appBarLayout;
//    private CollapsingToolbarLayout collapsingToolbarLayout;
//    private CoordinatorLayout coordinatorLayout;
    private TextView tvName;
    private TextView tvLogout;
//    private TextView tvSet;

    private TextView btnMyorder;
    private TextView btnMyrecord;

    private static final int REFRESH_COMPLETE = 0X133;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    showUser();
                    break;

            }
        };
    };

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mime, container, false);

//        toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
//        headIv = (ImageView) view.findViewById(R.id.head_iv);
//        headLayout = (LinearLayout) view.findViewById(R.id.my_head_layout);
//        tvSet = (TextView) view.findViewById(R.id.tv_set);
//        appBarLayout = (AppBarLayout) view.findViewById(R.id.my_app_bar_layout);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.my_collapsingToolbarLayout);
//        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.my_coordinator_Layout);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvLogout = (TextView) view.findViewById(R.id.tv_logout);

        btnMyorder = (TextView) view.findViewById(R.id.btn_myorder);
        btnMyrecord = (TextView) view.findViewById(R.id.btn_myrecord);

        tvName.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        btnMyorder.setOnClickListener(this);
        btnMyrecord.setOnClickListener(this);

        //用toolBar替换ActionBar
//        setToolBarReplaceActionBar();

        //把title设置到CollapsingToolbarLayout上
//        setTitleToCollapsingToolbarLayout();
        return view;
    }



    /**
     * 用toolBar替换ActionBar
     */
//    private void setToolBarReplaceActionBar() {
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        //设置返回
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "返回", Toast.LENGTH_LONG).show();
                // onBackPressed();//结束程序
//            }
//        });
//    }

    /**
     * 使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，
     * 设置到Toolbar上则不会显示
     */
//    private void setTitleToCollapsingToolbarLayout() {
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset <= -headLayout.getHeight() / 2) {
//                    collapsingToolbarLayout.setTitle(getString(R.string.borrowedRecord));
//                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
//                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
//                } else {
//                    collapsingToolbarLayout.setTitle("");
//                }
//            }
//        });
//    }

    @Override
    public void init() {
        showUser();
    }

    public void refData(){
        showUser();
    }

    private void showUser() {

        if(UserInfo.getInstance().getId() == null){
            tvLogout.setVisibility(View.GONE);
            tvName.setText(R.string.to_login);
            tvName.setEnabled(true);
            btnMyorder.setVisibility(View.GONE);
            btnMyrecord.setVisibility(View.GONE);
        } else {
            tvName.setEnabled(false);
            tvLogout.setVisibility(View.VISIBLE);
            tvName.setText(UserInfo.getInstance().getNickname());
            btnMyorder.setVisibility(View.VISIBLE);
            btnMyrecord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_name:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, Contants.REQUEST_CODE);
                break;
            case R.id.tv_logout:
                showLogout();
                break;
            case R.id.btn_myorder:
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.btn_myrecord:
                startActivity(new Intent(getActivity(), MyRecordActivity.class));
                break;
        }
    }

    /**
     * 退出登录
     */
    private void showLogout() {
        DialogUtil.showComfirmDialog(getContext(), "你确定要退出当前账号吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                LoginBusiness.logout(new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        if (getActivity() != null){
                            Toast.makeText(getActivity(), "退出登录失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess() {
                        if (getActivity() != null && getActivity() instanceof MainActivity){
                            ((MainActivity) getActivity()).logout();
                            tvLogout.setVisibility(View.GONE);
                            tvName.setText(R.string.to_login);
                            tvName.setEnabled(true);
                            btnMyorder.setVisibility(View.GONE);
                            btnMyrecord.setVisibility(View.GONE);
                        }
                    }
                });

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Contants.REQUEST_CODE){
            if(resultCode == Contants.LOGIN_RESULTCODE) {
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
            }
        }
    }

}
