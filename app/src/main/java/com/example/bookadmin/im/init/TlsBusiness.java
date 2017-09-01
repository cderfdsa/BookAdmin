package com.example.bookadmin.im.init;

import android.content.Context;
import android.util.Log;

import com.example.bookadmin.Contants;
import com.example.bookadmin.im.MyTLSService;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSRefreshUserSigListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by Administrator on 2017-06-21.
 */

public class TlsBusiness {

    private TlsBusiness(){}

    public static void init(Context context){
        TLSConfiguration.setSdkAppid(Contants.IMSDK_APPID);
        TLSConfiguration.setAccountType(Contants.IMSDK_ACCOUNT_TYPE);
        TLSConfiguration.setTimeout(8000);
        MyTLSService.getInstance().initTlsSdk(context);
    }

    public static void logout(String id){
        MyTLSService.getInstance().clearUserInfo(id);

    }

    /**
     * 重新登录逻辑
     */
    public void reLogin() {



    }

}
