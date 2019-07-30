package com.yequan.user.service;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.dbo.SysPermissionDO;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/30 10:12
 * @Description:
 */
public interface IAdminPermissionService {

    AppResult<List<SysPermissionDO>> listChildrenParallelPermissions(Integer pmnCode);

    AppResult<List<SysPermissionDO>> listDeepSysPermissions(Integer pmnCode);

    AppResult<Void> insertOneSysPermission(SysPermissionDO sysPermissionDO);
}
