package com.yequan.user.controller;

import com.yequan.common.application.AppConstant;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import com.yequan.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Auther: yq
 * @Date: 2019/7/4 14:24
 * @Description:
 */
@RestController
public class LoginController {

    @Autowired
    private IUserService iUserService;

    @PostMapping(value = "/login",produces = "application/json;charset=UTF-8")
    public AppResult<String> login(HttpSession session, @RequestBody UserDTO userDTO) {
        try {
            if (null != userDTO && userDTO.getMobilephone() != null) {
                UserDO user = iUserService.loginCheck(userDTO);
                if (null != user) {
                    Integer status = user.getStatus();
                    if (status == AppConstant.UserConstant.USER_NORMAL.getStatus()) {
                        //将user写入session中
                        session.setAttribute(AppConstant.SessionConstant.SESSION_USER_KEY, user);
                        return AppResultBuilder.success("登录成功", ResultCode.SUCCESS);
                    } else if (status == AppConstant.UserConstant.USER_ILLEGAL.getStatus()) {
                        return AppResultBuilder.failure(ResultCode.USER_ACCOUNT_FORBIDDEN);
                    } else {
                        return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
                    }
                }
            } else {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR);
    }

}
