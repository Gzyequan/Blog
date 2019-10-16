package com.yequan.pojo.dto;

import com.yequan.constant.RegexConsts;
import com.yequan.validation.annotation.DateValidator;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Auther: yq
 * @Date: 2019/10/16 16:07
 * @Description: 用于管理员创建管理员用户时传输对象
 */
public class SysUserDto {

    private Integer id;

    @NotNull
    private Integer roleId;

    @Length(max = 100)
    private String nickname;

    @NotNull
    @Length(max = 100)
    private String realname;

    @NotNull
    @Pattern(regexp = RegexConsts.REGEX_MOBILE)
    private String mobilephone;

    @NotNull
    private String password;

    private Integer status;

    @DateValidator
    private String createTime;

    @DateValidator
    private String modifyTime;

    private Integer creatorId;

    private Integer updaterId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }
}
