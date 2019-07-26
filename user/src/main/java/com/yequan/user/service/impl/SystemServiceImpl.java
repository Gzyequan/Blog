package com.yequan.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.yequan.common.application.AppConstant;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.bean.TokenPayload;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.*;
import com.yequan.user.dao.UserMapper;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import com.yequan.user.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/7/25 15:57
 * @Description:
 */
@Service("iSystemService")
public class SystemServiceImpl implements ISystemService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 登录说明:从前台传过来的密码是经过一次不加盐值的MD5摘要password-A,
     * 而存入数据库的密码是将password-A经过一次加盐值"caffea"的MD5摘要password-B,
     * 所以在登录密码校验时需要将password-A进行带盐值"caffea"的MD5摘要
     * <p>
     * 登录逻辑说明:
     * 1.用户手机号,密码校验
     * 2.校验成功,用户状态校验
     * 3.校验成功,生成token,同时将用户信息写入redis
     * 4.登录成功
     *
     * @param request
     * @param response
     * @param userDTO
     * @return
     */
    @Override
    public AppResult<String> login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        try {
            if (null == userDTO || userDTO.getMobilephone() == null) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            //登录校核
            UserDO user = loginCheck(userDTO);
            if (null == user) {
                return AppResultBuilder.failure(ResultCode.USER_LOGIN_INFO_ERROR);
            }
            //用户状态校验
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
                //将用户信息写入redis
                Map<String, Object> redisUserInfoMap = MapUtil.objectToMap(user);
                redisService.setMap(AppConstant.RedisPrefixKey.REDIS_CURRENT_USER + user.getId(), redisUserInfoMap,
                        AppConstant.SecurityConstant.EXPIRE_SECOND);
                response.setHeader("access-token", token);
                return AppResultBuilder.success(ResultCode.SUCCESS);
            } else if (status == AppConstant.UserConstant.USER_ILLEGAL.getStatus()) {
                return AppResultBuilder.failure(ResultCode.USER_ACCOUNT_FORBIDDEN);
            } else {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR);
    }

    /**
     * 用户注册
     * 注册逻辑说明
     * 1.校验手机号是否被注册
     * 2.校验成功对密码进行md5加密
     * 3.注册成功
     *
     * @param userDO
     * @return
     */
    public AppResult<UserDO> register(UserDO userDO) {
        try {
            if (null == userDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setMobilephone(userDO.getMobilephone());
            //判断手机号是否已被注册
            UserDO dbUser = userMapper.selectByMobilephone(userDTO);
            if (null != dbUser) {
                return AppResultBuilder.failure(ResultCode.USER_MOBILE_EXISTED);
            }
            //对密码进行加密
            String password = userDO.getPassword();
            if (null == password) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            String encryptPassword = MD5Util.encrypt(password);
            userDO.setPassword(encryptPassword);
            userDO.setCreateTime(DateUtil.getCurrentDate());
            int insert = userMapper.insertSelective(userDO);
            if (insert > 0) {
                UserDO newUser = userMapper.selectByMobilephone(userDTO);
                return AppResultBuilder.success(newUser, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_CREATE_ERROR);
    }

    /**
     * 登录校验:
     * 1.手机号密码校验
     * 2.用户状态校验:已注销用户返回空
     *
     * @param userDTO
     * @return
     */
    private UserDO loginCheck(UserDTO userDTO) {
        String password = userDTO.getPassword();
        String md5Password = MD5Util.encrypt(password);
        userDTO.setPassword(md5Password);
        return userMapper.loginCheck(userDTO);
    }

}
