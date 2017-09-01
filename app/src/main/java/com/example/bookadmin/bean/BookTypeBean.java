package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-05-09.
 */

public class BookTypeBean {

    private int s_id;
    private String s_sn;//图书类型代号
    private String s_name;//图书类型名称
    private int s_parentid;//父级id
    private String s_isdel;//是否删除
    private String s_uptime;//更新时间
    private String s_admin;//操作者

    public BookTypeBean() {
    }

    public BookTypeBean(int s_id, String s_sn, String s_name, int s_parentid, String s_isdel, String s_uptime, String s_admin) {
        this.s_id = s_id;
        this.s_sn = s_sn;
        this.s_name = s_name;
        this.s_parentid = s_parentid;
        this.s_isdel = s_isdel;
        this.s_uptime = s_uptime;
        this.s_admin = s_admin;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getS_sn() {
        return s_sn;
    }

    public void setS_sn(String s_sn) {
        this.s_sn = s_sn;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public int getS_parentid() {
        return s_parentid;
    }

    public void setS_parentid(int s_parentid) {
        this.s_parentid = s_parentid;
    }

    public String getS_isdel() {
        return s_isdel;
    }

    public void setS_isdel(String s_isdel) {
        this.s_isdel = s_isdel;
    }

    public String getS_uptime() {
        return s_uptime;
    }

    public void setS_uptime(String s_uptime) {
        this.s_uptime = s_uptime;
    }

    public String getS_admin() {
        return s_admin;
    }

    public void setS_admin(String s_admin) {
        this.s_admin = s_admin;
    }
}
