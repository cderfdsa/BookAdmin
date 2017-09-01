package com.example.bookadmin.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-11.
 */

public class DetailBook implements Serializable{

    private String bl_library;//	库房id
    private String bl_books;//	书名id
    private String bl_nice;//是否推荐
    private String bs_id;//图书编号
    private String bs_name;//图书名字
    private float bs_price;//价格
    private String bs_author;//	作者名称
    private String bs_typeid;//类型ID
    private String bs_publish;//	出版社id
    private String bs_remake;//	图书介绍
    private String bs_photo;//封面图片
    private String bs_isnot;//是否生效（生效1）
    private String bs_isdel;//是否删除
    private String bs_intime;//录入时间
    private String bs_uptime;//更新时间
    private String bs_admin;//操作者
    private String bs_title;//主题
    private String bs_order;//顺序
    private String pl_id;//出版社id
    private String pl_name;//出版社名称
    private String pl_remake;//出版社备注
    private String pl_isdel;//是否删除
    private String pl_intime;//录入时间
    private String pl_uptime;//更新时间
    private String pl_admin;//管理id
    private String bs_evaluate;//评分
    private int number;

    public DetailBook() {
    }

    public String getBl_library() {
        return bl_library;
    }

    public void setBl_library(String bl_library) {
        this.bl_library = bl_library;
    }

    public String getBl_books() {
        return bl_books;
    }

    public void setBl_books(String bl_books) {
        this.bl_books = bl_books;
    }

    public String getBl_nice() {
        return bl_nice;
    }

    public void setBl_nice(String bl_nice) {
        this.bl_nice = bl_nice;
    }

    public String getBs_id() {
        return bs_id;
    }

    public void setBs_id(String bs_id) {
        this.bs_id = bs_id;
    }

    public String getBs_name() {
        return bs_name;
    }

    public void setBs_name(String bs_name) {
        this.bs_name = bs_name;
    }

    public float getBs_price() {
        return bs_price;
    }

    public void setBs_price(float bs_price) {
        this.bs_price = bs_price;
    }

    public String getBs_author() {
        return bs_author;
    }

    public void setBs_author(String bs_author) {
        this.bs_author = bs_author;
    }

    public String getBs_typeid() {
        return bs_typeid;
    }

    public void setBs_typeid(String bs_typeid) {
        this.bs_typeid = bs_typeid;
    }

    public String getBs_publish() {
        return bs_publish;
    }

    public void setBs_publish(String bs_publish) {
        this.bs_publish = bs_publish;
    }

    public String getBs_remake() {
        return bs_remake;
    }

    public void setBs_remake(String bs_remake) {
        this.bs_remake = bs_remake;
    }

    public String getBs_photo() {
        return bs_photo;
    }

    public void setBs_photo(String bs_photo) {
        this.bs_photo = bs_photo;
    }

    public String getBs_isnot() {
        return bs_isnot;
    }

    public void setBs_isnot(String bs_isnot) {
        this.bs_isnot = bs_isnot;
    }

    public String getBs_isdel() {
        return bs_isdel;
    }

    public void setBs_isdel(String bs_isdel) {
        this.bs_isdel = bs_isdel;
    }

    public String getBs_intime() {
        return bs_intime;
    }

    public void setBs_intime(String bs_intime) {
        this.bs_intime = bs_intime;
    }

    public String getBs_uptime() {
        return bs_uptime;
    }

    public void setBs_uptime(String bs_uptime) {
        this.bs_uptime = bs_uptime;
    }

    public String getBs_admin() {
        return bs_admin;
    }

    public void setBs_admin(String bs_admin) {
        this.bs_admin = bs_admin;
    }

    public String getBs_title() {
        return bs_title;
    }

    public void setBs_title(String bs_title) {
        this.bs_title = bs_title;
    }

    public String getBs_order() {
        return bs_order;
    }

    public void setBs_order(String bs_order) {
        this.bs_order = bs_order;
    }

    public String getPl_id() {
        return pl_id;
    }

    public void setPl_id(String pl_id) {
        this.pl_id = pl_id;
    }

    public String getPl_name() {
        return pl_name;
    }

    public void setPl_name(String pl_name) {
        this.pl_name = pl_name;
    }

    public String getPl_remake() {
        return pl_remake;
    }

    public void setPl_remake(String pl_remake) {
        this.pl_remake = pl_remake;
    }

    public String getPl_isdel() {
        return pl_isdel;
    }

    public void setPl_isdel(String pl_isdel) {
        this.pl_isdel = pl_isdel;
    }

    public String getPl_intime() {
        return pl_intime;
    }

    public void setPl_intime(String pl_intime) {
        this.pl_intime = pl_intime;
    }

    public String getPl_uptime() {
        return pl_uptime;
    }

    public void setPl_uptime(String pl_uptime) {
        this.pl_uptime = pl_uptime;
    }

    public String getPl_admin() {
        return pl_admin;
    }

    public void setPl_admin(String pl_admin) {
        this.pl_admin = pl_admin;
    }

    public String getBs_evaluate() {
        return bs_evaluate;
    }

    public void setBs_evaluate(String bs_evaluate) {
        this.bs_evaluate = bs_evaluate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
