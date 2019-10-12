package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.pojo.entity.SysUserDO;
import com.yequan.user.service.IAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/8 11:03
 * @Description: 管理员角色
 */
@RestController
@RequestMapping("admin/user/")
public class AdminUserController {

    @Autowired
    private IAdminUserService iAdminUserService;

    @GetMapping(value = "list/{pageNum}/{pageSize}", produces = "application/json;charset=UTF-8")
    public AppResult<List<SysUserDO>> listUsers(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return iAdminUserService.listUsers(pageNum, pageSize);
    }

    @GetMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    public AppResult<SysUserDO> getUserById(@PathVariable("id") Integer id) {
        return iAdminUserService.getUserById(id);
    }

    /**
     * 更新用户信息
     *
     * @param id
     * @param sysUserDO
     * @return
     */
    @PutMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    public AppResult<SysUserDO> updateUser(@PathVariable("id") Integer id, @RequestBody SysUserDO sysUserDO) {
        return iAdminUserService.updateUser(id, sysUserDO);
    }

    @DeleteMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    public AppResult deleteUserById(@PathVariable("id") Integer id) {
        return iAdminUserService.deleteUserById(id);
    }


    @GetMapping(value = "online", produces = "application/json;charset=UTF-8")
    public AppResult<Void> listOnlineUsers() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
