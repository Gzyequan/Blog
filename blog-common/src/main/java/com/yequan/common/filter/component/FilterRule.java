package com.yequan.common.filter.component;

/**
 * @Auther: yq
 * @Date: 2019/7/9 13:48
 * @Description: 定义过滤规则
 */
public class FilterRule {

    /**
     * 要处理的关键字
     */
    private String key;

    /**
     * 要替换的值
     */
    private String replacement;

    /**
     * 处理的方式
     */
    private String type;

    /**
     * 正则处理方式下的正则表达式
     */
    private String regex;

    /**
     * 加密方式下的加密类型,目前只支持md5
     */
    private String encryptType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }
}
