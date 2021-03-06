package com.yequan.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.yequan.common.application.config.Global;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.bean.TokenPayload;
import com.yequan.common.service.RedisService;
import com.yequan.common.util.*;
import com.yequan.constant.*;
import com.yequan.pojo.dto.UserDTO;
import com.yequan.pojo.entity.SysUserDO;
import com.yequan.pojo.entity.SysUserRoleDO;
import com.yequan.user.dao.SysUserMapper;
import com.yequan.user.dao.SysUserRoleDOMapper;
import com.yequan.user.service.ISystemService;
import com.yequan.validation.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private SysUserRoleDOMapper sysUserRoleDOMapper;

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
//            Integer currentUserId = CurrentUserLocal.getUserId();
//            if (currentUserId != null) {
//                Map<String, Object> currentUserMap = redisService.getMap(RedisConsts.REDIS_CURRENT_USER + currentUserId);
//                if (currentUserMap != null && currentUserMap.size() > 0) {
//                    return AppResultBuilder.failure(ResultCode.USER_LOGIN_ILLEGAL);
//                }
//            }
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
            if (status == UserEnum.USER_NORMAL.getStatus()) {
                TokenPayload tokenPayload = new TokenPayload();
                tokenPayload.setUserId(user.getId());
                tokenPayload.setUserAgent(request.getHeader(HttpHeaderConsts.USER_AGENT));
                tokenPayload.setIp(IPUtil.getIpAddress(request));
                String unEncrypt = JSON.toJSONString(tokenPayload);
                //对载荷信息加密
                String encrypt = AESUtil.encryptToStr(unEncrypt, SecurityConsts.AES_KEY);
                String token = null;
                //不选择保持七天登录
                if (null == userDTO.isKeepAlive() || !userDTO.isKeepAlive()) {
                    //生成有效期为15分钟的token
                    token = JwtUtil.sign(encrypt, TokenConsts.TOKEN_EXPIRE_TIME_15_MINUTE);
                } else {
                    //如果选择保持七天登录状态
                    //生成有效期为7天的token
                    token = JwtUtil.sign(encrypt, TokenConsts.TOKEN_EXPIRE_TIME_7_DAY);
                }
                //将用户信息写入redis
                Map<String, Object> redisUserInfoMap = MapUtil.objectToMap(user);
                redisService.setMap(RedisConsts.REDIS_CURRENT_USER + user.getId(), redisUserInfoMap,
                        RedisConsts.REDIS_EXPIRE_15_MINUTE);
                response.setHeader(HttpHeaderConsts.ACCESS_TOKEN, token);
                return AppResultBuilder.success();
            } else if (status == UserEnum.USER_ILLEGAL.getStatus()) {
                return AppResultBuilder.failure(ResultCode.USER_ACCOUNT_FORBIDDEN);
            } else {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR);
    }

    /**
     * 用户注册
     * 注册逻辑说明
     * 1.校验手机号是否被注册
     * 2.校验成功对密码进行md5加密
     * 3.设置普通用户角色
     * 4.注册成功
     *
     * @param sysUserDO
     * @return
     */
    @Transactional
    @Override
    public AppResult<SysUserDO> register(SysUserDO sysUserDO) throws RuntimeException {
        try {
            if (null == sysUserDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }

            //数据格式校验
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysUserDO);
            if (validResult.isHasErrors()) {
                String errors = validResult.getErrors();
                Logger.error(errors);
                return AppResultBuilder.failure(ResultCode.DATA_VALIDATION_ERROR);
            }

            //生成密码加密盐值
            String salt = RandomUtil.generateStr(10);

            //对密码进行加密
            String password = sysUserDO.getPassword();
            String encryptPassword = MD5Util.encrypt(password,salt);
            sysUserDO.setPassword(encryptPassword);
            sysUserDO.setSalt(salt);
            sysUserDO.setCreateTime(DateUtil.getCurrentDateStr());
            int insert = sysUserMapper.insertSelective(sysUserDO);
            if (insert > 0) {
                SysUserDO newUser = sysUserMapper.selectByMobilephone(sysUserDO.getMobilephone());
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setUserId(newUser.getId());
                sysUserRoleDO.setRoleId(Global.getCustomerRoleId());
                //设置注册用户为普通用户角色
                sysUserRoleDOMapper.insert(sysUserRoleDO);
                return AppResultBuilder.success(newUser);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.USER_CREATE_ERROR);
    }

    /**
     * 校验手机号是否注册
     *
     * @param mobilephone
     * @return
     */
    public AppResult<Void> validateMobilephone(String mobilephone) {
        if (StringUtils.isEmpty(mobilephone)) {
            return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
        }
        //手机号码格式校验
        if (!mobilephone.matches(RegexConsts.REGEX_MOBILE)) {
            return AppResultBuilder.failure(ResultCode.PARAM_IS_INVALID);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setMobilephone(mobilephone);
        //判断手机号是否已被注册
        SysUserDO dbUser = sysUserMapper.selectByMobilephone(userDTO.getMobilephone());
        if (null == dbUser) {
            return AppResultBuilder.success();
        }
        return AppResultBuilder.failure(ResultCode.USER_MOBILE_EXISTED);
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
