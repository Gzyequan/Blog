package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
public interface IOrdinaryUserService {

    AppResult<UserDO> selectUserById(Integer id);

    int insertSelective(UserDO userDO);

    AppResult<UserDO> updateUser(Integer id,UserDO userDO);

    /**
     * 根据手机号查询用户
     *
     * @param userDTO
     * @return
     */
    UserDO selectByMobilephone(UserDTO userDTO);

    /**
     * 注销用户
     * @param id
     * @return
     */
    AppResult<String> unregisterUser(Integer id);
}
