package com.yequan.pojo.entity;

import com.yequan.validation.annotation.DateValidator;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SysRoleDO {

    private Integer id;

    @NotNull
    @Length(max = 60)
    private String roleName;

    @DateValidator
    private String createTime;

    @DateValidator
    private String modifyTime;

    private String detail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}