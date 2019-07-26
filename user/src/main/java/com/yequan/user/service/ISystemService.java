package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: yq
 * @Date: 2019/7/25 15:56
 * @Description:
 */
public interface ISystemService {

    AppResult<String> login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    AppResult<UserDO> register(UserDO userDO);

}
