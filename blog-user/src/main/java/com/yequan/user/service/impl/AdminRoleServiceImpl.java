package com.yequan.user.service.impl;

import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.DateUtil;
import com.yequan.common.util.Logger;
import com.yequan.common.util.MD5Util;
import com.yequan.constant.StatusEnum;
import com.yequan.pojo.dto.SysRoleDto;
import com.yequan.pojo.entity.SysRoleDO;
import com.yequan.pojo.entity.SysRolePermissionDO;
import com.yequan.user.dao.SysRoleDOMapper;
import com.yequan.user.dao.SysRolePermissionDOMapper;
import com.yequan.user.service.IAdminRoleService;
import com.yequan.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

            Integer userId = CurrentUserLocal.getUserId();
            sysRoleDO.setCreatorId(userId);
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

    /**
     * 删除角色:
     * 1.删除与该角色关联的用户角色信息删除
     * 2.删除与该角色关联的权限
     *
     * @param roleId
     * @return
     */
    @Transactional
    @Override
    public AppResult<Void> deleteSysRoleById(Integer roleId) {
        try {
            if (null == roleId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            //1.删除角色的权限信息
            sysRolePermissionDOMapper.clearRolePermission(roleId);

            //2.逻辑删除角色信息
            SysRoleDto sysRoleDto = new SysRoleDto();
            sysRoleDto.setId(roleId);
            sysRoleDto.setStatus(StatusEnum.STATUS_ILLEGAL.getStatus());
            sysRoleDOMapper.setSysRoleStatus(sysRoleDto);
            return AppResultBuilder.success();
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
    public List<SysRoleDO> getRoleByUserId(Integer userId) {
        try {
            if (null == userId) {
                return null;
            }
            List<SysRoleDO> sysRoleList = sysRoleDOMapper.getRoleByUserId(userId);
            if (CollectionUtils.isEmpty(sysRoleList)) {
                return sysRoleList;
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public AppResult<Void> grantAuthorityToRole(Integer roleId, List<Integer> permissionIds) {
        try {
            if (null == roleId || CollectionUtils.isEmpty(permissionIds)) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            //用于存放组装好的SysRolePermissionDO集合
            List<SysRolePermissionDO> sysRolePermissions = new ArrayList<>();
            //从数据库中查找该角色的权限列表
            List<SysRolePermissionDO> sysRolePermissionList = sysRolePermissionDOMapper.selectPermissionsByRoleId(roleId);
            if (!CollectionUtils.isEmpty(sysRolePermissionList)) {
                //清空该角色的权限
                int delete = sysRolePermissionDOMapper.clearRolePermission(roleId);
                if (delete > 0) {
                    assembleSysRolePermissionDo(roleId, sysRolePermissions, permissionIds);
                    int grant = sysRolePermissionDOMapper.insertBatch(sysRolePermissions);
                    if (grant == permissionIds.size()) {
                        return AppResultBuilder.success();
                    }
                }
            } else {
                assembleSysRolePermissionDo(roleId, sysRolePermissions, permissionIds);
                int grant = sysRolePermissionDOMapper.insertBatch(sysRolePermissions);
                if (grant == permissionIds.size()) {
                    return AppResultBuilder.success();
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.ROLE_GRANT_PERMISSION_FAILURE);
    }

    /**
     * 根据权限id集合组装sysRolePermission集合
     *
     * @param roleId
     * @param sysRolePermissionList
     * @param permissionIds
     */
    private void assembleSysRolePermissionDo(Integer roleId, List<SysRolePermissionDO> sysRolePermissionList, List<Integer> permissionIds) {
        for (Integer permissionId : permissionIds) {
            SysRolePermissionDO sysRolePermissionDO = new SysRolePermissionDO();
            sysRolePermissionDO.setRoleId(roleId);
            sysRolePermissionDO.setPmnId(permissionId);
            sysRolePermissionList.add(sysRolePermissionDO);
        }
    }

}
