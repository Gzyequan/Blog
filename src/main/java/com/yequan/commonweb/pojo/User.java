package com.yequan.commonweb.pojo;

import java.util.Date;

public class User {
    private Integer id;

    private String nickname;

    private String realname;

    private Integer age;

    private Integer mobilephone;

    private String address;

    private Date birthday;

    public User(Integer id, String nickname, String realname, Integer age, Integer mobilephone, String address, Date birthday) {
        this.id = id;
        this.nickname = nickname;
        this.realname = realname;
        this.age = age;
        this.mobilephone = mobilephone;
        this.address = address;
        this.birthday = birthday;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(Integer mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}