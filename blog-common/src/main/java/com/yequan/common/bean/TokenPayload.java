package com.yequan.common.bean;

/**
 * @Auther: yq
 * @Date: 2019/7/10 15:28
 * @Description: 用于携带token信息
 */
public class TokenPayload {

    private Integer userId;

    private String userAgent;

    private String ip;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAgent() {
        return userAgent.trim();
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
