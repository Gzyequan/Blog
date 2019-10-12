package com.yequan.common.service;


import com.yequan.common.application.response.AppResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 10:18
 * @Description:
 */
public interface TokenService {

    AppResult createToken();

    boolean checkToken(HttpServletRequest request);

}
