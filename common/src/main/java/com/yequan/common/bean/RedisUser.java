package com.yequan.common.bean;

import java.io.Serializable;

/**
 * @Auther: yq
 * @Date: 2019/7/11 11:13
 * @Description:
 */
public class RedisUser implements Serializable {

    private Integer id;

    private String token;

    private long expireTime;

    public RedisUser(Integer id, String token, long expireTime) {
        this.id = id;
        this.token = token;
        this.expireTime = expireTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
