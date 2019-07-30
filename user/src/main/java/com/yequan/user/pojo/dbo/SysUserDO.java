package com.yequan.user.pojo.dbo;

import java.util.Date;

public class SysUserDO {
    private Integer id;

    private String nickname;

    private String realname;

    private Integer age;

    private String mobilephone;

    private String address;

    private Date birthday;

    private String password;

    private Integer status;

    private Date createTime;

    private Date modifyTime;

    public SysUserDO(Integer id, String nickname, String realname, Integer age, String mobilephone, String address,
                     Date birthday, String password, Integer status, Date createTime, Date modifyTime) {
        this.id = id;
        this.nickname = nickname;
        this.realname = realname;
        this.age = age;
        this.mobilephone = mobilephone;
        this.address = address;
        this.birthday = birthday;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public SysUserDO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname == null ? null : nickname.trim();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getRealname() {
        return realname == null ? null : realname.trim();
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
        return mobilephone == null ? null : mobilephone.trim();
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public String getAddress() {
        return address == null ? null : address.trim();
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
        return password == null ? null : password.trim();
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}