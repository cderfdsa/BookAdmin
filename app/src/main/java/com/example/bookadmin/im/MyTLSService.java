package com.example.bookadmin.im;

import android.content.Context;

import com.example.bookadmin.im.init.TLSConfiguration;

import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by Administrator on 2017-06-21.
 */

public class MyTLSService {

    private static MyTLSService tlsService = null;

    private TLSLoginHelper loginHelper;
    private static int lastErrno = -1;

    private MyTLSService(){}

    public static MyTLSService getInstance() {
        if (tlsService == null) {
            tlsService = new MyTLSService();
        }
        return tlsService;
    }

    /**
     * @function: 初始化TLS SDK, 必须在使用TLS SDK相关服务之前调用
     * @param context: 关联的activity
     * */
    public void initTlsSdk(Context context) {
        loginHelper = TLSLoginHelper.getInstance().init(context.getApplicationContext(),
                TLSConfiguration.SDK_APPID, TLSConfiguration.ACCOUNT_TYPE, TLSConfiguration.APP_VERSION);
        loginHelper.setTimeOut(TLSConfiguration.TIMEOUT);
        loginHelper.setLocalId(TLSConfiguration.LANGUAGE_CODE);
        loginHelper.setTestHost("", true);
    }


    public void clearUserInfo(String identifier) {
        loginHelper.clearUserInfo(identifier);
        lastErrno = -1;
    }

    public String getLastUserIdentifier() {
        TLSUserInfo userInfo = getLastUserInfo();
        if (userInfo != null)
            return userInfo.identifier;
        else
            return null;
    }

    public TLSUserInfo getLastUserInfo() {
        return loginHelper.getLastUserInfo();
    }


    public String getUserSig(String identify) {
        return loginHelper.getUserSig(identify);
    }

//    public boolean needLogin(String identifier) {
//        if (identifier == null)
//            return true;
//        return loginHelper.needLogin(identifier);
//    }

    public static void setLastErrno(int errno) {
        lastErrno = errno;
    }
    public static int getLastErrno() {
        return lastErrno;
    }
}
