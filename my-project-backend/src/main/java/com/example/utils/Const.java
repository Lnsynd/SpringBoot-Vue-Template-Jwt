package com.example.utils;

/**
 *
 * Created by 刘千山 on 2023/8/8/008-22:12
 */
public class Const {
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public static final int ORDER_CORS = -102;
    public static final int ORDER_LIMIT = -101;

    /**
     * ip限流   记录ip请求次数
     */
    public static final String FLOW_LIMIT_COUNTER="flow:counter:";

    /**
     * ip封禁
     */
    public static final String FLOW_LIMIT_BLOCK="flow:block:";
}
