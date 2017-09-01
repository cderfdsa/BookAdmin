package com.example.bookadmin.im;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.bookadmin.bean.UserInfo;
import com.example.bookadmin.tools.utils.LogUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

/**
 * Created by Administrator on 2017-06-19.
 */

public class UserInfoMgr {

    public class TCUserInfo {

        public String identifier;//用户的identifier
        public String nickName;//用户的昵称
        public String faceUrl;//用户头像URL
        public String selfSignature;//用户个人签名
        public TIMFriendAllowType allowType;//用户好友选项
        public TIMFriendGenderType gender;//用户性别类型
        public String location;//位置信息
        public double latitude;
        public double longitude;
    }

    private TCUserInfo mUserInfo;

    private UserInfoMgr() {
        mUserInfo = new TCUserInfo();
    }

    private static UserInfoMgr instance = new UserInfoMgr();

    public static UserInfoMgr getInstance() {
        return instance;
    }

    /**
     * 查询用户资料
     *
     * @param listener 查询结果的回调
     */
    public void queryUserInfo(final IUserInfoMgrListener listener) {
        try {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int i, String s) {
                    LogUtils.e("queryUserInfo  failed  , " + i + " : " + s);
                    if (listener != null) {
                        listener.OnQueryUserInfo(i, s);
                    }
                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    LogUtils.e("queryUserInfo  success!");
                    if (!TextUtils.isEmpty(timUserProfile.getIdentifier()))
                        mUserInfo.identifier = timUserProfile.getIdentifier();
                    if (!TextUtils.isEmpty(timUserProfile.getNickName()))
                        mUserInfo.nickName = timUserProfile.getNickName();
                    if (!TextUtils.isEmpty(timUserProfile.getFaceUrl()))
                        mUserInfo.faceUrl = timUserProfile.getFaceUrl();
                    if (!TextUtils.isEmpty(timUserProfile.getSelfSignature()))
                        mUserInfo.selfSignature = timUserProfile.getSelfSignature();

                    mUserInfo.gender = timUserProfile.getGender();
                    if (!TextUtils.isEmpty(timUserProfile.getLocation()))
                        mUserInfo.location = timUserProfile.getLocation();

                    if (listener != null) {
                        listener.OnQueryUserInfo(0, null);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置用户ID， 并使用ID向服务器查询用户信息
     * setUserId一般在登录成功之后调用，用户信息需要提供给其他类使用，或者展示给用户，因此登录成功之后需要立即向服务器查询用户信息，
     *
     * @param identifier
     * @param listener 设置结果回调
     */
    public void setUserId(final String identifier, final IUserInfoMgrListener listener) {
        mUserInfo.identifier = identifier;
        if (listener != null) {
            queryUserInfo(new IUserInfoMgrListener() {
                @Override
                public void OnQueryUserInfo(int error, String errorMsg) {
                    if (0 == error) {
                        mUserInfo.identifier = identifier;
                    } else {
                        LogUtils.e("setUserId failed:" + error + "," + errorMsg);
                    }
                    if (null != listener)
                        listener.OnSetUserInfo(error, errorMsg);
                }

                @Override
                public void OnSetUserInfo(int error, String errorMsg) {

                }
            });
        }


    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     * @param listener 设置结果回调
     */
    public void setUserNickName(final String nickName, final IUserInfoMgrListener listener) {
        if (null != mUserInfo.nickName && mUserInfo.nickName.equals(nickName)) {
            if (null != listener)
                listener.OnSetUserInfo(0, null);
            return;
        }
        mUserInfo.nickName = nickName;
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setNickname(nickName);
        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("setUserNickName failed:" + i + "," + s);
                if (null != listener)
                    listener.OnSetUserInfo(i, s);
            }

            @Override
            public void onSuccess() {
                mUserInfo.nickName = nickName;
                if (null != listener)
                    listener.OnSetUserInfo(0, null);
            }
        });
    }

    /**
     * 设置签名
     *
     * @param sign     签名
     * @param listener 设置结果回调
     */
    public void setUserSign(final String sign, final IUserInfoMgrListener listener) {
        if (mUserInfo.selfSignature.equals(sign)) {
            if (null != listener)
                listener.OnSetUserInfo(0, null);
            return;
        }
        mUserInfo.selfSignature = sign;
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setSelfSignature(sign);
        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("setUserSign failed:" + i + "," + s);
                if (null != listener)
                    listener.OnSetUserInfo(i, s);
            }

            @Override
            public void onSuccess() {
                mUserInfo.selfSignature = sign;
                if (null != listener)
                    listener.OnSetUserInfo(0, null);
            }
        });
    }

    /**
     * 设置性别
     *
     * @param sex      性别
     * @param listener 设置结果回调
     */
    public void setUserSex(final TIMFriendGenderType sex, final IUserInfoMgrListener listener) {
        if (mUserInfo.gender == sex) {
            if (null != listener)
                listener.OnSetUserInfo(0, null);
            return;
        }
        mUserInfo.gender = sex;
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setGender(sex);
        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("setUserSex failed:" + i + "," + s);
                if (null != listener)
                    listener.OnSetUserInfo(i, s);
            }

            @Override
            public void onSuccess() {
                mUserInfo.gender = sex;
                if (null != listener)
                    listener.OnSetUserInfo(0, null);
            }
        });
    }

    /**
     * 设置头像
     * 设置头像前，首先会将该图片上传到服务器存储，之后服务器返回图片的存储URL，
     * 再调用setUserHeadPic将URL存储到服务器，以后查询头像就使用该URL到服务器下载。
     *
     * @param url      头像的存储URL
     * @param listener 设置结果回调
     */
    public void setUserHeadPic(final String url, final IUserInfoMgrListener listener) {
        mUserInfo.faceUrl = url;
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(url);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("setUserHeadPic failed:" + i + "," + s);
                if (null != listener)
                    listener.OnSetUserInfo(i, s);
            }

            @Override
            public void onSuccess() {
                mUserInfo.faceUrl = url;
                if (null != listener)
                    listener.OnSetUserInfo(0, null);
            }
        });
    }

    /**
     * 设置用户定位信息
     *
     * @param location  详细定位信息
     * @param latitude  纬度
     * @param longitude 经度
     * @param listener  设置结果回调
     */
    public void setLocation(@NonNull final String location, final double latitude, final double longitude, final IUserInfoMgrListener listener) {
        if (mUserInfo.location != null && mUserInfo.location.equals(location)) {
            if (null != listener)
                listener.OnSetUserInfo(0, null);
            return;
        }
        mUserInfo.latitude = latitude;
        mUserInfo.longitude = longitude;
        mUserInfo.location = location;
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setLocation(location);
        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("setLocation failed:" + i + "," + s);
                if (null != listener)
                    listener.OnSetUserInfo(i, s);
            }

            @Override
            public void onSuccess() {
                mUserInfo.latitude = latitude;
                mUserInfo.longitude = longitude;
                mUserInfo.location = location;
                if (null != listener)
                    listener.OnSetUserInfo(0, null);
            }
        });
    }

    public String getUserIdentifier() {
        return mUserInfo.identifier;
    }

    public String getNickname() {
        return mUserInfo.nickName;
    }

    public String getHeadPic() {
        return mUserInfo.faceUrl;
    }

    public TIMFriendGenderType getSex() {
        return mUserInfo.gender;
    }

    public String getLocation() {
        return mUserInfo.location;
    }

    public void getUserInfo() {
        int sex = mUserInfo.gender == TIMFriendGenderType.Male ? 0 : 1;

        UserInfo.getInstance().setId(mUserInfo.identifier);
        UserInfo.getInstance().setNickname(mUserInfo.nickName);
        UserInfo.getInstance().setHeadPic(mUserInfo.faceUrl);
        UserInfo.getInstance().setSex(sex);
    }

    public void setUserInfo() {
        if (UserInfo.getInstance() != null) {
            if (!TextUtils.isEmpty(UserInfo.getInstance().getUserId())) {
                setUserId(UserInfo.getInstance().getId(), null);
            }
            if (!TextUtils.isEmpty(UserInfo.getInstance().getNickname())) {
                setUserNickName(UserInfo.getInstance().getNickname(), null);
            }
            if (!TextUtils.isEmpty(UserInfo.getInstance().getHeadPic())) {
                setUserHeadPic(UserInfo.getInstance().getHeadPic(), null);
            }
            final TIMFriendGenderType sexType = (UserInfo.getInstance().getSex() == 1 ? TIMFriendGenderType.Male : TIMFriendGenderType.Female);
            setUserSex(sexType, null);
        }
    }


}
