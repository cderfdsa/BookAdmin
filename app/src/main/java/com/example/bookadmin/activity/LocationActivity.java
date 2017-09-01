package com.example.bookadmin.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.activity.logic.LoginActivity;
import com.example.bookadmin.bean.LibAddress;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.im.MyTLSService;
import com.example.bookadmin.im.PushUtil;
import com.example.bookadmin.im.init.InitBusiness;
import com.example.bookadmin.im.init.InitTIMUserConfig;
import com.example.bookadmin.im.LoginBusiness;
import com.example.bookadmin.im.SplashPresenter;
import com.example.bookadmin.im.init.TlsBusiness;
import com.example.bookadmin.im.chat.MessageEvent;
import com.example.bookadmin.interf.SplashView;
import com.example.bookadmin.requrest.LoadAddress;
import com.example.bookadmin.tools.utils.IniterUtils;
import com.example.bookadmin.tools.utils.LogUtils;
import com.example.bookadmin.tools.PermissionsChecker;
import com.example.bookadmin.tools.utils.ToastUtils;
import com.example.bookadmin.tools.UserInfoCache;
import com.example.bookadmin.widget.CustomDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;

import java.util.List;

/**
 * Created by Administrator on 2017-05-17.
 */

public class LocationActivity extends BaseActivity implements LoadAddress.RequestMainLib, SplashView, TIMCallBack {

    SplashPresenter presenter;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };


    public static final int REQUEST_CODE = 999; // 请求码
    private PermissionsChecker mPermissionsChecker; // 权限检测器


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    private boolean isFirst = false;

    private static final int REFRESH_COMPLETE = 0X153;
    //    private static final int REFRESH_LOCATION_ERROE = 0X154;
    private static final int REFRESH_START_ACTIVITY = 0X155;

    private int LOGIN_RESULT_CODE = 100;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    finish();
                    break;
//                case REFRESH_LOCATION_ERROE:
//                    finish();
//                    break;
                case REFRESH_START_ACTIVITY:
//                    startActivity(new Intent(LocationActivity.this, MainActivity.class));
                    initIM();
                    break;
            }
        }
    };

    //标志位确定SDK是否初始化，避免客户SDK未初始化的情况，实现可重入的init操作
    private static boolean isSDKInit = false;

    private void initIM() {

        init();

        presenter = new SplashPresenter(this);
        presenter.start();
    }

    private void init() {
        if (isSDKInit)
            return;
//        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
//        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext());
        //初始化TLS
        TlsBusiness.init(getApplicationContext());

//        String id =  TLSService.getInstance().getLastUserIdentifier();
//        UserInfo.getInstance().setId(id);
//        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));

        isSDKInit = true;
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            // TODO Auto-generated method stub
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    double locationType = amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    BookApplication.getInstance().setLatLng(latLng);

                    if (!isFirst) {
                        LogUtils.e("定位成功");
                        LoadAddress loadAddress = new LoadAddress(LocationActivity.this);
                        loadAddress.loadLibAdress(LocationActivity.this);
                        isFirst = true;
                    }

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.e("location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());

                    isNoConn();
                }
            }
        }
    };

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private SimpleDraweeView simpleDraweeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearNotification();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_location);
        mPermissionsChecker = new PermissionsChecker(this);

        Contants.displayWidth = getWindowManager().getDefaultDisplay().getWidth();
        Contants.displayHeight = getWindowManager().getDefaultDisplay().getHeight();

        TextView mainTitle = (TextView) findViewById(R.id.mainTitle);
        TextView subTitle = (TextView) findViewById(R.id.subTitle);
        TextView versionNum = (TextView) findViewById(R.id.versionNum);

        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        mainTitle.setTypeface(fontFace);
        mainTitle.setText("图书借阅");

        subTitle.setText("");
        versionNum.setText("v " + getVersionName(this));


//        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.drawee_view);
//        Uri uri = Uri.parse(Contants.API.IP_UTL + "/Bookadmin/Public/Apkstart/start.png");
//        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setPostprocessor(new BlurPostprocessor(this, 6))
//                .build();
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(uri)
//                .setImageRequest(request)
//                .setOldController(simpleDraweeView.getController())
//                .build();
//        simpleDraweeView.setController(controller);

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            if (IniterUtils.isNetworkAvailable(LocationActivity.this)) {

                setUpMap();
            } else {
//                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
                isNoConn();
            }
        }
    }
    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
