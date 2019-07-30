package com.yequan.common.application.constant;

/**
 * @Auther: yq
 * @Date: 2019/7/29 13:39
 * @Description:
 */
public final class TokenConsts {

    private TokenConsts() {
    }

    /**
     * 过期时间15分钟(15*60*1000毫秒)
     */
    public static final long TOKEN_EXPIRE_TIME_15MINUTE = 15 * 60 * 1000;

    /**
     * 过期时间7天(7*24*60*60*1000)
     */
    public static final long TOKEN_EXPIRE_TIME_7DAY = 15 * 60 * 1000;

    public static final long TOKEN_EXPIRE_TIME_1 = 60 * 1000;

    /**
     * 过期时间30分钟
     */
    public static final long TOKEN_EXPIRE_TIME_30 = 30 * 60 * 1000;

    /**
     * token私钥
     */
    public final static String TOKEN_PRIVATE_KEY = "e10adc3949ba59abbe56e057f20f883e";

}
