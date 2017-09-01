package com.example.bookadmin.tools.deserializer;

import android.widget.PopupWindow;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017-05-22.
 */

public class UIHelper {


    public static void setPopupWindowTouchModal(PopupWindow popupWindow,
                                                boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
