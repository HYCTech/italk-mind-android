package com.lw.italk.http;

/**
 * Created by lfei on 15-4-14.
 */
public interface ErrorCode {
    int SUCCESS = 0;
    int UNDEFINE = 1000;//未知的系统错误
    int SERVER_BUSY = 1001;//服务器繁忙，请稍后再试
    int TOKEN_INVALID = 71000;//账户token无效，校验失败
    int DATA_FORMAT_ERROR = 1003;//	数据格式错误
    int LOCAL_TIME_ERROR = 1004;//手机端时间与服务器时间差异较大，请调整为北京时间
    int ACCOUNT_OR_PSW_ERROR = 2001;//账户或密码错误
    int ACCOUNT_NOT_EXIST = 2002;//手机号未注册
    int CODE_EXPIRES = 2003;//验证码过期
    int CODE_ERROR = 2004;//验证码错误
    int IMEI_ERROR = 3001;//IMEI状态不对
    int IMEI_NOT_EXIST = 3002;//	IMEI不存在
    int IMEI_NOT_IN_ACCOUNT = 3003;//IMEI不在所属客户下
    int INVENTORY_NOT_ENOUGH = 3004;//库存不足
    int SALE_ORDER_ERROR = 3005;//	销售单状态不对


    int FILE_NOT_LOGIN = 60000; //用户尚未登录
    int FILE_TIMEOUT = 60001;

}
