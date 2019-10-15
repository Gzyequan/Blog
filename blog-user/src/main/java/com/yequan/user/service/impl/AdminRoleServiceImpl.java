package com.yequan.user.service.impl;

import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.util.DateUtil;
import com.yequan.common.util.Logger;
import com.yequan.pojo.entity.SysRoleDO;
import com.yequan.pojo.entity.SysRolePermissionDO;
import com.yequan.user.dao.SysRoleDOMapper;
import com.yequan.user.dao.SysRolePermissionDOMapper;
import com.yequan.user.service.IAdminRoleService;
import com.yequan.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/10/14 15:05
 * @Description:
 */
@Service("iAdminRoleService")
public class AdminRoleServiceImpl implements IAdminRoleService {

    @Autowired
    private SysRoleDOMapper sysRoleDOMapper;

    @Autowired
    private SysRolePermissionDOMapper sysRolePermissionDOMapper;

    @Override
    public AppResult<SysRoleDO> getSysRoleById(Integer roleId) {
        try {
            if (null == roleId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            SysRoleDO sysRoleDO = sysRoleDOMapper.selectByPrimaryKey(roleId);
            if (null != sysRoleDO) {
                return AppResultBuilder.success(sysRoleDO);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
    }

    @Override
    public AppResult<Void> insertOneSysRole(SysRoleDO sysRoleDO) {
        try {
            if (null == sysRoleDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }

            //数据校验
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysRoleDO);
            if (validResult.isHasErrors()) {
                return AppResultBuilder.failure(ResultCode.DATA_VALIDATION_ERROR);
            }

            sysRoleDO.setCreateTime(DateUtil.getCurrentDateStr());
            int insert = sysRoleDOMapper.insertSelective(sysRoleDO);
            if (insert > 0) {
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.ROLE_CREATE_FAILURE);
    }

    @Override
    public AppResult<Void> deleteSysRoleById(Integer roleId) {
        try {
            if (null == roleId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            int delete = sysRoleDOMapper.deleteByPrimaryKey(roleId);
            if (delete > 0) {
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.ROLE_DELETE_FAILURE);
    }

    @Override
    public AppResult<Void> updateSysRole(Integer roleId, SysRoleDO sysRoleDO) {
        try {
            if (null == roleId || null == sysRoleDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }

            //数据校验
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysRoleDO);
            if (validResult.isHasErrors()) {
                return AppResultBuilder.failure(ResultCode.DATA_VALIDATION_ERROR);
            }

            //设置更新时间
            sysRoleDO.setModifyTime(DateUtil.getCurrentDateStr());
            int update = sysRoleDOMapper.insertSelective(sysRoleDO);
            if (update > 0) {
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.ROLE_DELETE_FAILURE);
    }

    @Override
    public AppResult<SysRoleDO> getRoleByUserId(Integer userId) {
        try {
            if (null == userId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            SysRoleDO sysRole = sysRoleDOMapper.getRoleByUserId(userId);
            if (null != sysRole) {
                return AppResultBuilder.success(sysRole);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
    }

    @Override
    public AppResult<Void> grantAuthorityToRole(Integer roleId, String permissionIds) {
        try {
            if (null == roleId || null == permissionIds) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            //清空该角色的权限
            int delete = sysRolePermissionDOMapper.clearRolePermission(roleId);
            if (delete > 0) {
                String[] permissionIdArray = permissionIds.split(",");
                List<String> permissionIdList = Arrays.asList(permissionIdArray);
                List<SysRolePermissionDO> sysRolePermissionList = new ArrayList<>();
                for (String permissionId : permissionIdList) {
                    SysRolePermissionDO sysRolePermissionDO = new SysRolePermissionDO();
                    sysRolePermissionDO.setRoleId(roleId);
                    sysRolePermissionDO.setPmnId(Integer.parseInt(permissionId));
                    sysRolePermissionList.add(sysRolePermissionDO);
                }
                int grant = sysRolePermissionDOMapper.insertBatch(sysRolePermissionList);
                if (grant > 0) {
                    return AppResultBuilder.success();
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.ROLE_GRANT_PERMISSION_FAILURE);
    }
}
