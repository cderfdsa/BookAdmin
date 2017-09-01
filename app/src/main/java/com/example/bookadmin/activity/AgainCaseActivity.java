package com.example.bookadmin.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.bookadmin.BookApplication;
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.DateBean;
import com.example.bookadmin.bean.LtTime;
import com.example.bookadmin.bean.MarkerApiAddress;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.okhttp.OkHttpUtils;
import com.example.bookadmin.okhttp.callback.StringCallback;
import com.example.bookadmin.requrest.OrderGs;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.PreferencesUtils;
import com.example.bookadmin.tools.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017-06-02.
 */

public class AgainCaseActivity extends IMBaseActivity implements AMapLocationListener, LocationSource, View.OnClickListener,
        AMap.OnMarkerClickListener, AMap.OnMyLocationChangeListener, AMap.OnMapClickListener {

    private Toolbar toolbar;
    private ImageView btnMy;
    private Button btnNew;

    private AMap aMap;
    private MapView mapView;
    public static final int ZOOM = 15;

    private List<ApiAddress> mApiAddresses;
    private List<MarkerApiAddress> markerApiAddresses;

    private ApiAddress apiAddressJson;

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private boolean isFirstIn = true;
    private LatLng lastLatLng;

    private ApiAddress mApiAddress;
    private DateBean mDateBean;
    private LtTime mLtTime;

    private String order_id;
    private String ordercode;

    private PopupWindow popupWindow;
    private boolean popIsshow;

    private boolean isSure;

    private Button bs_determine;
    private TextView gsName, gsTime, txtParticulars;
    private RelativeLayout reParticulars, locationLayout, clockLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnMy = (ImageView) findViewById(R.id.btn_my);
        btnMy.setOnClickListener(this);
        btnNew = (Button) findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);
        btnNew.setVisibility(View.GONE);

        order_id = getIntent().getStringExtra("order_Id");
        ordercode = getIntent().getStringExtra("order_code");

        setToolBarReplaceActionBar();

        mapView = (MapView) findViewById(R.id.map);//找到地图控件
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        isSure = false;
        mapInit();
        dataInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissPopWindow();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (popIsshow) {
                popIsshow = false;
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setToolBarReplaceActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void mapInit() {
        if (aMap == null) {
            aMap = mapView.getMap();

            aMap.setLocationSource(this);// 设置定位监听
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            myLocationStyle.strokeColor(Color.BLUE);
            myLocationStyle.strokeWidth(2);
            aMap.setMyLocationStyle(myLocationStyle);
            UiSettings settings = aMap.getUiSettings();
            settings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            settings.setCompassEnabled(false);//指南针显示

            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM));

            aMap.setOnMarkerClickListener(this);
            aMap.setOnMyLocationChangeListener(this);//自带定位消息监听
            aMap.setOnMapClickListener(this);
            // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
            aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE));
        }

    }

    private void dataInit() {
        OrderGs.nearby(this, new OrderGs.RequstDialogResult() {
            @Override
            public void requestDialogResult(ApiAddress apiAddress) {

            }

            @Override
            public void requestApiAddress(List<ApiAddress> apiAddresses) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM));
                mApiAddresses = apiAddresses;
                markerApiAddresses = new ArrayList<MarkerApiAddress>();
                for (ApiAddress apiAddress : apiAddresses) {
                    Marker marker = addMarkersToMap(apiAddress);
                    MarkerApiAddress markerApiAddress = new MarkerApiAddress();
                    markerApiAddress.setMarker(marker);
                    markerApiAddress.setApiAddress(apiAddress);
                    markerApiAddresses.add(markerApiAddress);
                }
                String comGropJson = PreferencesUtils.getString(AgainCaseActivity.this, Contants.APIADDRESS_JSON);
                if (comGropJson != null) {
                    apiAddressJson = GsonUtil.GsonToBean(comGropJson, ApiAddress.class);
                    if (apiAddressJson != null) {
                        for (MarkerApiAddress markerApiAddress : markerApiAddresses) {
                            ApiAddress apiAddress = markerApiAddress.getApiAddress();
                            if (apiAddressJson.equals(apiAddress)) {
                                markerApiAddress.setLast(true);
                                btnNew.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        btnNew.setVisibility(View.GONE);
                    }
                } else {
                    btnNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void requestApiError() {

            }
        });
    }

    private Marker addMarkersToMap(ApiAddress apiAddress) {
        LatLng latLng = new LatLng(apiAddress.getAd_latitude(), apiAddress.getAd_longitude());
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latLng)
                .title(apiAddress.getGs_name())
                .snippet(apiAddress.getAd_name())
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_poi_marker_pressed)))
                .setFlat(true)
                .draggable(false);
        Marker marker = aMap.addMarker(markerOption);
        return marker;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock_layout:
                if (mApiAddress == null) {
                    ToastUtils.showToastInCenter(AgainCaseActivity.this, 1, "请先选择书柜地址", Toast.LENGTH_SHORT);
                    return;
                }
                if (ordercode.equals(Contants.ORDER_CODE_IN)) {//还书
                    startClockActivity(true);
                } else if (ordercode.equals(Contants.ORDER_CODE_OUT)) {
                    startClockActivity(false);
                }
                break;
            case R.id.sure:
                if (mApiAddress == null) {
                    ToastUtils.showToastInCenter(AgainCaseActivity.this, 1, getString(R.string.filladdress), Toast.LENGTH_SHORT);
                    return;
                }
                if (mDateBean == null || mLtTime == null) {
                    ToastUtils.showToastInCenter(AgainCaseActivity.this, 1, "请先选择日期与时间", Toast.LENGTH_SHORT);
                    return;
                }
                orderBaceBox();
                break;
            case R.id.btn_my:
                isFirstIn = true;
                dismissPopWindow();
                break;
            case R.id.btn_new:
                Marker marker = null;
                for (MarkerApiAddress markerApiAddress : markerApiAddresses) {
                    if (markerApiAddress.isLast()) {
                        marker = markerApiAddress.getMarker();
                    }
                }
                if (aMap != null && marker != null) {
                    jumpPoint(marker);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
                }
                showPopupWindow(apiAddressJson);
                break;
        }
    }

    private void startClockActivity(boolean isAlso) {
        Intent intentClock = new Intent(AgainCaseActivity.this, ClockActivity.class);
        intentClock.putExtra("gs_id", mApiAddress.getGs_id());
        intentClock.putExtra("isAlso", isAlso);
        startActivityForResult(intentClock, Contants.REQUEST_CLOCK);
    }

    private void dismissPopWindow() {
        if (popIsshow) {
            popIsshow = false;
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    private void orderBaceBox() {
        String order = "";
        if (ordercode.equals(Contants.ORDER_CODE_IN)) {//还书
            order = "{\"out\":[],\"in\":[" + order_id + "]}";
        } else if (ordercode.equals(Contants.ORDER_CODE_OUT)) {
            order = "{\"out\":[" + order_id + "],\"in\":[]}";
        }
        OkHttpUtils
                .post()
                .url(Contants.API.BASE_URL + Contants.API.CAUSER)
                .addParams("token", BookApplication.getInstance().getKey())
                .addParams("number", String.valueOf(BookApplication.getInstance().getNumber()))
                .addParams("id", UserInfo.getInstance().getUserId())
                .addParams("order", order)
                .addParams("date", mDateBean.getStrDate())
                .addParams("time", mLtTime.getLt_id())
                .addParams("grogshop", String.valueOf(mApiAddress.getGs_id()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            final int code = obj.getInt("code");
                            String value = obj.getString("value");
                            String data = obj.getString("data");
                            if (code == 200) {
                                result200(data);
                            } else if (code == 546) {//全被占用或预约失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToastInCenter(AgainCaseActivity.this, 1, "全被占用或预约失败", Toast.LENGTH_SHORT);
                                    }
                                });
                            } else if (code == 511) {
                                result200(data);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        ToastUtils.showShortToast(OrderSecondActivity.this, "错误：" + code);
//                                        ToastUtils.showToastInCenter(OrderSecondActivity.this, 1, "错误：" + code, Toast.LENGTH_SHORT);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void result200(String data) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                setResult(Contants.RESULT_ORDER_CODE, intent);
                ToastUtils.showToastInCenter(AgainCaseActivity.this, 2, "书柜预定成功！", Toast.LENGTH_SHORT);
                PreferencesUtils.putString(AgainCaseActivity.this, Contants.APIADDRESS_JSON, GsonUtil.GsonString(mApiAddress));
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {

                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息

                if (isFirstIn) {
                    //设置缩放级别
                    lastLatLng = new LatLng(latitude, longitude);
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(lastLatLng));
                    mListener.onLocationChanged(amapLocation);
                    isFirstIn = false;
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        dismissPopWindow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
        }
        for (int i = 0; i < markerApiAddresses.size(); i++) {
            if (marker.equals(markerApiAddresses.get(i).getMarker())) {
                final ApiAddress apiAddress = mApiAddresses.get(i);
                showPopupWindow(apiAddress);
            }
        }
        return true;
    }

    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatlng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void showPopupWindow(final ApiAddress apiAddress) {
        mApiAddress = apiAddress;
        dismissPopWindow();
        popIsshow = true;
        View contentView = LayoutInflater.from(AgainCaseActivity.this).inflate(R.layout.map_pop_window, null);

        gsTime = (TextView) contentView.findViewById(R.id.gs_time);
        gsName = (TextView) contentView.findViewById(R.id.gs_name);
        txtParticulars = (TextView) contentView.findViewById(R.id.txt_particulars);
        reParticulars = (RelativeLayout) contentView.findViewById(R.id.re_particulars);
        reParticulars.setVisibility(View.GONE);
        locationLayout = (RelativeLayout) contentView.findViewById(R.id.location_layout);
        locationLayout.setOnClickListener(this);
        clockLayout = (RelativeLayout) contentView.findViewById(R.id.clock_layout);
        clockLayout.setOnClickListener(this);
        bs_determine = (Button) contentView.findViewById(R.id.sure);
        bs_determine.setOnClickListener(this);
        if (isSure) {
            bs_determine.setEnabled(false);
        } else {
            bs_determine.setEnabled(true);
        }
        txtParticulars.setVisibility(View.GONE);
        gsName.setText(apiAddress.getGs_name());
        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        ColorDrawable dw = new ColorDrawable(00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(toolbar);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popIsshow = false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Contants.REQUEST_CLOCK:
                if (resultCode == Contants.CLOCK_RESULTCODE) {
                    mDateBean = (DateBean) data.getSerializableExtra(Contants.CLOCK_DATE);
                    mLtTime = (LtTime) data.getSerializableExtra(Contants.CLOCK_TIME);
                    gsTime.setText(mDateBean.getStrDate() + "  " +mLtTime.getLt_starttime() + " - - - " + mLtTime.getLt_endtime());
                    reParticulars.setVisibility(View.VISIBLE);
                    txtParticulars.setText("注意：\n        书柜的使用时间为" + mDateBean.getStrDate() + "  " +
                            mLtTime.getLt_starttime() + " - " + mLtTime.getLt_endtime() + "\n        书柜预约时间未到或预约时间过期，书柜不能使用" +
                            "\n        书柜预约成功后可到我的订单里面查看书柜密码");
                }
                break;
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");
            }
        } else {
            Log.e("amap", "定位失败");
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            setLocationClient();
        }
    }

    private void setLocationClient() {
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

}
