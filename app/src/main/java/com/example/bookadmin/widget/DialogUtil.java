package com.example.bookadmin.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2017-06-20.
 */

public class DialogUtil {

    public static void showComfirmDialog(final Context context, String msg, DialogInterface.OnClickListener confirmListener,
                                         DialogInterface.OnClickListener cancelListener) {

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", confirmListener);
        builder.setNegativeButton("取消", cancelListener);
        builder.create().show();

    }

    public static void showMsgDialog(Context context, String msg, DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", confirmListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
