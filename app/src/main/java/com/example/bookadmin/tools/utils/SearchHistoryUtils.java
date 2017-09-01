package com.example.bookadmin.tools.utils;

import android.content.Context;

import com.example.bookadmin.Contants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-27.
 */

public class SearchHistoryUtils {

    public static final String index = ";";

    /**
     * 读取历史纪录
     * @param context
     * @return
     */
    public static List<String> readHistory(Context context){
        String history = PreferencesUtils.getString(context, Contants.SEARCH_JSON);
        List<String> list = new ArrayList<String>();
        if(history != null) {
            String[] histroys = history.split(index);
            if (histroys.length > 0) {
                for (int i = 0; i < histroys.length; i++) {
                    if (histroys[i] != null && histroys[i].length() > 0) {
                        list.add(histroys[i]);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 插入历史纪录
     * @param context
     * @param value
     */
    public static void insertHistory(Context context,  String value) {
        String history = PreferencesUtils.getString(context,  Contants.SEARCH_JSON);
        if(history != null){
            boolean repeat = false;
            if (history != null && history.length() > 0) {//历史记录不为空
                String[] historys = history.split(index);//得到数组
                for (int i = 0; i < historys.length; i++) {//遍历是否含有此数据，有返回true
                    if (historys[i].equals(value)) {
                        repeat = true;
                    }
                }
                if (repeat) {//不写
                    return;
                }else{
                    if (historys.length < 10) {//小于3追加到前面
                        PreferencesUtils.putString(context,  Contants.SEARCH_JSON, value + index + history);//追加到前面
                    }else{//大于3删除后面的
                        PreferencesUtils.putString(context,  Contants.SEARCH_JSON, value + index + history.substring(0, history.lastIndexOf(index)));
                    }
                }
            } else {
                PreferencesUtils.putString(context,  Contants.SEARCH_JSON, value);
            }
        }else{
            PreferencesUtils.putString(context,  Contants.SEARCH_JSON, value);
        }

    }

    /**
     * 设置历史记录长度
     * @param context
     * @param length
     */
    public static void setHistoryLength(Context context,int length){
        PreferencesUtils.putInt(context,Contants.SEARCH_JSON + "Length", length);
    }



}
