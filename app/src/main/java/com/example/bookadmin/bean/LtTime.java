package com.example.bookadmin.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-06-06.
 */

public class LtTime implements Serializable{

    private String lt_id;
    private String lt_grogshop;
    private String lt_starttime;
    private String lt_endtime;
    private String lt_intime;
    private String lt_uptime;
    private String lt_admin;
    private String lt_isnot;
    private String lt_isdel;

    public String getLt_id() {
        return lt_id;
    }

    public void setLt_id(String lt_id) {
        this.lt_id = lt_id;
    }

    public String getLt_grogshop() {
        return lt_grogshop;
    }

    public void setLt_grogshop(String lt_grogshop) {
        this.lt_grogshop = lt_grogshop;
    }

    public String getLt_starttime() {
        return lt_starttime;
    }

    public void setLt_starttime(String lt_starttime) {
        this.lt_starttime = lt_starttime;
    }

    public String getLt_endtime() {
        return lt_endtime;
    }

    public void setLt_endtime(String lt_endtime) {
        this.lt_endtime = lt_endtime;
    }

    public String getLt_intime() {
        return lt_intime;
    }

    public void setLt_intime(String lt_intime) {
        this.lt_intime = lt_intime;
    }

    public String getLt_uptime() {
        return lt_uptime;
    }

    public void setLt_uptime(String lt_uptime) {
        this.lt_uptime = lt_uptime;
    }

    public String getLt_admin() {
        return lt_admin;
    }

    public void setLt_admin(String lt_admin) {
        this.lt_admin = lt_admin;
    }

    public String getLt_isnot() {
        return lt_isnot;
    }

    public void setLt_isnot(String lt_isnot) {
        this.lt_isnot = lt_isnot;
    }

    public String getLt_isdel() {
        return lt_isdel;
    }

    public void setLt_isdel(String lt_isdel) {
        this.lt_isdel = lt_isdel;
    }
}