//        MiPushClient.clearNotification(getApplicationContext());
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            if (IniterUtils.isNetworkAvailable(LocationActivity.this)) {
                LogUtils.e("开始定位");
                setUpMap();
            } else {
                ToastUtils.showToastInCenter(LocationActivity.this, 1, "当前没有网络", Toast.LENGTH_SHORT);
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
            }
        }else if(requestCode == LOGIN_RESULT_CODE){
            if(resultCode == Contants.LOGIN_RESULTCODE){
                if (0 == MyTLSService.getInstance().getLastErrno()){
                    navToHome();
                } else if (resultCode == RESULT_CANCELED){
                    finish();
                }
            }
        }
    }

    private void setUpMap() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //该方法默认为false，true表示只定位一次
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private void isNoConn() {
        CustomDialog.Builder builder = new CustomDialog.Builder(LocationActivity.this);
        builder.setMessage("网络连接不可用,是否进行设置");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
            }
        });

        builder.setNegativeButton("设置", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        });
        builder.create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void requestLibAddress(List<LibAddress> libAddresses) {
        LatLng myLatLng = BookApplication.getInstance().getLatLng();
        if (myLatLng != null) {
            int count = 0;
            float record = 0;
            for (int i = 0; i < libAddresses.size(); i++) {
                LibAddress libAddress = libAddresses.get(i);
                double la = libAddress.getL_latitude();
                double lo = libAddress.getL_longitude();
                LatLng latLng = new LatLng(la, lo);
                float distance = AMapUtils.calculateLineDistance(myLatLng, latLng);
                if (i == 0) {
                    record = distance;
                    count = i;
                } else {
                    if (distance < record) {
                        record = distance;
                        count = i;
                    }
                }
            }
            BookApplication.getInstance().setLibAddress(libAddresses.get(count));
            mHandler.sendEmptyMessageDelayed(REFRESH_START_ACTIVITY, 0);

        }
    }

    @Override
    public void requestLibAddressFaile() {
        mHandler.sendEmptyMessageDelayed(REFRESH_START_ACTIVITY, 2000);
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    @Override
    public void navToHome() {
        TIMUserConfig userConfig = InitTIMUserConfig.setTIMUserConfig(this);
        TIMManager.getInstance().setUserConfig(userConfig);
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
    }

    @Override
    public void navToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_RESULT_CODE);
    }

    @Override
    public boolean isUserLogin() {
        String id = UserInfo.getInstance().getId();
        boolean isNeed = UserInfoCache.needLogin(id);
        boolean islogin = id != null && (!isNeed);
        return islogin;
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "login error : code " + i + " " + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                showKickOutDialog(this);
                break;
            case 6200:
                ToastUtils.showToastInCenter(this, 1, "登录失败，当前无网络", Toast.LENGTH_SHORT);
                navToLogin();
                break;
            default:
                ToastUtils.showToastInCenter(this, 1, "登录失败，请稍后重试", Toast.LENGTH_SHORT);
                navToLogin();
                break;
        }
    }

    @Override
    public void onSuccess() {
        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String deviceMan = android.os.Build.MANUFACTURER;
        //注册小米和华为推送
//        if (deviceMan.equals("Xiaomi") && shouldMiInit()){
//            MiPushClient.registerPush(getApplicationContext(), "2882303761517480335", "5411748055335");
//        }else if (deviceMan.equals("HUAWEI")){
//            PushManager.requestToken(this);
//        }
        LogUtils.d( "imsdk env " + TIMManager.getInstance().getEnv());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        ToastUtils.showToastInCenter(this, 2, "登录成功", Toast.LENGTH_SHORT);
        finish();
    }

    /**
     * 显示被踢下线通知
     *
     * @param context activity
     */
    private void showKickOutDialog(final Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage("您的帐号已在其它地方登陆");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                LoginBusiness.logout(new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        if (LocationActivity.this != null){
                            Toast.makeText(LocationActivity.this, "退出登录失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess() {
                        //sendBroadcast(new Intent(Constants.EXIT_APP));
                    }
                });
            }
        });
        builder.setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                navToHome();
            }
        });
        builder.create().show();
    }

}
