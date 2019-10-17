package com.yequan.base.service;

/**
 * @Auther: yq
 * @Date: 2019/10/17 10:24
 * @Description: dto转换成do, bo的统一接口
 */
public interface DTOConvert<S, T> {

    T convert(S s);

}
