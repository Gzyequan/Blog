package com.yequan.common.quartz.exception;

/**
 * @Auther: yq
 * @Date: 2019/4/25 10:03
 * @Description:
 */
public class ScheduleException extends RuntimeException {

    public ScheduleException() {
    }

    public ScheduleException(Throwable cause) {
        super(cause);
    }

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }


}
