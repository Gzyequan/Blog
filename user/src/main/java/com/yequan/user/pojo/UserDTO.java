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
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
