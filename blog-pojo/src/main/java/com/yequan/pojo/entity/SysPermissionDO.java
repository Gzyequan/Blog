package com.yequan.pojo.entity;

import com.yequan.validation.annotation.PermissionTypeValidator;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SysPermissionDO {
    private Integer id;

    @NotNull
    private Integer parentId;

    @NotNull
    @Length(max = 200)
    private String pmnName;

    @NotNull
    @PermissionTypeValidator
    private String type;

    private Byte status;

    private Integer creatorId;

    private Integer updaterId;

    private String createTime;

    private String modifyTime;

    @Length(max = 200)
    private String detail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPmnName() {
        return pmnName;
    }

    public void setPmnName(String pmnName) {
        this.pmnName = pmnName == null ? null : pmnName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
}