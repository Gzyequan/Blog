package com.yequan.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.yequan.common.application.constant.RedisConsts;
import com.yequan.common.application.constant.SecurityConsts;
import com.yequan.common.application.constant.TokenConsts;
import com.yequan.common.application.constant.UserConsts;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.bean.TokenPayload;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.*;
import com.yequan.user.dao.SysUserMapper;
import com.yequan.user.pojo.dbo.SysUserDO;
import com.yequan.user.pojo.dto.UserDTO;
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
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 登录说明:从前台传过来的密码是经过一次不加盐值的MD5摘要password-A,
     * 而存入数据库的密码是将password-A经过一次加盐值"caffea"的MD5摘要password-B,
     * 所以在登录密码校验时需要将password-A进行带盐值"caffea"的MD5摘要
     * <p>
     * 登录逻辑说明:
     * 正常登录:
     * 1.用户手机号,密码校验
     * 2.校验成功,用户状态校验
     * 3.校验成功,生成token,同时将用户信息写入redis
     * 4.登录成功
     * <p>
     * 七天内自动登录:
     * 勾选保持7天内登录状态后,返回access_token和
     * 1.在7天时限内,当正常的登录状态过期后(超过15分钟),自动申请一个access_token
     * 2.在7天时限外,当正常的登录状态过期后(超过15分钟),跳出登录状态,需要重新登录
     *
     * @param request
     * @param response
     * @param userDTO
     * @return
     */
    @Override
    public AppResult<String> login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        try {
            //校验重复登录
            Integer currentUserId = CurrentUserLocal.getUserId();
            if (currentUserId != null) {
                Map<String, Object> currentUserMap = redisService.getMap(RedisConsts.REDIS_CURRENT_USER + currentUserId);
                if (currentUserMap != null && currentUserMap.size() > 0) {
                    return AppResultBuilder.failure(ResultCode.USER_LOGIN_ILLEGAL);
                }
            }
            if (null == userDTO || null == userDTO.getMobilephone()) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            //登录校核
            SysUserDO user = loginCheck(userDTO);
            if (null == user) {
                return AppResultBuilder.failure(ResultCode.USER_LOGIN_INFO_ERROR);
            }
            //用户状态校验
            Integer status = user.getStatus();
            if (status == UserConsts.USER_NORMAL.getStatus()) {
                TokenPayload tokenPayload = new TokenPayload();
                tokenPayload.setUserId(user.getId());
                tokenPayload.setUserAgent(request.getHeader("User-Agent"));
                tokenPayload.setIp(IPUtil.getIpAddress(request));
                String unEncrypt = JSON.toJSONString(tokenPayload);
                //对载荷信息加密
                String encrypt = AESUtil.encryptToStr(unEncrypt, SecurityConsts.AES_KEY);
                String token = null;
                //不选择保持七天登录
                if (null == userDTO.isKeepAlive() || !userDTO.isKeepAlive()) {
                    //生成有效期为15分钟的token
                    token = JwtUtil.sign(encrypt, TokenConsts.TOKEN_EXPIRE_TIME_15MINUTE);
                } else {
                    //如果选择保持七天登录状态
                    //生成有效期为7天的token
                    token = JwtUtil.sign(encrypt, TokenConsts.TOKEN_EXPIRE_TIME_7DAY);
                }
                //将用户信息写入redis
                Map<String, Object> redisUserInfoMap = MapUtil.objectToMap(user);
                redisService.setMap(RedisConsts.REDIS_CURRENT_USER + user.getId(), redisUserInfoMap,
                        RedisConsts.REDIS_EXPIRE_SECOND);
                response.setHeader("access-token", token);
                return AppResultBuilder.success(ResultCode.SUCCESS);
            } else if (status == UserConsts.USER_ILLEGAL.getStatus()) {
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
     * @param sysUserDO
     * @return
     */
    public AppResult<SysUserDO> register(SysUserDO sysUserDO) {
        try {
            if (null == sysUserDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setMobilephone(sysUserDO.getMobilephone());
            //判断手机号是否已被注册
            SysUserDO dbUser = sysUserMapper.selectByMobilephone(userDTO);
            if (null != dbUser) {
                return AppResultBuilder.failure(ResultCode.USER_MOBILE_EXISTED);
            }
            //对密码进行加密
            String password = sysUserDO.getPassword();
            if (null == password) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            String encryptPassword = MD5Util.encrypt(password);
            sysUserDO.setPassword(encryptPassword);
            sysUserDO.setCreateTime(DateUtil.getCurrentDate());
            int insert = sysUserMapper.insertSelective(sysUserDO);
            if (insert > 0) {
                SysUserDO newUser = sysUserMapper.selectByMobilephone(userDTO);
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
    private SysUserDO loginCheck(UserDTO userDTO) {
        String password = userDTO.getPassword();
        String md5Password = MD5Util.encrypt(password);
        userDTO.setPassword(md5Password);
        return sysUserMapper.loginCheck(userDTO);
    }

}
