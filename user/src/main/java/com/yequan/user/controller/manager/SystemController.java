package com.yequan.user.controller.manager;

import com.yequan.common.application.response.AppResult;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import com.yequan.user.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> register(@RequestBody UserDO userDO) {
        return iSystemService.register(userDO);
    }

}
