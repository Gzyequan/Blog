package com.yequan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.service.RedisService;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.DateUtil;
import com.yequan.common.util.Logger;
import com.yequan.common.util.MD5Util;
import com.yequan.pojo.dto.SysUserDto;
import com.yequan.pojo.entity.SysUserDO;
import com.yequan.pojo.entity.SysUserRoleDO;
import com.yequan.user.dao.SysUserMapper;
import com.yequan.user.dao.SysUserRoleDOMapper;
import com.yequan.user.service.IAdminUserService;
import com.yequan.validation.ValidationUtil;
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
    private SysUserRoleDOMapper sysUserRoleDOMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public AppResult<Void> createOneUser(SysUserDto sysUserDto) {
        try {
            if (null == sysUserDto) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysUserDto);
            if (validResult.isHasErrors()) {
                return AppResultBuilder.failure(ResultCode.DATA_VALIDATION_ERROR);
            }

            SysUserDO sysUserDO = new SysUserDO();
            sysUserDO.setNickname(sysUserDto.getNickname());
            sysUserDO.setRealname(sysUserDto.getRealname());
            sysUserDO.setMobilephone(sysUserDto.getMobilephone());
            sysUserDO.setPassword(MD5Util.encrypt(sysUserDto.getPassword()));
            sysUserDO.setCreateTime(DateUtil.getCurrentDateStr());
            sysUserDO.setCreatorId(CurrentUserLocal.getUserId());

            int insert = sysUserMapper.insertSelective(sysUserDO);
            if (insert > 0) {
                SysUserDO newSysUser = sysUserMapper.selectByMobilephone(sysUserDO.getMobilephone());
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setUserId(newSysUser.getId());
                sysUserRoleDO.setRoleId(sysUserDto.getRoleId());
                int setUserRole = sysUserRoleDOMapper.insert(sysUserRoleDO);
                if (setUserRole > 0) {
                    return AppResultBuilder.success();
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.USER_CREATE_ERROR);
    }

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
            List<SysUserDO> sysUserDOs = pageInfo.getList();
            if (sysUserDOs != null && sysUserDOs.size() > 0) {
                return AppResultBuilder.success(sysUserDOs);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
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
                return AppResultBuilder.success(sysUserDO);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
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
            sysUserDO.setModifyTime(DateUtil.getCurrentDateStr());
            sysUserDO.setUpdaterId(CurrentUserLocal.getUserId());
            int update = sysUserMapper.updateByPrimaryKeySelective(sysUserDO);
            if (update > 0) {
                SysUserDO updatedSysUserDO = sysUserMapper.selectByPrimaryKey(id);
                return AppResultBuilder.success(updatedSysUserDO);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
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
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.USER_DELETE_ERROR);
    }

}
