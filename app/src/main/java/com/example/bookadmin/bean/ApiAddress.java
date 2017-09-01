package com.example.bookadmin.bean;

import com.amap.api.maps.model.Marker;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-17.
 */

public class ApiAddress implements Serializable {


    private int gs_id;//   			  书柜id
    private String gs_name;//     书柜名称
    private String ad_name;//" 书柜详细地址
    private String district;//"      书柜地区
    private String city;//"  书柜城市
    private String province;//"    书柜省份
    private double ad_longitude;//"		经度
    private double ad_latitude;//	纬度

    public ApiAddress(int gs_id, String gs_name, String ad_name, String district, String city, String province, double ad_latitude, double ad_longitude) {
        this.gs_id = gs_id;
        this.gs_name = gs_name;
        this.ad_name = ad_name;
        this.district = district;
        this.city = city;
        this.province = province;
        this.ad_longitude = ad_longitude;
        this.ad_latitude = ad_latitude;
    }

    public int getGs_id() {
        return gs_id;
    }

    public void setGs_id(int gs_id) {
        this.gs_id = gs_id;
    }

    public String getGs_name() {
        return gs_name;
    }

    public void setGs_name(String gs_name) {
        this.gs_name = gs_name;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public double getAd_longitude() {
        return ad_longitude;
    }

    public void setAd_longitude(double ad_longitude) {
        this.ad_longitude = ad_longitude;
    }

    public double getAd_latitude() {
        return ad_latitude;
    }

    public void setAd_latitude(double ad_latitude) {
        this.ad_latitude = ad_latitude;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ApiAddress) {
            ApiAddress address = (ApiAddress) obj;
            return this.gs_id == address.getGs_id()
                    && this.gs_name.equals(address.getGs_name())
                    && this.ad_name.equals(address.getAd_name())
                    && this.district.equals(address.getDistrict())
                    && this.city.equals(address.getCity())
                    && this.province.equals(address.getProvince())
                    && this.ad_longitude == address.getAd_longitude()
                    && this.ad_latitude == address.getAd_latitude();
        }
        return super.equals(obj);
    }
}
