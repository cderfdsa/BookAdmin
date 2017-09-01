package com.example.bookadmin.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-12.
 */

public class ShopInBook implements Serializable {

    public static final int STATE_BORROW = 1;//
    public static final int STATE_STILL = 2;

    private int state;
    private InBookBean inBookBean;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public InBookBean getInBookBean() {
        return inBookBean;
    }

    public void setInBookBean(InBookBean inBookBean) {
        this.inBookBean = inBookBean;
    }
}
