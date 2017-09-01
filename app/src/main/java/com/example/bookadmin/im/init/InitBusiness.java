package com.example.bookadmin.im.init;

import android.content.Context;
import android.os.Environment;

import com.example.bookadmin.Contants;
import com.example.bookadmin.tools.utils.LogUtils;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;

/**
 * Created by Administrator on 2017-06-21.
 */

public class InitBusiness {

    private static final String TAG = InitBusiness.class.getSimpleName();

    private InitBusiness(){}

    public static void start(Context context){
        initImsdk(context, TIMLogLevel.DEBUG.ordinal());
    }

    public static void start(Context context, int logLevel){
        initImsdk(context, logLevel);
    }


    /**
     * 初始化imsdk
     */
    private static void initImsdk(Context context, int logLevel){
        //初始化SDK基本配置
        TIMSdkConfig config = new TIMSdkConfig(Contants.IMSDK_APPID)
                .enableCrashReport(false)
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.values()[logLevel])
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/bookadmin/justfortest/");
        TIMManager.getInstance().init(context, config);
        LogUtils.d( "initIMsdk");

    }

}
