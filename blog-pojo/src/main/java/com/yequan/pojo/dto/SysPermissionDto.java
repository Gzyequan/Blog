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

    //https://gitbook.cn/books/5d9054898e7d0276851ea85f/index.html#dto-1
    //http://lrwinx.github.io/2016/04/28/%E5%A6%82%E4%BD%95%E4%BC%98%E9%9B%85%E7%9A%84%E8%AE%BE%E8%AE%A1java%E5%BC%82%E5%B8%B8/

}
