package com.yequan.constant;

/**
 * @Auther: yq
 * @Date: 2019/7/29 12:57
 * @Description:
 */
public final class SecurityConsts {

    private SecurityConsts() {
    }

    /**
     * MD5摘要默认盐值
     */
    public static final String DEFAULT_MD5_SALT = "caffea";

    /**
     * md5加密类型
     */
    public static final String ENCRYPT_MD5 = "md5";

    /**
     * 秘钥大小
     */
    public static final int AES_KEY_SIZE = 128;

    /**
     * 签名秘钥
     */
    public static final String BASE64_SECRET = "ZW]4l5JH[m6Lm)LaQEjpb!4E0lRaG(";

    /**
     * 用于JWT加密的密匙
     */
    public static final String AES_KEY = "u^3y6SPER41jm*fn";
}
