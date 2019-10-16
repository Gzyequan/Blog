package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.entity.SysPermissionDO;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/30 10:12
 * @Description:
 */
public interface IAdminPermissionService {

    AppResult<List<SysPermissionDO>> listChildrenParallelPermissions(Integer pmnId);

    AppResult<List<SysPermissionDO>> listDeepSysPermissions(Integer pmnId);

    AppResult<Void> createOneSysPermission(SysPermissionDO sysPermissionDO);

    AppResult<Void> createSysPermissionBatch(List<SysPermissionDO> sysPermissions);

    /**
     * 根据id删除资源
     *
     * @param pmnId
     * @return
     */
    AppResult<Void> deleteSysPermissionById(Integer pmnId);

    List<SysPermissionDO> getSysPermissionByRoleId(Integer roleId);

    List<SysPermissionDO> getSysPermissionByUserId(Integer userId);
}
