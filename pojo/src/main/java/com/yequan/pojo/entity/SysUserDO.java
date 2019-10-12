package com.yequan.pojo.entity;

import com.yequan.common.annotation.validator.DateValidator;
import com.yequan.common.application.constant.DateFormatConsts;
import com.yequan.common.application.constant.RegexConsts;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SysUserDO {
    private Integer id;

    @Length(max = 100)
    private String nickname;

    @NotNull
    @Length(max = 100)
    private String realname;

    @Min(0)
    @Max(150)
    private Integer age;

    @NotNull
    @Pattern(regexp = RegexConsts.REGEX_MOBILE)
    private String mobilephone;

    @Length(max = 300)
    private String address;

    @DateValidator(dateFormat = DateFormatConsts.YYYY_MM_DD)
    private String birthday;

    @NotNull
    private String password;

    private Integer status;

    @DateValidator
    private String createTime;

    @DateValidator
    private String modifyTime;

    public SysUserDO(Integer id, String nickname, String realname, Integer age, String mobilephone, String address,
                     String birthday, String password, Integer status, String createTime, String modifyTime) {
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}