package com.yequan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.DateUtil;
import com.yequan.user.dao.UserMapper;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.service.IAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/26 11:25
 * @Description:
 */
@Service("iAdminUserService")
public class AdminUserServiceImpl implements IAdminUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 获取用户集合
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public AppResult<List<UserDO>> listUsers(Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum || null == pageSize) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            List<UserDO> userDOS = selectUserList(pageNum, pageSize);
            if (userDOS != null && userDOS.size() > 0) {
                return AppResultBuilder.success(userDOS, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.RESULE_DATA_NONE);
    }

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    public AppResult<UserDO> getUserById(Integer id) {
        try {
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDO userDO = userMapper.selectByPrimaryKey(id);
            if (null != userDO) {
                return AppResultBuilder.success(userDO, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.RESULE_DATA_NONE);
    }

    /**
     * 更新用户信息
     * 在线用户不能修改
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

            UserDO currentUserDO = userMapper.selectByPrimaryKey(id);
            if (null == currentUserDO) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            userDO.setId(id);
            userDO.setModifyTime(DateUtil.getCurrentDate());
            int update = userMapper.updateByPrimaryKeySelective(userDO);
            if (update > 0) {
                UserDO updatedUserDO = userMapper.selectByPrimaryKey(id);
                return AppResultBuilder.success(updatedUserDO, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_UPDATE_ERROR);
    }

    /**
     * 根据用户id删除用户信息
     *
     * @param id
     * @return
     */
    public AppResult deleteUserById(Integer id) {
        try {
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDO userDO = userMapper.selectByPrimaryKey(id);
            if (null == userDO) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            int delete = userMapper.deleteByPrimaryKey(id);
            if (delete > 0) {
                return AppResultBuilder.success(ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_DELETE_ERROR);
    }

    private List<UserDO> selectUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDO> userDOList = userMapper.selectUserList();
        PageInfo<UserDO> pageInfo = new PageInfo<UserDO>(userDOList);
        return pageInfo.getList();
    }
}
