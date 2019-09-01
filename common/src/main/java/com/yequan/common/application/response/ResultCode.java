package com.yequan.common.application.response;

/**
 * @Auther: yq
 * @Date: 2019/7/5 15:00
 * @Description: 统一状态码
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(0, "成功"),
    /* 内部错误 */
    ERROR(-1, "内部错误"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户,角色,权限错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_INFO_ERROR(20002, "账号不存在或密码错误"),
    USER_LOGIN_ERROR(20003, "登录错误"),
    USER_LOGIN_ILLEGAL(20004, "重复登录"),
    USER_LOGIN_REMOTE(20005, "异地登录"),
    USER_LOGIN_EXPIRED(20006, "登录过期"),
    USER_ACCOUNT_FORBIDDEN(20007, "账号已被禁用"),
    USER_NOT_EXIST(20008, "用户不存在"),
    USER_HAS_EXISTED(20009, "用户已存在"),
    USER_MOBILE_EXISTED(20010, "手机号已被注册"),
    USER_CREATE_ERROR(20011, "新增用户失败"),
    USER_UPDATE_ERROR(20012, "更新用户失败"),
    USER_DELETE_ERROR(20013, "删除用户失败"),
    USER_UNREGISTER_ERROR(20014, "注销用户失败"),
    PERMISSION_CREATE_ERROR(20020, "新增权限失败"),
    PERMISSION_TYPE_ERROR(20021, "权限类型错误"),

    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "某业务出现问题"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /* 数据错误：50001-599999 */
    RESULT_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),
    INTERFACE_REQUEST_FREQUENT(60007, "请求频繁"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限");


    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
