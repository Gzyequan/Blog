package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.entity.SysRoleDO;
import com.yequan.user.service.IAdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: yq
 * @Date: 2019/10/14 16:12
 * @Description:
 */
@RestController
@RequestMapping("admin/role/")
public class AdminRoleController {

    @Autowired
    private IAdminRoleService iAdminRoleService;

    @GetMapping(value = "{roleId}", produces = "application/json;charset=UTF-8")
    public AppResult<SysRoleDO> getSysRoleById(@PathVariable("roleId") Integer roleId) {
        return iAdminRoleService.getSysRoleById(roleId);
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public AppResult<Void> insertOneSysRole(@RequestBody SysRoleDO sysRoleDO) {
        return iAdminRoleService.insertOneSysRole(sysRoleDO);
    }

    @DeleteMapping(value = "{roleId}", produces = "application/json;charset=UTF-8")
    public AppResult<Void> deleteSysRoleById(@PathVariable("roleId") Integer roleId) {
        return iAdminRoleService.deleteSysRoleById(roleId);
    }

    @PutMapping(value = "{roleId}")
    public AppResult<Void> updateSysRole(@PathVariable("roleId") Integer roleId, @RequestBody SysRoleDO sysRoleDO) {
        return iAdminRoleService.updateSysRole(roleId, sysRoleDO);
    }

    @PostMapping(value = "{roleId}", produces = "application/json;charset=UTF-8")
    public AppResult<Void> grantAuthorityToRole(@PathVariable("roleId") Integer roleId,
                                                @RequestBody String permissionIds) {
        return iAdminRoleService.grantAuthorityToRole(roleId, permissionIds);
    }

}
