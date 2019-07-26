package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.UserDO;
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
@RequestMapping("system/admin/")
public class AdminUserController {

    @Autowired
    private IAdminUserService iAdminUserService;

    @RequestMapping(value = "{pageNum}/{pageSize}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<List<UserDO>> listUsers(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return iAdminUserService.listUsers(pageNum, pageSize);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> getUserById(@PathVariable("id") Integer id) {
        return iAdminUserService.getUserById(id);
    }

    /**
     * 更新用户信息
     *
     * @param id
     * @param userDO
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> updateUser(@PathVariable("id") Integer id, @RequestBody UserDO userDO) {
        return iAdminUserService.updateUser(id, userDO);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
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
