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
        Logger.debug("setUserId userId :{}", userId);
        if (null == userId) {
            return;
        }
        local.set(userId);
    }

    /**
     * 获取userId
     *
     * @return
     */
    public static Integer getUserId() {
        Logger.debug("getUserId userId:{}", local.get());
        return local.get();
    }

    /**
     * 移除userId
     */
    public static void removeUserId() {
        Logger.debug("removeUserId userId:{}", local.get());
        local.remove();
    }

}
