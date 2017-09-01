package com.example.bookadmin.okhttp.callback;

import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{

    public static final String TAG = "StringCallback";

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }

    @Override
    public void onError(Call call, Exception e, int id) {

        Log.i(TAG, "onError");
    }

}
