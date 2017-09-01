package com.example.bookadmin.bean;

/**
 * Created by Administrator on 2017-06-21.
 */

public class UserInfo {

    private String id;
    private String userSig;

    private String userId;
    private String nickname;
    private String headPic;
    private int sex;
    private String password;
    private String phone;

    private String token;
    private String sdkAppId;
    private String sdkAccountType;

    private static UserInfo ourInstance = new UserInfo();

    public static UserInfo getInstance() {
        return ourInstance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static UserInfo getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(UserInfo ourInstance) {
        UserInfo.ourInstance = ourInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public String getSdkAccountType() {
        return sdkAccountType;
    }

    public void setSdkAccountType(String sdkAccountType) {
        this.sdkAccountType = sdkAccountType;
    }
}
