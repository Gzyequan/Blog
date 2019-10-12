package com.yequan.common.util;

import com.yequan.constant.SecurityConsts;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Auther: yq
 * @Date: 2019/7/8 08:44
 * @Description:
 */
public class MD5Util {

    /**
     * 基于commons-lang3包的加密工具
     *
     * @param text 明文
     * @param salt 盐值
     * @return
     */
    public static String encrypt(String text, String salt) {
        Logger.debug("encrypt text :{},salt :{}", text, salt);
        if (null == text) {
            return null;
        }
        return DigestUtils.md5Hex(text + salt);
    }

    /**
     * 使用默认盐值进行加密
     *
     * @param text 明文
     * @return
     */
    public static String encrypt(String text) {
        return encrypt(text, SecurityConsts.DEFAULT_MD5_SALT);
    }

    /**
     * 校验明文密文是否对应,使用自定义盐值
     *
     * @param text 明文
     * @param salt 盐值
     * @param md5  密文
     * @return
     */
    public static boolean verify(String text, String salt, String md5) {
        Logger.debug("verify text :{},salt :{},md5 :{}", text, salt, md5);
        if (null == text || null == md5) {
            return false;
        }
        String md5Code = encrypt(text, salt);
        return md5Code.equalsIgnoreCase(md5);
    }

    /**
     * 使用默认盐值
     *
     * @param text 明文
     * @param md5  密文
     * @return
     */
    public static boolean verify(String text, String md5) {
        return verify(text, SecurityConsts.DEFAULT_MD5_SALT, md5);
    }

    public static void main(String[] args) {
        //默认盐值
        String md5Code1 = encrypt("123456");
        System.out.println("123456--->>>" + md5Code1);
        //校验
        System.out.println(verify("123456", md5Code1));

        //自定义盐值
        String md5Code2 = encrypt("123456", "");
        System.out.println("123456--->>>" + md5Code2);
        //校验
        System.out.println(verify("123456", "", md5Code2));


        //"123456"无盐值的md5摘要
        String text = "e10adc3949ba59abbe56e057f20f883e";
        //自定义盐值
        String md5Code3 = encrypt(text, "");
        System.out.println("123456--->>>" + md5Code3);
        //校验
        System.out.println(verify(text, "", md5Code3));


        //"123456"默认盐值的md5摘要
        String text1 = "e10adc3949ba59abbe56e057f20f883e";
        //盐值
        String md5Code4 = encrypt(text1);
        System.out.println("e10adc3949ba59abbe56e057f20f883e--->>>" + md5Code4);
        //校验
        System.out.println(verify(text1, md5Code4));
    }

}
