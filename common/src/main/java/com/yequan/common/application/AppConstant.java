package com.yequan.common.application;

/**
 * @Auther: yq
 * @Date: 2019/7/5 14:33
 * @Description:
 */
public interface AppConstant {

    interface SessionConstant{
        String SESSION_USER_KEY="user";
    }

    enum  UserConstant{

        USER_NORMAL(1,"正常"),USER_ILLEGAL(0,"非法"),USER_DELETED(-1,"注销");

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

}
