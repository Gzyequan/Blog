package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.UserDO;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/26 11:21
 * @Description:
 */
public interface IAdminUserService {

    AppResult<List<UserDO>> listUsers(Integer pageNum, Integer pageSize);

    AppResult<UserDO> getUserById(Integer id);

    AppResult<UserDO> updateUser(Integer id,UserDO userDO);

    AppResult deleteUserById(Integer id);

}
