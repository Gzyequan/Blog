package com.yequan.common.bean;

/**
 * @Auther: yq
 * @Date: 2019/7/10 15:28
 * @Description: 用于携带token信息
 */
public class TokenPayload {

    private String mobilephone;

    private Integer userId;

    private String userAgent;

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
