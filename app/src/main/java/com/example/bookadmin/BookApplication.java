package com.example.bookadmin;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.example.bookadmin.bean.LibAddress;
import com.example.bookadmin.tools.Foreground;
import com.example.bookadmin.tools.Md5Key;
import com.example.bookadmin.tools.utils.SDCardUtils;
import com.example.bookadmin.tools.UserInfoCache;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.io.File;
import java.util.Random;

/**
 * Created by Administrator on 2017-05-04.
 */

public class BookApplication extends MultiDexApplication {

    private static BookApplication mInstance;

    public static  BookApplication getInstance(){

        return  mInstance;
    }

    private Md5Key md5Key;
    private String key;
    private int number;

    private LatLng latLng;

    private LibAddress libAddress;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        if (SDCardUtils.isSDCardEnable())
            initFresco();
        else {
            Fresco.initialize(this);
        }

        Foreground.init(this);

        init();
        UserInfoCache.getUser(this);

        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });
        }

        Log.d("stl","ViewRootImpl " + android.os.Process.myPid() + " Thread: " + android.os.Process.myTid() + " name " + Thread.currentThread().getName());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
//        Multidex.install(this);
    }

    /**
     * 高级初始话Fresco。
     */
    public void initFresco() {
        // 高级初始化：
        Fresco.initialize(this, ImagePipelineConfig.newBuilder(BookApplication.this)
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(this)
                                .setBaseDirectoryPath(new File(SDCardUtils.getSDCardPath() + "bookadmin"))
                                .build()
                )
                .build()
        );
    }

    private void init() {
        md5Key = new Md5Key();

        number = new Random().nextInt(1000);
        key = md5Key.getkeyBeanofStr(Contants.KEY + number).toLowerCase();
        key = md5Key.getkeyBeanofStr(key + number).toLowerCase();
    }

    public String getKey(){
        return key;
    }

    public int getNumber(){
        return number;
    }

//    public UserEntity getUserEntity(){
//        return userEntity;
//    }

//    public void putUser(UserEntity userEntity){
//        this.userEntity = userEntity;
//        UserLocalData.putUser(this, userEntity);
//    }
//
//    public void clearUser(){
//        this.userEntity = null;
//        UserLocalData.clearUser(this);
//
//    }

//    private Intent intent;
//    public void putIntent(Intent intent){
//        this.intent = intent;
//    }
//
//    public Intent getIntent() {
//        return this.intent;
//    }

//    public void jumpToTargetActivity(Context context){
//
//        context.startActivity(intent);
//        this.intent = null;
//    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public LibAddress getLibAddress() {
        return libAddress;
    }

    public void setLibAddress(LibAddress libAddress) {
        this.libAddress = libAddress;
    }
}
