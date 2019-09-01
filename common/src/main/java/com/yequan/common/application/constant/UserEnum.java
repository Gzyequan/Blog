package com.yequan.common.application.constant;

/**
 * @Auther: yq
 * @Date: 2019/7/29 12:55
 * @Description:
 */
public enum UserEnum {

    USER_DELETED(0, "注销"),
    USER_NORMAL(1, "正常"),
    USER_ILLEGAL(2, "禁用");

    private int status;

    private String msg;

    UserEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}
