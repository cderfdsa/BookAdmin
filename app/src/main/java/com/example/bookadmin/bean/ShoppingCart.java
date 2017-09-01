package com.example.bookadmin.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-11.
 */

public class ShoppingCart extends DetailBook implements Serializable{

    private int count;
    private boolean isChecked = true;
    private boolean isInvalid;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }
}
