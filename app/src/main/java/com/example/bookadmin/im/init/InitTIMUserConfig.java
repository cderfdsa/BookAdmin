package com.example.bookadmin.im.init;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.bookadmin.Contants;
import com.example.bookadmin.activity.LocationActivity;
import com.example.bookadmin.im.chat.RefreshEvent;
import com.example.bookadmin.im.group.GroupEvent;
import com.example.bookadmin.tools.utils.LogUtils;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupSettings;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qcloud.ui.NotifyDialog;

/**
 * Created by Administrator on 2017-06-19.
 */

public class InitTIMUserConfig {

    /**
     * 设置当前用户的用户配置，登录前设置
     */
    public static TIMUserConfig setTIMUserConfig(final Context context) {
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        //设置群组资料拉取字段
        userConfig.setGroupSettings(new TIMGroupSettings())
                //设置资料关系链拉取字段
//                .setFriendshipSettings(new InitFriendshipSettings())
                //设置用户状态变更事件监听器
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        LogUtils.i("onForceOffline 被其他终端踢下线");
                        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(new Intent(Contants.EXIT_APP));
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        LogUtils.i("onUserSigExpired  用户签名过期了，需要刷新userSig重新登录SDK");
//                        IMLogin.getInstance().reLogin();
                        new NotifyDialog().show("账号登录已过期，请重新登录", ((LocationActivity) context).getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                            logout();
                            }
                        });
                    }
                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        LogUtils.i("onConnected");
                    }

                    @Override
                    public void onDisconnected(int i, String s) {
                        LogUtils.i("onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String s) {
                        LogUtils.i("onWifiNeedAuth");
                    }
                })
                //设置群组事件监听器
                .setGroupEventListener(new TIMGroupEventListener() {
                    @Override
                    public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                        LogUtils.i("onGroupTipsEvent, type: " + elem.getTipsType());
                    }
                });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
//        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        return userConfig;
    }

}
