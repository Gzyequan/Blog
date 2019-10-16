package com.yequan.pojo.entity;

import com.yequan.validation.annotation.DateValidator;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SysRoleDO {

    private Integer id;

    @NotNull
    @Length(max = 200)
    private String roleCode;

    @NotNull
    @Length(max = 200)
    private String roleName;

    private Integer creatorId;

    private Integer updaterId;

    @DateValidator
    private String createTime;

    @DateValidator
    private String modifyTime;

    private String detail;

    @NotNull
    private Byte reserved;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
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

    public Byte getReserved() {
        return reserved;
    }

    public void setReserved(Byte reserved) {
        this.reserved = reserved;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}