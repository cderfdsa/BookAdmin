package com.example.bookadmin.tools;

import android.content.Context;
import android.text.TextUtils;

import com.example.bookadmin.Contants;
import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.tools.utils.PreferencesUtils;

/**
 * Created by Administrator on 2017-05-10.
 */

public class UserInfoCache {

    public static final String ID = "id";
    public static final String NICKNAME = "nickname";
    public static final String USER_ID = "user_id";
    public static final String HEAD_PIC = "head_pic";
    public static final String SIG_ID = "sig_id";
    public static final String SEX = "sex";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String TOKEN = "token";
    public static final String SDK_APP_ID = "sdk_app_id";
    public static final String ADK_ACCOUNT_TYPE = "adk_account_type";

    public static void saveCache(Context context) {
        UserInfo info = UserInfo.getInstance();
        ACache.get(context).put(ID, info.getId());
        ACache.get(context).put(NICKNAME, info.getNickname());
        ACache.get(context).put(USER_ID, info.getUserId());
        ACache.get(context).put(HEAD_PIC, info.getHeadPic());
        ACache.get(context).put(SIG_ID, info.getUserSig());
        ACache.get(context).put(SEX, info.getSex() + "");
        ACache.get(context).put(PASSWORD, info.getPassword());
        ACache.get(context).put(PHONE, info.getPhone());
        ACache.get(context).put(TOKEN, info.getToken());
        ACache.get(context).put(SDK_APP_ID, info.getSdkAppId());
        ACache.get(context).put(ADK_ACCOUNT_TYPE, info.getSdkAccountType());

        if (info.getSdkAppId() != null && TextUtils.isDigitsOnly(info.getSdkAppId())) {
            Contants.IMSDK_APPID = Integer.parseInt(info.getSdkAppId());
        }
        if (info.getSdkAccountType() != null && TextUtils.isDigitsOnly(info.getSdkAccountType())) {
            Contants.IMSDK_ACCOUNT_TYPE = Integer.parseInt(info.getSdkAccountType());
        }
    }

    public static String getId(Context context) {
        return ACache.get(context).getAsString(ID);
    }

    public static String getUserId(Context context) {
        return ACache.get(context).getAsString(USER_ID);
    }

    public static String getNickname(Context context) {
        return ACache.get(context).getAsString(NICKNAME);
    }

    public static String getHeadPic(Context context) {
        return ACache.get(context).getAsString(HEAD_PIC);
    }

    public static String getSex(Context context) {
        return ACache.get(context).getAsString(SEX);
    }

    public static String getSigId(Context context) {
        return ACache.get(context).getAsString(SIG_ID);
    }

    public static String getaToken(Context context) {
        return ACache.get(context).getAsString(TOKEN);
    }

    public static String getSdkAppId(Context context) {
        return ACache.get(context).getAsString(SDK_APP_ID);
    }

    public static String getAccountType(Context context) {
        return ACache.get(context).getAsString(ADK_ACCOUNT_TYPE);
    }

    public static String getPhone(Context context) {
        return ACache.get(context).getAsString(PHONE);
    }

    public static String getPassword(Context context) {
        return ACache.get(context).getAsString(PASSWORD);
    }

    public static void getUser(Context context) {
        UserInfo.getInstance().setId(getId(context));
        UserInfo.getInstance().setUserSig(getSigId(context));
        UserInfo.getInstance().setUserId(getUserId(context));
        UserInfo.getInstance().setNickname(getNickname(context));
        UserInfo.getInstance().setHeadPic(getHeadPic(context));
        String sex = getSex(context);
        if (sex != null) {
            UserInfo.getInstance().setSex(Integer.valueOf(sex));
        }
        UserInfo.getInstance().setPassword(getPassword(context));
        UserInfo.getInstance().setPhone(getPhone(context));
    }

    public static void clearCache(Context context) {
        ACache.get(context).remove(ID);
        ACache.get(context).remove(USER_ID);
        ACache.get(context).remove(NICKNAME);
        ACache.get(context).remove(HEAD_PIC);
        ACache.get(context).remove(SEX);
        ACache.get(context).remove(SIG_ID);
        ACache.get(context).remove(PHONE);
        ACache.get(context).remove(PASSWORD);
        ACache.get(context).remove(TOKEN);
        ACache.get(context).remove(SDK_APP_ID);
        ACache.get(context).remove(ADK_ACCOUNT_TYPE);
    }

    public static void clearAddress(Context context) {

        PreferencesUtils.putString(context, Contants.APIADDRESS_JSON, "");
    }


    public static boolean needLogin(String identifier) {
        if (identifier == null)
            return true;
        return false;
    }

}
