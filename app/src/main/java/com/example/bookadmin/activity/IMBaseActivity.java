package com.example.bookadmin.activity;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.im.ErrorDialogFragment;
import com.example.bookadmin.im.chat.MessageEvent;
import com.example.bookadmin.im.init.TlsBusiness;
import com.example.bookadmin.tools.UserInfoCache;
import com.example.bookadmin.widget.CustomDialog;

/**
 * Created by Administrator on 2017-06-19.
 */

public class IMBaseActivity  extends BaseActivity  {

    private static final String TAG = IMBaseActivity.class.getSimpleName();
    //错误消息弹窗
    private ErrorDialogFragment mErrDlgFragment;
    //被踢下线广播监听
    private LocalBroadcastManager mLocalBroadcatManager;
    private BroadcastReceiver mExitBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcatManager = LocalBroadcastManager.getInstance(this);
        mExitBroadcastReceiver = new ExitBroadcastRecevier();
        mLocalBroadcatManager.registerReceiver(mExitBroadcastReceiver, new IntentFilter(Contants.EXIT_APP));
        mErrDlgFragment = new ErrorDialogFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcatManager.unregisterReceiver(mExitBroadcastReceiver);
    }

    public void onReceiveExitMsg() {
         //显示被踢下线通知
        CustomDialog.Builder builder = new CustomDialog.Builder(IMBaseActivity.this);
        builder.setMessage("您的账号在其他地方登陆");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LocalBroadcastManager.getInstance(IMBaseActivity.this).sendBroadcast(new Intent(Contants.NET_LOONGGG_EXITAPP));
            }
        });

        builder.setNegativeButton("重新登录", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                logout();
            }
        });
        CustomDialog customDialog = builder.create();
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);
        customDialog.show();
    }

    public class ExitBroadcastRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contants.EXIT_APP)) {
                //在被踢下线的情况下，执行退出前的处理操作：停止推流、关闭群组
                onReceiveExitMsg();
            }
        }
    }

    protected void showErrorAndQuit(String errorMsg) {
        if (!mErrDlgFragment.isAdded() && !this.isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);
            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }


    public void logout(){
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfoCache.clearCache(this);
        UserInfoCache.clearAddress(this);
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
//        FriendshipInfo.getInstance().clear();
//        GroupInfo.getInstance().clear();
        Intent intent = new Intent(IMBaseActivity.this, LocationActivity.class);
        startActivity(intent);
        finish();
    }

}
