package com.yequan.common.application.constant;

/**
 * @Auther: Administrator
 * @Date: 2019/9/1 10:10
 * @Description:
 */
public enum OSEnum {
    OS_WIN(1, "windows"), OS_LINUX(2, "linux"), OTHER(-1, "other");

    private int code;

    private String os;

    OSEnum(int code, String os) {
        this.code = code;
        this.os = os;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
