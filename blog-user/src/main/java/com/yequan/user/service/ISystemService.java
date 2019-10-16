package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.dto.UserDTO;
import com.yequan.pojo.entity.SysUserDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: yq
 * @Date: 2019/7/25 15:56
 * @Description:
 */
public interface ISystemService {

    AppResult<String> login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    AppResult<SysUserDO> register(SysUserDO sysUserDO) throws RuntimeException;

    AppResult<Void> validateMobilephone(String mobilephone);
}
