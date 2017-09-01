package com.example.bookadmin;

import com.example.bookadmin.bean.LibAddress;

/**
 * Created by Administrator on 2017-05-04.
 */

public class Contants {

    /**
     * 腾讯云服务配置专区（请自主替换）发布使用
     */
    //云通信服务相关配置
//    public static int IMSDK_ACCOUNT_TYPE = 13524;
//    public static int IMSDK_APPID = 	1400033510;
    public static int IMSDK_ACCOUNT_TYPE = 13536;
    public static int IMSDK_APPID = 	1400033550;

    //退出广播字段
    public static final String EXIT_APP = "EXIT_APP";
    public static final String NET_LOONGGG_EXITAPP = "net.loonggg.exitapp";


    public static final int AVIMCMD_TEXT_TYPE = 0;
    public static final int AVIMCMD_ENTER_LIVE = 1;          // 用户加入讨论群
    public static final int AVIMCMD_EXIT_LIVE = 2;         // 用户退出讨论群
    public static final int AVIMCMD_HOST_LEAVE = 4;         // 群主离开,
    public static final int AVIMCMD_HOST_BACK = 5;         // 群主回来,
    public static final int AVIMCMD_SET_SILENCE = 7;  // 禁言消息、设置场控消息  7 // userId，userImage,nickName,type //1、禁言，2、场控 3、取消场控
    public static final int AVIMCMD_FOLLOW = 8;           // 关注    8
    public static final int AVIMCMD_SYSTEM_NOTIFY = 10;         // 官方提示消息   10
    public static final int AVIMCMD_SHOW_INFO = 17;       // 显示消息  message ,color ,type （1 评论列中显示， 2 评论之上显示）
    public static final int AVIMCMD_DANMU = 19;       // 回调状态消息。
    public static final int AVIMCMD_LIVE_END = 20;       // 结束

    public static final String ADDRESS = "10";

    public static final float aspectRatio = 0.7f;

    public static int displayWidth;
    public static int displayHeight;

    public static int griditemwidth;
    public static int griditempadding = 40;

    public static final String KEY = "123456";

    public static final String USER_JSON = "user_json";
    public static final String APIADDRESS_JSON = "apiAddress_json";
    public static final String SEARCH_JSON = "search_json";

    public static final String DES_KEY = "BookAdmin_123456";

    public static final String PAGE = "pege";
    public static final String SEARCH = "search";
    public static final String BORROW_LIST = "borrow_list";

    public static final int REQUEST_CODE = 0;
    public static final int REQUEST_LOGIN_CODE = 20;
    public static final int REQUEST_REG_CODE = 21;
    public static final int REQUEST_FORGET_ONE = 211;
    public static final int REQUEST_FOTGET_TWO = 212;
    public static final int REQUEST_LOGIN_FORGET_CODE = 22;
    public static final int REQUEST_STILL = 25;
    public static final int RESULT_CODE_LOGIN_REG = 250;
    public static final int RESULT_CODE_REG = 251;
    public static final int RESULT_CODE_SECOND_REG = 252;

    public static final int RESULT_FORGET_SECOND = 271;
    public static final int RESULT_VAILT = 272;

    public static final int RESULT_FORGET = 29;
    public static final int REQUEST_ORDER_CODE = 30;
    public static final int RESULT_ORDER_CODE = 31;

    public static final String MAP_ADDRESS = "map_address";
    public static final String CLOCK_DATE = "clock_date";
    public static final String CLOCK_TIME = "clock_time";
    public static final int REQUEST_MAP = 54;
    public static final int MAP_RESULTCODE = 55;
    public static final int REQUEST_LOGIN = 56;
    public static final int LOGIN_RESULTCODE = 571;
    public static final int LOGIN_RESULTERRORCODE = 572;
    public static final int REQUEST_CLOCK = 58;
    public static final int CLOCK_RESULTCODE = 59;
    public static final int REQUEST_LIB = 60;
    public static final int LIB_RESULTCODE = 61;

    public static final String BROADCAST_ACTION = "order";

    public static final String BS_ID = "bs_id";

    public static final String ORDER_CODE_IN = "2";//还书
    public static final String ORDER_CODE_OUT = "1";//借书

    public static class API {
        public static final String HTTP = "http";
//        public static final String IP = "192.168.0.111";
//        public static final String IP = "192.168.0.15";
//        public static final String IP = "byu2941720001.my3w.com";
        public static final String IP = "jieyue.zhushiyun.com";
        public static final String IP_UTL = API.HTTP + "://" + API.IP;
        public static final String BASE_URL = API.IP_UTL + "/Bookadmin/Admin/Api/";
        public static final String RECOMMEND = "first_type";
        public static final String USERADD = "user_add";
        public static final String USER_NAMEPHONE = "user_namephone";
        public static final String SAVE = "user_save_pass";
        public static final String PASS = "user_save_word";
        public static final String USERLOGIN = "user_login";
        public static final String BOOKSTYPE = "bookstype";
        public static final String BOOKORDER = "bookorder";
        public static final String SCREEN = "screen";
        public static final String MYREAD = "myread";
        public static final String BOOK = "books";
        public static final String INBOOK = "inbook";
        public static final String BOOK_HTML = "books_html";
        public static final String APILIBRARY = "apilibrary";
        public static final String APIADDRESS = "apiaddress";//changyong
        public static final String COMGROPSHOP = "comgrogshop";
        public static final String CAUSER = "causer";
        public static final String ORAPIOUT = "orapiout";//借书
        public static final String ORAPIN = "orapin";//还书
        public static final String IS_NUMBER_BOOK = "is_number_book";//还书
        public static final String ORDEROUT_USER = "orderout_user";
        public static final String ORDERIN_USER = "orderin_user";//还书单
        public static final String ORDER_DETAIL = "order_detail";
        public static final String CASEPASS = "casepass";
        public static final String GROGSHOP_TIME = "grogshop_time";

        public static final String MOB_SMS_APPKEY = "1db6179565cbf";
        public static final String MOB_SMS_APPSECRECT = "7d69205891cb2e81d99071d4d04be3a8";


    }

    public static String getLibAddressId(){
        LibAddress libAddress = BookApplication.getInstance().getLibAddress();
        if(libAddress != null) {
            String libAddressId = libAddress.getL_id();
            if (libAddressId != null) {
                return Contants.ADDRESS;
//                return libAddressId;
            } else {
                return Contants.ADDRESS;
            }
        }else {
            return Contants.ADDRESS;
        }
    }

}
