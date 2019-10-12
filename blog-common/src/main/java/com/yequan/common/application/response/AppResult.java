package com.yequan.common.application.response;

/**
 * @Auther: yq
 * @Date: 2019/7/5 14:59
 * @Description: 统一的返回格式
 */
public class AppResult<T> {

    private int code;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
