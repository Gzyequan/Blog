package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.entity.SysPermissionDO;
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
     * 获取pmnId下一级(不做递归查所有)的所有权限
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
    @GetMapping(value = "listdeep/{pmnId}", produces = "application/json;charset=UTF-8")
    public AppResult<List<SysPermissionDO>> listDeepSysPermissions(@PathVariable("pmnId") Integer pmnId) {
        return iAdminPermissionService.listDeepSysPermissions(pmnId);
    }

    /**
     * 新增一个权限
     *
     * @param sysPermission
     * @return
     */
    @PostMapping(produces = "application/json;charset=UTF-8")
    public AppResult<Void> addOneSysPermission(@RequestBody SysPermissionDO sysPermission) {
        return iAdminPermissionService.createOneSysPermission(sysPermission);
    }

    /**
     * 批量新增权限
     *
     * @param sysPermissions
     * @return
     */
    @PostMapping(value = "batch", produces = "application/json;charset=UTF-8")
    public AppResult<Void> addBatchSysPermission(@RequestBody List<SysPermissionDO> sysPermissions) {
        return iAdminPermissionService.createSysPermissionBatch(sysPermissions);
    }

    /**
     * 删除权限资源
     *
     * @param pmnId
     * @return
     */
    @DeleteMapping(value = "{pmnId}", produces = "application/json;charset=UTF-8")
    public AppResult<Void> deleteSysPermissionById(@PathVariable("pmnId") Integer pmnId) {
        return iAdminPermissionService.deleteSysPermissionById(pmnId);
    }

}
