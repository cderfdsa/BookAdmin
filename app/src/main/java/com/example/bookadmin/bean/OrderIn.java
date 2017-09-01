package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-05-25.
 */

public class OrderIn extends DetailOrderSimple{


    private Date_find date_find;
    private String[] photo;


    public Date_find getDate_find() {
        return date_find;
    }

    public void setDate_find(Date_find date_find) {
        this.date_find = date_find;
    }

    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }
}
