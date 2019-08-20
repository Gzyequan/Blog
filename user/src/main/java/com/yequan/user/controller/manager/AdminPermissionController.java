package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.dbo.SysPermissionDO;
import com.yequan.user.service.IAdminPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/30 09:58
 * @Description:
 */
@RestController
@RequestMapping("admin/permission/")
public class AdminPermissionController {

    @Autowired
    private IAdminPermissionService iAdminPermissionService;

    /**
     * 获取parentCode下一级(不做递归查所有)的所有权限
     *
     * @return
     */
    @GetMapping(value = "list/{pmnId}", produces = "application/json;charset=UTF-8")
    public AppResult<List<SysPermissionDO>> listParallelSysPermissions(@PathVariable("pmnId") Integer pmnId) {
        return iAdminPermissionService.listChildrenParallelPermissions(pmnId);
    }

    /**
     * 递归获取parentCode下及其子权限中的所有权限
     *
     * @param pmnId
     * @return
     */
    @GetMapping(value = "listdeep/{pmnId}",produces = "application/json;charset=UTF-8")
    public AppResult<List<SysPermissionDO>> listDeepSysPermissions(@PathVariable("pmnId") Integer pmnId) {
        return iAdminPermissionService.listDeepSysPermissions(pmnId);
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public AppResult<Void> addOneSysPermission(@RequestBody SysPermissionDO sysPermissionDO){
        return iAdminPermissionService.insertOneSysPermission(sysPermissionDO);
    }

}
