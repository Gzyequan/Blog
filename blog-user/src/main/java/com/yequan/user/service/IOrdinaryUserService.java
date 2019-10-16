package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.dto.UserDTO;
import com.yequan.pojo.entity.SysUserDO;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
public interface IOrdinaryUserService {

    AppResult<SysUserDO> getUserById(Integer id);

    int insertSelective(SysUserDO sysUserDO);

    AppResult<SysUserDO> updateUser(Integer id, SysUserDO sysUserDO);

    /**
     * 根据手机号查询用户
     *
     * @param mobilephone
     * @return
     */
    SysUserDO selectByMobilephone(String mobilephone);

    /**
     * 注销用户
     * @param id
     * @return
     */
    AppResult<String> unregisterUser(Integer id);
}
