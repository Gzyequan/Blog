package com.yequan.pojo.dto;

/**
 * @Auther: yq
 * @Date: 2019/10/16 14:30
 * @Description:
 */
public class SysPermissionDto {

    private Integer id;

    private Integer parentId;

    private Byte status;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
