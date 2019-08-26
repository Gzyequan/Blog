package com.yequan.common.util;

/**
 * @Auther: yq
 * @Date: 2019/7/30 15:08
 * @Description:
 */
public final class CurrentUserLocal {

    private static ThreadLocal<Integer> local = new ThreadLocal<>();

    /**
     * 设置userId
     *
     * @param userId
     */
    public static void setUserId(Integer userId) {
        local.set(userId);
    }

    /**
     * 获取userId
     *
     * @return
     */
    public static Integer getUserId() {
        return local.get();
    }

    /**
     * 移除userId
     */
    public static void removeUserId() {
        local.remove();
    }

}
