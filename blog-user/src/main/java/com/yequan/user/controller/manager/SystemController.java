package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.pojo.dto.UserDTO;
import com.yequan.pojo.entity.SysUserDO;
import com.yequan.user.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * @Auther: yq
 * @Date: 2019/7/4 14:24
 * @Description:
 */
@RestController
public class SystemController {

    @Autowired
    private ISystemService iSystemService;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public AppResult<String> login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDTO userDTO) {
        return iSystemService.login(request, response, userDTO);
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public AppResult<SysUserDO> register(@RequestBody SysUserDO sysUserDO) {
        return iSystemService.register(sysUserDO);
    }

    @GetMapping(value = "/logout", produces = "application/json;charset=UTF-8")
    public AppResult<String> logout() {
        Integer currentUserId = CurrentUserLocal.getUserId();
        return AppResultBuilder.success("当前用户" + currentUserId + "退出成功");
    }

    @GetMapping(value = "/validation/{mobilephone}", produces = "application/json;charset=UTF-8")
    public AppResult<Void> validateMobilephone(@PathVariable("mobilephone") @NotNull String mobilephone) {
        return iSystemService.validateMobilephone(mobilephone);
    }

}
