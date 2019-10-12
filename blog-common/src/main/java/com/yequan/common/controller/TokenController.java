package com.yequan.common.controller;

import com.yequan.common.application.response.AppResult;
import com.yequan.common.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 10:19
 * @Description:
 */
@RestController
@RequestMapping("token/")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "value",produces = "application/json;charset=UTF-8")
    public AppResult getToken(){
        return tokenService.createToken();
    }

}
