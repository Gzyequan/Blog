package com.yequan.user.pojo;

/**
 * @Auther: yq
 * @Date: 2019/7/5 11:24
 * @Description:
 */
public class UserDTO {

    private Integer id;

    private String mobilephone;

    private String password;

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
}
