package com.example.bookadmin.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017-06-06.
 */

public class DateBean implements Serializable{

    private long dateMillis;
    private String strDate;
    private Date date;


    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
