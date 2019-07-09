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

    interface SecurityCodeConstant {

        /**
         * MD5摘要默认盐值
         */
        String DEFAULT_MD5_SALT = "caffea";

        /**
         * md5加密类型
         */
        String ENCRYPT_MD5 = "md5";
    }

    enum UserConstant {

        USER_NORMAL(1, "正常"), USER_ILLEGAL(0, "非法"), USER_DELETED(-1, "注销");

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

}
