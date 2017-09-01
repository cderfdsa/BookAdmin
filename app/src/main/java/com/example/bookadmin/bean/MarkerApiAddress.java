package com.example.bookadmin.bean;

import com.amap.api.maps.model.Marker;

/**
 * Created by Administrator on 2017-06-08.
 */

public class MarkerApiAddress {

    private Marker marker;
    private ApiAddress apiAddress;
    private boolean isLast;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public ApiAddress getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(ApiAddress apiAddress) {
        this.apiAddress = apiAddress;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
