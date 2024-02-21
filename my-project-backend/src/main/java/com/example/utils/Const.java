package com.example.utils;

/**
 *
 * Created by 刘千山 on 2023/8/8/008-22:12
 */
public class Const {
    //请求自定义属性
    public final static String ATTR_USER_ID = "userId";

    //JWT令牌
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";
    public static final String JWT_FREQUENCY = "jwt:frequency:";

    // 邮件验证码
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";

    // 过滤器优先级
    public static final int ORDER_LIMIT = -101;
    public static final int ORDER_CORS = -102;

    /**
     * 请求频率限制
     */
    public static final String FLOW_LIMIT_COUNTER="flow:counter:";
    public static final String FLOW_LIMIT_BLOCK="flow:block:";
    // 消息队列
    public final static String MQ_MAIL = "mail";

    //用户角色
    public final static String ROLE_DEFAULT = "user";
    // 论坛相关
    public final static String FORUM_WEATHER_CACHE = "weather:cache:";
    public final static String FORUM_IMAGE_COUNTER = "forum:image:";
    public final static String FORUM_TOPIC_CREATE_COUNTER = "forum:topic:create:";
    public final static String FORUM_TOPIC_COMMENT_COUNTER = "forum:topic:comment:";
    public final static String FORUM_TOPIC_PREVIEW_CACHE = "forum:topic:preview:cache";

}
