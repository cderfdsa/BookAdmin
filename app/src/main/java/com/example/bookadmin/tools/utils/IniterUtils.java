package com.example.bookadmin.tools.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-06-09.
 */

public class IniterUtils {

    public static void noIntent(final Activity activity, final View view, final Dialog dialog) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(view != null) {
                    view.setVisibility(View.GONE);
                }
                ToastUtils.showToastInCenter(activity, 1, "无网络连接！", Toast.LENGTH_SHORT);
                if(dialog !=null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
