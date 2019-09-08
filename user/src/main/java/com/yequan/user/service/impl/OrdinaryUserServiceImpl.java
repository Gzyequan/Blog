package com.yequan.user.service.impl;

import com.yequan.common.annotation.CrossPermission;
import com.yequan.common.application.constant.RedisConsts;
import com.yequan.common.application.constant.UserEnum;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.service.RedisService;
import com.yequan.common.util.*;
import com.yequan.user.dao.SysUserMapper;
import com.yequan.user.pojo.dbo.SysUserDO;
import com.yequan.user.pojo.dto.UserDTO;
import com.yequan.user.service.IOrdinaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
@Service("iOrdinaryUserService")
public class OrdinaryUserServiceImpl implements IOrdinaryUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据用户主键id查询用户信息,只能获取当前登录用户的信息
     * 1.获取当前登录人信息,与传入用户id校核,相同则下一步,不通过则返回错误
     * 2.从redis中获取用户信息
     * 3.如果redis中有用户信息则返回
     * 4.如果没有则从数据库中获取
     *
     * @param id
     * @return
     */
    public AppResult<SysUserDO> getUserById(Integer id) {
        try {
            SysUserDO currentUser = null;
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }

            //获取当前用户id
            Integer currentUserId = CurrentUserLocal.getUserId();
            if (currentUserId == null) {
                return AppResultBuilder.failure(ResultCode.ERROR);
            }

            //从redis中获取当前用户信息
            Map<String, Object> currentUserMap = redisService.getMap(RedisConsts.REDIS_CURRENT_USER + id);
            if (null == currentUserMap) {
                AppResultBuilder.failure(ResultCode.USER_NOT_LOGGED_IN);
            }
            //将当前用户map集合转换成对象
            currentUser = (SysUserDO) MapUtil.mapToObject(currentUserMap, SysUserDO.class);
            if (null != currentUser) {
                return AppResultBuilder.success(currentUser);
            }
            currentUser = sysUserMapper.selectByPrimaryKey(id);
            if (null != currentUser) {
                return AppResultBuilder.success(currentUser);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
    }

    /**
     * 新增用户
     * 对用户密码进行md5摘要
     *
     * @param sysUserDO
     * @return
     */
    public int insertSelective(SysUserDO sysUserDO) {
        if (null == sysUserDO) {
            return 0;
        }
        String password = sysUserDO.getPassword();
        String md5Password = MD5Util.encrypt(password);
        sysUserDO.setPassword(md5Password);
        return sysUserMapper.insertSelective(sysUserDO);
    }

    /**
     * 更新当前登录用户的信息,更新用户信息不更新用户密码,不更新手机号码,密码和手机号码通过其他接口修改
     * 1.根据id从redis中获取当前用户的信息,用于校验是否登录
     * 2.校验成功则将新的用户信息更新至数据库中
     * 3.更新redis中当前用户的信息
     *
     * @param id
     * @param sysUserDO
     * @return
     */
    public AppResult<SysUserDO> updateUser(Integer id, SysUserDO sysUserDO) {
        try {
            if (null == sysUserDO || null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            //从redis中获取当前用户信息
            Map<String, Object> currentUserMap = redisService.getMap(RedisConsts.REDIS_CURRENT_USER + id);
            if (null == currentUserMap) {
                AppResultBuilder.failure(ResultCode.USER_NOT_LOGGED_IN);
            }
            //数据库中查询用户
            SysUserDO currentUser = sysUserMapper.selectByPrimaryKey(id);
            if (null == currentUser) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            sysUserDO.setId(id);
            //排除用户密码
            sysUserDO.setPassword(null);
            //排除用户手机号
            sysUserDO.setMobilephone(null);
            sysUserDO.setModifyTime(DateUtil.getCurrentDateStr());
            int update = sysUserMapper.updateByPrimaryKeySelective(sysUserDO);
            if (update > 0) {
                SysUserDO updatedSysUserDO = sysUserMapper.selectByPrimaryKey(id);
                //更新redis中当前用户的信息
                Map<String, Object> updateUserMap = MapUtil.objectToMap(updatedSysUserDO);
                redisService.setMap(RedisConsts.REDIS_CURRENT_USER + id, updateUserMap);
                return AppResultBuilder.success(updatedSysUserDO);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        return AppResultBuilder.failure(ResultCode.USER_UPDATE_ERROR);
    }

    /**
     * 根据手机号查询用户
     *
     * @param userDTO
     * @return
     */
    public SysUserDO selectByMobilephone(UserDTO userDTO) {
        SysUserDO sysUserDO = null;
        if (null != userDTO) {
            sysUserDO = sysUserMapper.selectByMobilephone(userDTO);
        }
        return sysUserDO;
    }

    /**
     * 注销用户
     *
     * @param id
     * @return
     */
    public AppResult<String> unregisterUser(Integer id) {
        try {
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            SysUserDO user = sysUserMapper.selectByPrimaryKey(id);
            if (null == user) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            SysUserDO sysUserDO = new SysUserDO();
            sysUserDO.setId(id);
            sysUserDO.setStatus(UserEnum.USER_DELETED.getStatus());
            int update = sysUserMapper.updateByPrimaryKeySelective(sysUserDO);
            if (update > 0) {
                //注销用户后将该用户信息从redis中清除
                redisService.del(RedisConsts.REDIS_CURRENT_USER + id);
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        return AppResultBuilder.failure(ResultCode.USER_UNREGISTER_ERROR);
    }

}
