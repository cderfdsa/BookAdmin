package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-05-22.
 */

public class ScreenPublish {

    private String pl_id;
    private String pl_name;
    private boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
