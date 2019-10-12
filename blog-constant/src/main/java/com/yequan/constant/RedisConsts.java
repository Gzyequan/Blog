package com.yequan.constant;

/**
 * @Auther: yq
 * @Date: 2019/7/29 12:53
 * @Description:
 */
public final class RedisConsts {

    private RedisConsts() {
    }

    public static final String REDIS_TOKEN = "token:";
    public static final String REDIS_CURRENT_USER = "current-user:";
    /**
     * 超时时间:15分钟(15*60 秒)
     */
    public static final int REDIS_EXPIRE_15_MINUTE = 15 * 60;

    /**
     * 超市时间:1分钟(60秒)
     */
    public static final int REDIS_EXPIRE_1_MINUTE = 60;


}
