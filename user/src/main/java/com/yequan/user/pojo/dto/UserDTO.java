package com.yequan.user.pojo.dto;

import com.yequan.common.application.constant.RegexConsts;

import javax.validation.constraints.Pattern;

/**
 * @Auther: yq
 * @Date: 2019/7/5 11:24
 * @Description:
 */
public class UserDTO {

    private Integer id;

    @Pattern(regexp = RegexConsts.REGEX_MOBILE)
    private String mobilephone;

    private String password;

    private Boolean keepAlive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobilephone() {
        return mobilephone.trim();
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public String getPassword() {
        return password.trim();
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
