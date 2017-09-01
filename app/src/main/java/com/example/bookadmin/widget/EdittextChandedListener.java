package com.example.bookadmin.widget;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Administrator on 2017-05-08.
 */

public class EdittextChandedListener implements TextWatcher {

    private Handler handler;

    public EdittextChandedListener(Handler handler){
        this.handler = handler;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        sendMsg(0);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        sendMsg(1);
    }

    @Override
    public void afterTextChanged(Editable s) {
        sendMsg(2);
    }

    private void sendMsg(int type){
        if(handler != null){
            handler.sendEmptyMessage(type);
        }
    }
}
