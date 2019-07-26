package com.yequan.common.application;

/**
 * @Auther: yq
 * @Date: 2019/7/5 14:33
 * @Description:
 */
public interface AppConstant {

    interface SessionConstant {
        String SESSION_USER_KEY = "user";
    }

    interface SecurityConstant {

        /**
         * MD5摘要默认盐值
         */
        String DEFAULT_MD5_SALT = "caffea";

        /**
         * md5加密类型
         */
        String ENCRYPT_MD5 = "md5";

        /**
         * 秘钥大小
         */
        int AES_KEY_SIZE = 128;

        /**
         * 签名秘钥
         */
        String BASE64_SECRET = "ZW]4l5JH[m6Lm)LaQEjpb!4E0lRaG(";

        /**
         * 超时时间:秒
         */
        int EXPIRE_SECOND = 15 * 60;

        /**
         * 用于JWT加密的密匙
         */
        String AES_KEY = "u^3y6SPER41jm*fn";
    }

    interface RedisPrefixKey {
        String REDIS_TOKEN = "token:";
        String REDIS_CURRENT_USER = "current-user:";
    }

    enum UserConstant {

        USER_DELETED(0, "注销"), USER_NORMAL(1, "正常"), USER_ILLEGAL(2, "禁用");

        private int status;

        private String msg;

        UserConstant(int status, String msg) {
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

    interface FilterConstant {
        /**
         * 简单替换,字符串替换
         */
        String FILTER_SIMPLE = "simple";

        /**
         * 正则替换
         */
        String FILTER_REGEX = "regex";

        /**
         * 加密
         */
        String FILTER_ENCRYPT = "encrypt";
    }

    interface DateConstant {

        String DATE_FORMAT_YMD = "yyyy-MM-dd";

        String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    }

}
