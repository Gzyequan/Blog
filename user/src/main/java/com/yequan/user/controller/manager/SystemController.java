package com.yequan.user.controller.manager;

import com.yequan.common.annotation.ApiVersion;
import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.dbo.SysUserDO;
import com.yequan.user.pojo.dto.UserDTO;
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

    @PostMapping(value = "/{version}/login", produces = "application/json;charset=UTF-8")
    @ApiVersion(1)
    public AppResult<String> login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDTO userDTO) {
        return iSystemService.login(request, response, userDTO);
    }

    @PostMapping(value = "/{version}/login", produces = "application/json;charset=UTF-8")
    @ApiVersion(2)
    public AppResult<String> loginV2(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDTO userDTO) {
        return iSystemService.login(request, response, userDTO);
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public AppResult<SysUserDO> register(@RequestBody SysUserDO sysUserDO) {
        return iSystemService.register(sysUserDO);
    }

    @GetMapping(value = "/validation/{mobilephone}", produces = "application/json;charset=UTF-8")
    public AppResult<Void> validateMobilephone(@PathVariable("mobilephone") @NotNull String mobilephone) {
        return iSystemService.validateMobilephone(mobilephone);
    }

}
