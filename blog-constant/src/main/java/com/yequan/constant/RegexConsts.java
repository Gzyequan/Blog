package com.yequan.constant;

/**
 * @Auther: Administrator
 * @Date: 2019/9/1 10:32
 * @Description: 正则表达式
 */
public class RegexConsts {

    // 验证手机号
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    // 验证邮箱
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    // 验证汉字
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    // 验证身份证
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    // 验证URL
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    // 验证IP地址
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    // 校验密码规则:包含大小写字母,数字,至少8位
    public static final String REGEX_PASSWORD = "[A-Za-z0-9]{8,}$";

    // 匹配手机号码中间4位
    public static final String REGEX_MOBILE_CENTER_4 = "(\\d{3})\\d{4}(\\d{4})";

}
