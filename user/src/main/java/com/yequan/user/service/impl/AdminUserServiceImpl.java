package com.yequan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.DateUtil;
import com.yequan.user.dao.SysUserMapper;
import com.yequan.user.pojo.dbo.SysUserDO;
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
    private SysUserMapper sysUserMapper;

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
    public AppResult<List<SysUserDO>> listUsers(Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum || null == pageSize) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            //列表分页
            PageHelper.startPage(pageNum, pageSize);
            List<SysUserDO> sysUserDOList = sysUserMapper.selectUserList();
            PageInfo<SysUserDO> pageInfo = new PageInfo<SysUserDO>(sysUserDOList);
            List<SysUserDO> sysUserDOS = pageInfo.getList();
            if (sysUserDOS != null && sysUserDOS.size() > 0) {
                return AppResultBuilder.success(sysUserDOS, ResultCode.SUCCESS);
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
    public AppResult<SysUserDO> getUserById(Integer id) {
        try {
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            SysUserDO sysUserDO = sysUserMapper.selectByPrimaryKey(id);
            if (null != sysUserDO) {
                return AppResultBuilder.success(sysUserDO, ResultCode.SUCCESS);
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
     * @param sysUserDO
     * @return
     */
    public AppResult<SysUserDO> updateUser(Integer id, SysUserDO sysUserDO) {
        try {
            if (null == sysUserDO || null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }

            SysUserDO currentSysUserDO = sysUserMapper.selectByPrimaryKey(id);
            if (null == currentSysUserDO) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            sysUserDO.setId(id);
            sysUserDO.setModifyTime(DateUtil.getCurrentDate());
            int update = sysUserMapper.updateByPrimaryKeySelective(sysUserDO);
            if (update > 0) {
                SysUserDO updatedSysUserDO = sysUserMapper.selectByPrimaryKey(id);
                return AppResultBuilder.success(updatedSysUserDO, ResultCode.SUCCESS);
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
            SysUserDO sysUserDO = sysUserMapper.selectByPrimaryKey(id);
            if (null == sysUserDO) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            int delete = sysUserMapper.deleteByPrimaryKey(id);
            if (delete > 0) {
                return AppResultBuilder.success(ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_DELETE_ERROR);
    }

}
