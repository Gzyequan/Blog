package com.yequan.common.application.constant;

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
     * 超时时间:秒
     */
    public static final int REDIS_EXPIRE_SECOND = 15 * 60;

}
