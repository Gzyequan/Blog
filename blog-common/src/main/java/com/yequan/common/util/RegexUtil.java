package com.yequan.common.util;

import com.yequan.constant.RegexConsts;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 11:06
 * @Description: 正则校验
 */
public class RegexUtil {

    /**
     * 手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        Logger.debug("isMobile mobile :{}", mobile);
        return StringUtils.isNotBlank(mobile) && Pattern.matches(RegexConsts.REGEX_MOBILE, mobile);
    }

    /**
     * 邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Logger.debug("isMobile email :{}", email);
        return StringUtils.isNotBlank(email) && Pattern.matches(RegexConsts.REGEX_EMAIL, email);
    }

    /**
     * 汉字
     *
     * @param chinese
     * @return
     */
    public static boolean isChinese(String chinese) {
        Logger.debug("isMobile chinese :{}", chinese);
        return StringUtils.isNotBlank(chinese) && Pattern.matches(RegexConsts.REGEX_CHINESE, chinese);
    }

    /**
     * 身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard) {
        Logger.debug("isMobile idCard :{}", idCard);
        return StringUtils.isNotBlank(idCard) && Pattern.matches(RegexConsts.REGEX_ID_CARD, idCard);
    }

    /**
     * URL
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Logger.debug("isMobile url :{}", url);
        return StringUtils.isNotBlank(url) && Pattern.matches(RegexConsts.REGEX_URL, url);
    }

    /**
     * IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        Logger.debug("isMobile ipAddr :{}", ipAddr);
        return StringUtils.isNotBlank(ipAddr) && Pattern.matches(RegexConsts.REGEX_IP_ADDR, ipAddr);
    }

}
