package com.yequan.constant;

/**
 * @Auther: yq
 * @Date: 2019/7/29 12:55
 * @Description: 用于权限, 角色等逻辑删除的状态枚举类
 */
public enum StatusEnum {

    STATUS_ILLEGAL((byte) 0, "禁用"),
    STATUS_NORMAL((byte) 1, "正常");

    private byte status;

    private String msg;

    StatusEnum(byte status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public byte getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}
