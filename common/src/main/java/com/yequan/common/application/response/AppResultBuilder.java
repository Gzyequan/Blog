package com.yequan.common.application.response;

/**
 * @Auther: yq
 * @Date: 2019/7/5 15:01
 * @Description:
 */
public class AppResultBuilder {

    /**
     * 成功不返回数据
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> AppResult<T> success(ResultCode code) {
        AppResult<T> result = new AppResult<T>();
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        return result;
    }

    /**
     * 成功，返回数据l
     *
     * @param t
     * @param code
     * @param <T>
     * @return
     */
    public static <T> AppResult<T> success(T t, ResultCode code) {
        AppResult<T> result = new AppResult<T>();
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        result.setData(t);
        return result;
    }

    /**
     * 失败，返回失败信息
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> AppResult<T> failure(ResultCode code) {
        AppResult<T> result = new AppResult<T>();
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        return result;
    }

    /**
     * 失败，返回失败信息与错误数据
     *
     * @param t
     * @param code
     * @param <T>
     * @return
     */
    public static <T> AppResult<T> failure(T t, ResultCode code) {
        AppResult<T> result = new AppResult<T>();
        result.setCode(code.getCode());
        result.setMsg(code.getMsg());
        result.setData(t);
        return result;
    }
}
