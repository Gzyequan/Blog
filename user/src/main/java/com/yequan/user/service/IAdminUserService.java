package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.dbo.SysUserDO;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/26 11:21
 * @Description:
 */
public interface IAdminUserService {

    AppResult<List<SysUserDO>> listUsers(Integer pageNum, Integer pageSize);

    AppResult<SysUserDO> getUserById(Integer id);

    AppResult<SysUserDO> updateUser(Integer id, SysUserDO sysUserDO);

    AppResult deleteUserById(Integer id);

}
