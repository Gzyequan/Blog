package com.yequan.user.service.impl;

import com.yequan.common.application.AppConstant;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.DateUtil;
import com.yequan.common.util.MD5Util;
import com.yequan.common.util.MapUtil;
import com.yequan.user.dao.UserMapper;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
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
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据用户主键id查询用户信息
     * 1.从redis中获取用户信息
     * 2.如果redis中有用户信息则返回
     * 3.如果没有则从数据库中获取
     *
     * @param id
     * @return
     */
    public AppResult<UserDO> selectUserById(Integer id) {
        try {
            UserDO currentUser = null;
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            //从redis中获取当前用户信息
            Map<String, Object> currentUserMap = redisService.getMap(AppConstant.RedisPrefixKey.REDIS_CURRENT_USER + id);
            if (null == currentUserMap) {
                AppResultBuilder.failure(ResultCode.USER_NOT_LOGGED_IN);
            }
            //将当前用户map集合转换成对象
            currentUser = (UserDO) MapUtil.mapToObject(currentUserMap, UserDO.class);
            if (null != currentUser) {
                AppResultBuilder.success(currentUser, ResultCode.SUCCESS);
            }
            currentUser = userMapper.selectByPrimaryKey(id);
            if (null != currentUser) {
                return AppResultBuilder.success(currentUser, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.RESULE_DATA_NONE);
    }

    /**
     * 新增用户
     * 对用户密码进行md5摘要
     *
     * @param userDO
     * @return
     */
    public int insertSelective(UserDO userDO) {
        if (null == userDO) {
            return 0;
        }
        String password = userDO.getPassword();
        String md5Password = MD5Util.encrypt(password);
        userDO.setPassword(md5Password);
        return userMapper.insertSelective(userDO);
    }

    /**
     * 更新当前登录用户的信息,更新用户信息不更新用户密码,不更新手机号码,密码和手机号码通过其他接口修改
     * 1.根据id从redis中获取当前用户的信息,用于校验是否登录
     * 2.校验成功则将新的用户信息更新至数据库中
     * 3.更新redis中当前用户的信息
     *
     * @param id
     * @param userDO
     * @return
     */
    public AppResult<UserDO> updateUser(Integer id, UserDO userDO) {
        try {
            if (null == userDO || null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            //从redis中获取当前用户信息
            Map<String, Object> currentUserMap = redisService.getMap(AppConstant.RedisPrefixKey.REDIS_CURRENT_USER + id);
            if (null == currentUserMap) {
                AppResultBuilder.failure(ResultCode.USER_NOT_LOGGED_IN);
            }
            //数据库中查询用户
            UserDO currentUser = userMapper.selectByPrimaryKey(id);
            if (null == currentUser) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            userDO.setId(id);
            //排除用户密码
            userDO.setPassword(null);
            //排除用户手机号
            userDO.setMobilephone(null);
            userDO.setModifyTime(DateUtil.getCurrentDate());
            int update = userMapper.updateByPrimaryKeySelective(userDO);
            if (update > 0) {
                UserDO updatedUserDO = userMapper.selectByPrimaryKey(id);
                //更新redis中当前用户的信息
                Map<String, Object> updateUserMap = MapUtil.objectToMap(updatedUserDO);
                redisService.setMap(AppConstant.RedisPrefixKey.REDIS_CURRENT_USER + id, updateUserMap);
                return AppResultBuilder.success(updatedUserDO, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_UPDATE_ERROR);
    }

    /**
     * 根据手机号查询用户
     *
     * @param userDTO
     * @return
     */
    public UserDO selectByMobilephone(UserDTO userDTO) {
        UserDO userDO = null;
        if (null != userDTO) {
            userDO = userMapper.selectByMobilephone(userDTO);
        }
        return userDO;
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
            UserDO user = userMapper.selectByPrimaryKey(id);
            if (null == user) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            UserDO userDO = new UserDO();
            userDO.setId(id);
            userDO.setStatus(AppConstant.UserConstant.USER_DELETED.getStatus());
            int update = userMapper.updateByPrimaryKeySelective(userDO);
            if (update > 0) {
                //注销用户后将该用户信息从redis中清除
                redisService.del(AppConstant.RedisPrefixKey.REDIS_CURRENT_USER + id);
                return AppResultBuilder.success(ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_UNREGISTER_ERROR);
    }

}
