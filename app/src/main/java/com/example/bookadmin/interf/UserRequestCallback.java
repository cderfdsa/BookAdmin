package com.example.bookadmin.interf;

/**
 * Created by zt on 2017/5/6.
 */

public interface UserRequestCallback {
//    public void handleQueryResult(T t, int code);
    public void handleQueryResult(int code);
    public void handleQueryResultError();
    public void handleQueryError();
}
