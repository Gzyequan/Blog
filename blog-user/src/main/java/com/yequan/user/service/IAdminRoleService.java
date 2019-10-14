package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.entity.SysRoleDO;

/**
 * @Auther: yq
 * @Date: 2019/10/14 14:52
 * @Description:
 */
public interface IAdminRoleService {

    AppResult<SysRoleDO> getSysRoleById(Integer roleId);

    AppResult<Void> insertOneSysRole(SysRoleDO sysRoleDO);

    AppResult<Void> deleteSysRoleById(Integer roleId);

    AppResult<Void> updateSysRole(Integer roleId,SysRoleDO sysRoleDO);

    AppResult<SysRoleDO> getRoleByUserId(Integer userId);

    AppResult<Void> grantAuthorityToRole(Integer roleId,String permissions);
}
