package com.yequan.common.bean;

import java.io.Serializable;

/**
 * @Auther: yq
 * @Date: 2019/7/11 11:13
 * @Description: 用于存储存放在redis中的用户信息,但是不推荐这种方式,理由如下
 *               1.增加序列化和反序列化的开销
 *               2.不方便修改,当只需要修改其中一个信息时都需要获取整个对象进行修改
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
