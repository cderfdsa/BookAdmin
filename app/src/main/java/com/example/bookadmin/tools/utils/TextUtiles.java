package com.example.bookadmin.tools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-05-26.
 */

public class TextUtiles {

    public static String formatRpbcode(String rpBcode) {
        String result = "";
        switch (rpBcode) {
            case "1":
                result = "约借";
                break;
            case "2":
                result = "打包";
                break;
            case "3":
                result = "出库";
                break;
            case "4":
                result = "入柜";
                break;
            case "5":
                result = "取书";
                break;
            case "6":
                result = "约还";
                break;
            case "7":
                result = "放书";
                break;
            case "8":
                result = "出柜";
                break;
            case "9":
                result = "入库";
                break;
            case "10":
                result = "拆包";
                break;
        }
        return result;
    }


    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return boolean
     */
    public static boolean checkUsername(String username) {
        String regex = "([a-zA-Z0-9]{6,12})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    /**
     * 验证手机号码
     *
     * @param phoneNumber 手机号码
     * @return boolean
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    public static boolean checkName(String str) {
        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        return m.find();
    }


}
