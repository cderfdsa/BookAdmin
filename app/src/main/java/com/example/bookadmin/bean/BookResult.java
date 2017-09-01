package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-06-08.
 */

public class BookResult {

    private String rp_id;
    private String rp_name;
    private String bz_sn;
    private String isbn;
    private String bs_name;
    private String bs_author;
    private String pl_name;

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

    public String getBz_sn() {
        return bz_sn;
    }

    public void setBz_sn(String bz_sn) {
        this.bz_sn = bz_sn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBs_name() {
        return bs_name;
    }

    public void setBs_name(String bs_name) {
        this.bs_name = bs_name;
    }

    public String getBs_author() {
        return bs_author;
    }

    public void setBs_author(String bs_author) {
        this.bs_author = bs_author;
    }

    public String getPl_name() {
        return pl_name;
    }

    public void setPl_name(String pl_name) {
        this.pl_name = pl_name;
    }
}
