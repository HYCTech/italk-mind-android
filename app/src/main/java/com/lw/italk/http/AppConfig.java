package com.lw.italk.http;

public class AppConfig {

    public static final int ENV_DEBUG = 0; //测试环境
    public static final int ENV_PRE = 1;  //预发布环境
    public static final int ENV_PRODUCT = 2;//正式环境
//    public static final String DEBUG_SERVER_UPDATE = "http://ausp-test.nubia.com/";
//    public static final String DEBUG_SERVER_UPDATE_uniquekey = "100622";
//    public static final String DEBUG_UPDATE_APP_KEY = "JyAQQal74c0f04df";
//    public static final String DEBUG_UPDATE_SECRET_KEY = "2f81eb6a55e29563";
//    public static final String RELEASE_SERVER_UPDATE = "http://ausp.server.nubia.cn/";
//    public static final String RELEASE_SERVER_UPDATE_uniquekey = "100094";
//    public static final String RELEASE_UPDATE_APP_KEY = "JyWCIh7d0ec2c1eb";
//    public static final String RELEASE_UPDATE_SECRET_KEY = "b3bd54896a095751";
//    public static final String PRE_SERVER_UPDATE = "http://ausp-test.nubia.com/";
//    public static final String PRE_SERVER_UPDATE_uniquekey = "100622";
//    public static final String PRE_UPDATE_APP_KEY = "JyAQQal74c0f04df";
//    public static final String PRE_UPDATE_SECRET_KEY = "2f81eb6a55e29563";
//    public static final String SECURITY_KEY = "a11064631280b9a2555b5864588c9993";
    //测试环境的域名
//    private static final String DEBUG_DOMAIN = "http://47.96.232.206:8888/";
//    private static final String DEBUG_DOMAIN = "http://test.zncode.com:8888/";
    private static final String DEBUG_DOMAIN = "http://47.96.126.173:8887/";
//    private static final String FILE_DOMAIN = "http://47.96.126.173:8886/";
    //正式环境域名
//    private static final String RELEASE_DOMAIN = "http://47.96.232.206:8888/";
    private static final String RELEASE_DOMAIN = "http://47.96.126.173:8887";
    //预生产环境域名
//    private static final String PRE_DOMAIN = "http://47.96.232.206:8888/";
    private static final String PRE_DOMAIN = "http://47.96.126.173:8887";


    // 上传图片前，先获取上传的认证链接
    public static final String OSS_AUTHENTICATION = "http://47.98.180.117:8887/";
    public static int VERSION_CODE;
    public static String VERSION_NAME;
    public static String DOMAIN;
    public static String FILE_DOMAIN = "http://47.96.126.173:8886/";
    public static String SERVER_UPDATE;
    public static String SERVER_UPDATE_uniquekey;
    public static String UPDATE_APP_KEY;
    public static String UPDATE_SECRET_KEY;

    //版本的编码，目前是测试，以后要改成正式
    private static int env = ENV_DEBUG;

    static {
        switch (env) {
            case AppConfig.ENV_DEBUG:
                DOMAIN = DEBUG_DOMAIN;

//                SERVER_UPDATE = DEBUG_SERVER_UPDATE;
//                SERVER_UPDATE_uniquekey = DEBUG_SERVER_UPDATE_uniquekey;
//                UPDATE_APP_KEY = DEBUG_UPDATE_APP_KEY;
//                UPDATE_SECRET_KEY = DEBUG_UPDATE_SECRET_KEY;
                break;
            case AppConfig.ENV_PRE:
                DOMAIN = PRE_DOMAIN;
//                SERVER_UPDATE = PRE_SERVER_UPDATE;
//                SERVER_UPDATE_uniquekey = PRE_SERVER_UPDATE_uniquekey;
//                UPDATE_APP_KEY = PRE_UPDATE_APP_KEY;
//                UPDATE_SECRET_KEY = PRE_UPDATE_SECRET_KEY;
                break;
            case AppConfig.ENV_PRODUCT:
                DOMAIN = RELEASE_DOMAIN;
//                SERVER_UPDATE = RELEASE_SERVER_UPDATE;
//                SERVER_UPDATE_uniquekey = RELEASE_SERVER_UPDATE_uniquekey;
//                UPDATE_APP_KEY = RELEASE_UPDATE_APP_KEY;
//                UPDATE_SECRET_KEY = RELEASE_UPDATE_SECRET_KEY;
                break;
            default:
                break;
        }
    }

    public static boolean isDebug() {
        return env != ENV_PRODUCT;
    }
}
