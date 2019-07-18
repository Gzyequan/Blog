package com.yequan.user.controller;

import com.alibaba.fastjson.JSON;
import com.yequan.common.application.AppConstant;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.bean.RedisUser;
import com.yequan.common.bean.TokenPayload;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.AESUtil;
import com.yequan.common.util.JwtUtil;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import com.yequan.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: yq
 * @Date: 2019/7/4 14:24
 * @Description:
 */
@RestController
public class LoginController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisService redisService;

    /**
     * 登录说明:从前台传过来的密码是经过一次不加盐值的MD5摘要password-A,
     * 而存入数据库的密码是将password-A经过一次加盐值"caffea"的MD5摘要password-B,
     * 所以在登录密码校验时需要将password-A进行带盐值"caffea"的MD5摘要
     *
     * @param request
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public AppResult<String> login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDTO userDTO) {
        try {
            if (null != userDTO && userDTO.getMobilephone() != null) {
                UserDO user = iUserService.loginCheck(userDTO);
                if (null != user) {
                    Integer status = user.getStatus();
                    if (status == AppConstant.UserConstant.USER_NORMAL.getStatus()) {
                        TokenPayload tokenPayload = new TokenPayload();
                        tokenPayload.setMobilephone(user.getMobilephone());
                        tokenPayload.setUserId(user.getId());
                        tokenPayload.setUserAgent(request.getHeader("User-Agent"));
                        String unEncrypt = JSON.toJSONString(tokenPayload);
                        //对载荷信息加密
                        String encrypt = AESUtil.encryptToStr(unEncrypt, AppConstant.SecurityConstant.AES_KEY);
                        //生成token
                        String token = JwtUtil.sign(encrypt);
                        //将token信息写入redis
                        RedisUser redisUser = new RedisUser(user.getId(), token, System.currentTimeMillis() + AppConstant.SecurityConstant.EXPIRE_SECOND);
                        redisService.setObj(AppConstant.RedisPrefixKey.REDIS_TOKEN + user.getId(), redisUser, AppConstant.SecurityConstant.EXPIRE_SECOND);
                        response.addHeader("access-token", token);
                        return AppResultBuilder.successNoData(ResultCode.SUCCESS);
                    } else if (status == AppConstant.UserConstant.USER_ILLEGAL.getStatus()) {
                        return AppResultBuilder.failure(ResultCode.USER_ACCOUNT_FORBIDDEN);
                    } else {
                        return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
                    }
                } else {
                    return AppResultBuilder.failure(ResultCode.USER_LOGIN_INFO_ERROR);
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
