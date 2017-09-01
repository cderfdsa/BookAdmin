package com.example.bookadmin.activity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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
import com.example.bookadmin.Contants;
import com.example.bookadmin.R;
import com.example.bookadmin.bean.ApiAddress;
import com.example.bookadmin.bean.MarkerApiAddress;
import com.example.bookadmin.requrest.OrderGs;
import com.example.bookadmin.tools.utils.DensityUtil;
import com.example.bookadmin.tools.utils.GsonUtil;
import com.example.bookadmin.tools.utils.PreferencesUtils;
import com.example.bookadmin.tools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-06.
 */

public class MapActivity extends IMBaseActivity implements AMapLocationListener, LocationSource, View.OnClickListener,
        AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMap.OnMyLocationChangeListener {

    private Toolbar toolbar;
    private ImageView btnMy;
    private Button btnNew;

    private LinearLayout linearMy;
    private RelativeLayout relativeMap;

    private AMap aMap;
    private MapView mapView;
    public static final int ZOOM = 15;

    private List<ApiAddress> mApiAddresses;
    private List<MarkerApiAddress> markerApiAddresses;
    private ApiAddress apiAddressJson;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private boolean isFirstIn = true;
    private LatLng lastLatLng;

    private PopupWindow popupWindow;
    private boolean popIsshow;

    private ApiAddress mApiAddress;

    private TextView gsName;
    private Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        btnMy = (ImageView) findViewById(R.id.btn_my);
        btnMy.setOnClickListener(this);
        btnNew = (Button) findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);
        btnNew.setVisibility(View.GONE);

        linearMy = (LinearLayout) findViewById(R.id.linearMy);
        relativeMap = (RelativeLayout) findViewById(R.id.relative_map);

        setToolBarReplaceActionBar();

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapInit();
        dataInit();
        linearMy.setVisibility(View.GONE);
        relativeMap.setVisibility(View.GONE);
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
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
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

            aMap.setLocationSource(this);
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            myLocationStyle.strokeColor(Color.BLUE);
            myLocationStyle.strokeWidth(2);
            aMap.setMyLocationStyle(myLocationStyle);
            UiSettings settings = aMap.getUiSettings();
            settings.setMyLocationButtonEnabled(true);
            settings.setCompassEnabled(false);

            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            aMap.setMyLocationEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM));

            aMap.setOnMarkerClickListener(this);
            aMap.setOnMyLocationChangeListener(this);
            aMap.setOnMapClickListener(this);
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
                linearMy.setVisibility(View.VISIBLE);
                relativeMap.setVisibility(View.VISIBLE);
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
                String comGropJson = PreferencesUtils.getString(MapActivity.this, Contants.APIADDRESS_JSON);
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearMy.setVisibility(View.GONE);
                        relativeMap.setVisibility(View.GONE);
                        ToastUtils.showToastInCenter(MapActivity.this, 1, "无网络连接！", Toast.LENGTH_SHORT);
                    }
                });
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
            case R.id.sure:
                PreferencesUtils.putString(MapActivity.this, Contants.APIADDRESS_JSON, GsonUtil.GsonString(mApiAddress));
                Intent intent = new Intent();
                intent.putExtra(Contants.MAP_ADDRESS, mApiAddress);
                setResult(Contants.MAP_RESULTCODE, intent);
                dismissPopWindow();
                finish();
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

    private void dismissPopWindow() {
        if (popIsshow) {
            popIsshow = false;
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                double latitude = amapLocation.getLatitude();
                double longitude = amapLocation.getLongitude();
                String address = amapLocation.getAddress();

                if (isFirstIn) {
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
        View contentView = LayoutInflater.from(MapActivity.this).inflate(R.layout.map_pop_layout, null);
        btnSure = (Button) contentView.findViewById(R.id.sure);
        gsName = (TextView) contentView.findViewById(R.id.gs_name);
        btnSure.setOnClickListener(this);
        gsName.setText(apiAddress.getGs_name());

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        ColorDrawable dw = new ColorDrawable(00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
//        popupWindow.showAsDropDown(toolbar);
        popupWindow.showAtLocation(MapActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, popupWindow.getHeight() + DensityUtil.dip2px(MapActivity.this, 110));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popIsshow = false;
            }
        });
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

    @Override
    public void onMapClick(LatLng latLng) {
        dismissPopWindow();
    }

}
