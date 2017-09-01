package com.example.bookadmin.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-10.
 */

public class InBookBean implements Serializable{
    private String ub_user;//	用户id
    private String ub_book;//图书id
    private int ub_code;//图书状态（0=还、1=借、2=续）
    private String b_id;//	图书编号
    private String isbn;//图书编码
    private String b_rackmin;//	书架区域ID
    private String b_booksid;//同一图书id
    private int b_code;//	图书状态（0在库）
    private String b_isnot;//是否生效（生效1）
    private String b_isdel;//是否删除
    private String b_intime;//录入时间
    private String b_comintime;//借书还书时间
    private String b_uptime;//更新时间
    private String b_admin;//录入管理员
    private String bs_id;//图书编号
    private String bs_name;//图书名字
    private String bs_price;//价格
    private String bs_author;//作者名称
    private String bs_typeid;//类型ID
    private String bs_publish;//	出版社id
    private String bs_remake;//图书介绍
    private String bs_photo;//封面图片
    private String bs_isnot;//是否生效（生效1）
    private String bs_isdel;//是否删除
    private String bs_intime;//录入时间
    private String bs_uptime;//更新时间
    private String bs_admin;//操作者
    private String bs_title;//	主题
    private String bs_order;//	默认排序
    private String bs_evaluate;//评分
    private String bs_number;//借阅量

    public String getUb_user() {
        return ub_user;
    }

    public void setUb_user(String ub_user) {
        this.ub_user = ub_user;
    }

    public String getUb_book() {
        return ub_book;
    }

    public void setUb_book(String ub_book) {
        this.ub_book = ub_book;
    }

    public int getUb_code() {
        return ub_code;
    }

    public void setUb_code(int ub_code) {
        this.ub_code = ub_code;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getB_rackmin() {
        return b_rackmin;
    }

    public void setB_rackmin(String b_rackmin) {
        this.b_rackmin = b_rackmin;
    }

    public String getB_booksid() {
        return b_booksid;
    }

    public void setB_booksid(String b_booksid) {
        this.b_booksid = b_booksid;
    }

    public int getB_code() {
        return b_code;
    }

    public void setB_code(int b_code) {
        this.b_code = b_code;
    }

    public String getB_isnot() {
        return b_isnot;
    }

    public void setB_isnot(String b_isnot) {
        this.b_isnot = b_isnot;
    }

    public String getB_isdel() {
        return b_isdel;
    }

    public void setB_isdel(String b_isdel) {
        this.b_isdel = b_isdel;
    }

    public String getB_intime() {
        return b_intime;
    }

    public void setB_intime(String b_intime) {
        this.b_intime = b_intime;
    }

    public String getB_comintime() {
        return b_comintime;
    }

    public void setB_comintime(String b_comintime) {
        this.b_comintime = b_comintime;
    }

    public String getB_uptime() {
        return b_uptime;
    }

    public void setB_uptime(String b_uptime) {
        this.b_uptime = b_uptime;
    }

    public String getB_admin() {
        return b_admin;
    }

    public void setB_admin(String b_admin) {
        this.b_admin = b_admin;
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

    public String getBs_price() {
        return bs_price;
    }

    public void setBs_price(String bs_price) {
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

    public String getBs_evaluate() {
        return bs_evaluate;
    }

    public void setBs_evaluate(String bs_evaluate) {
        this.bs_evaluate = bs_evaluate;
    }

    public String getBs_number() {
        return bs_number;
    }

    public void setBs_number(String bs_number) {
        this.bs_number = bs_number;
    }
}
