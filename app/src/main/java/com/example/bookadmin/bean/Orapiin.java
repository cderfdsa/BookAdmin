package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-05-24.
 */

public class Orapiin {


    private String rp_id;
    private String rp_name;
    private String rp_library;
    private String rp_pack;
    private String rp_bookcase;
    private String rp_user;
    private String rp_bcode;
    private String rp_intime;
    private String rp_uptime;
    private String rp_isdel;
    private String rp_admin;
    private String code;

    public String getRp_id() {
        return rp_id;
    }

    public void setRp_id(String rp_id) {
        this.rp_id = rp_id;
    }

    public String getRp_name() {
        return rp_name;
    }

    public void setRp_name(String rp_name) {
        this.rp_name = rp_name;
    }

    public String getRp_library() {
        return rp_library;
    }

    public void setRp_library(String rp_library) {
        this.rp_library = rp_library;
    }

    public String getRp_pack() {
        return rp_pack;
    }

    public void setRp_pack(String rp_pack) {
        this.rp_pack = rp_pack;
    }

    public String getRp_bookcase() {
        return rp_bookcase;
    }

    public void setRp_bookcase(String rp_bookcase) {
        this.rp_bookcase = rp_bookcase;
    }

    public String getRp_user() {
        return rp_user;
    }

    public void setRp_user(String rp_user) {
        this.rp_user = rp_user;
    }

    public String getRp_bcode() {
        return rp_bcode;
    }

    public void setRp_bcode(String rp_bcode) {
        this.rp_bcode = rp_bcode;
    }

    public String getRp_intime() {
        return rp_intime;
    }

    public void setRp_intime(String rp_intime) {
        this.rp_intime = rp_intime;
    }

    public String getRp_uptime() {
        return rp_uptime;
    }

    public void setRp_uptime(String rp_uptime) {
        this.rp_uptime = rp_uptime;
    }

    public String getRp_isdel() {
        return rp_isdel;
    }

    public void setRp_isdel(String rp_isdel) {
        this.rp_isdel = rp_isdel;
    }

    public String getRp_admin() {
        return rp_admin;
    }

    public void setRp_admin(String rp_admin) {
        this.rp_admin = rp_admin;
    }

    @Override
    public String toString() {
        return "Orapiin{" +
                "rp_id='" + rp_id + '\'' +
                ", rp_pack='" + rp_pack + '\'' +
                '}';
    }
}
