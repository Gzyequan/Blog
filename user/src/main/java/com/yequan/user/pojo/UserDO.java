package com.yequan.user.pojo;

import java.util.Date;

public class UserDO {
    private Integer id;

    private String nickname;

    private String realname;

    private Integer age;

    private String mobilephone;

    private String address;

    private Date birthday;

    private String password;

    private Integer status;

    public UserDO(Integer id, String nickname, String realname, Integer age, String mobilephone, String address, Date birthday, String password, Integer status) {
        this.id = id;
        this.nickname = nickname;
        this.realname = realname;
        this.age = age;
        this.mobilephone = mobilephone;
        this.address = address;
        this.birthday = birthday;
        this.password = password;
        this.status = status;
    }

    public UserDO() {
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

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}