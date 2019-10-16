package com.yequan.user.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.DateUtil;
import com.yequan.common.util.Logger;
import com.yequan.constant.StatusEnum;
import com.yequan.pojo.dto.SysPermissionDto;
import com.yequan.pojo.entity.SysPermissionDO;
import com.yequan.user.dao.SysPermissionDOMapper;
import com.yequan.user.service.IAdminPermissionService;
import com.yequan.validation.ValidationUtil;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

/**
 * @Auther: yq
 * @Date: 2019/7/30 10:12
 * @Description:
 */
@Service("iAdminPermissionService")
public class AdminPermissionServiceImpl implements IAdminPermissionService {

    @Autowired
    private SysPermissionDOMapper sysPermissionDOMapper;

    /**
     * 获取pmnCode下一级(不做递归查所有)的所有权限
     *
     * @return
     */
    @Override
    public AppResult<List<SysPermissionDO>> listChildrenParallelPermissions(Integer pmnId) {
        try {
            if (null == pmnId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            List<SysPermissionDO> sysPermissionDOList = sysPermissionDOMapper.selectChildrenParallelPermission(pmnId);
            if (!CollectionUtils.isEmpty(sysPermissionDOList)) {
                return AppResultBuilder.success(sysPermissionDOList);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
    }

    /**
     * 递归获取pmnCode下一级及其子权限中的所有权限
     *
     * @param pmnId
     * @return
     */
    @Override
    public AppResult<List<SysPermissionDO>> listDeepSysPermissions(Integer pmnId) {
        try {
            if (null == pmnId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            TreeSet<SysPermissionDO> sysPermissionSet = Sets.newTreeSet(new Comparator<SysPermissionDO>() {
                //treeSet使用Comparator比较器进行自然排序
                @Override
                public int compare(SysPermissionDO o1, SysPermissionDO o2) {
                    return o1.getId() - o2.getId();
                }
            });
            listDeepChildrenPermission(sysPermissionSet, pmnId);
            ArrayList<SysPermissionDO> sysPermissionDOList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(sysPermissionSet)) {
                sysPermissionDOList.addAll(sysPermissionSet);
            }
            return AppResultBuilder.success(sysPermissionDOList);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.RESULT_DATA_NONE);
    }

    @Override
    public AppResult<Void> createOneSysPermission(SysPermissionDO sysPermissionDO) {
        try {
            if (null == sysPermissionDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }

            //校验数据合法性
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysPermissionDO);
            if (validResult.isHasErrors()) {
                String errors = validResult.getErrors();
                Logger.error(errors);
                return AppResultBuilder.failure(ResultCode.DATA_VALIDATION_ERROR);
            }

            Integer parentId = sysPermissionDO.getParentId();
            //parentId为0表示添加的是根节点,校验parentId有效性
            if (parentId != 0) {
                SysPermissionDO sysPermissionDB = sysPermissionDOMapper.selectByPrimaryKey(parentId);
                if (null == sysPermissionDB) {
                    return AppResultBuilder.failure(ResultCode.PARAM_IS_INVALID);
                }
            }

            //获取当前创建人
            Integer currentUserId = CurrentUserLocal.getUserId();
            if (currentUserId == null) {
                return AppResultBuilder.failure(ResultCode.ERROR);
            }
            sysPermissionDO.setCreatorId(currentUserId);
            //设置创建时间
            sysPermissionDO.setCreateTime(DateUtil.getCurrentDateStr());
            int insert = sysPermissionDOMapper.insertSelective(sysPermissionDO);
            if (insert > 0) {
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.PERMISSION_CREATE_ERROR);
    }

    @Override
    public AppResult<Void> createSysPermissionBatch(List<SysPermissionDO> sysPermissions) {
        try {
            if (CollectionUtils.isEmpty(sysPermissions)) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }

            for (SysPermissionDO sysPermission : sysPermissions) {
                //校验数据合法性
                ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysPermission);
                if (validResult.isHasErrors()) {
                    String errors = validResult.getErrors();
                    Logger.error(errors);
                    return AppResultBuilder.failure(ResultCode.DATA_VALIDATION_ERROR);
                }
                Integer parentId = sysPermission.getParentId();
                //parentId为0表示添加的是根节点,校验parentId有效性
                if (parentId != 0) {
                    SysPermissionDO sysPermissionDB = sysPermissionDOMapper.selectByPrimaryKey(parentId);
                    if (null == sysPermissionDB) {
                        return AppResultBuilder.failure(ResultCode.PARAM_IS_INVALID);
                    }
                }
                //获取当前创建人
                Integer currentUserId = CurrentUserLocal.getUserId();
                if (currentUserId == null) {
                    return AppResultBuilder.failure(ResultCode.ERROR);
                }
                sysPermission.setCreatorId(currentUserId);
                //设置创建时间
                sysPermission.setCreateTime(DateUtil.getCurrentDateStr());
            }

            int insert = sysPermissionDOMapper.insertBatch(sysPermissions);
            if (sysPermissions.size() == insert) {
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.PERMISSION_CREATE_ERROR);
    }

    /**
     * 根据id删除资源
     *
     * @param pmnId
     * @return
     */
    public AppResult<Void> deleteSysPermissionById(Integer pmnId) {
        try {
            if (null == pmnId) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            SysPermissionDto sysPermissionDto = new SysPermissionDto();
            sysPermissionDto.setId(pmnId);
            sysPermissionDto.setStatus(StatusEnum.STATUS_ILLEGAL.getStatus());
            int delete = sysPermissionDOMapper.setPermissionStatus(sysPermissionDto);
            if (delete > 0) {
                return AppResultBuilder.success();
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return AppResultBuilder.failure(ResultCode.PERMISSION_DELETE_FAILURE);
    }

    @Override
    public List<SysPermissionDO> getSysPermissionByRoleId(Integer roleId) {
        try {
            if (null == roleId) {
                return null;
            }
            List<SysPermissionDO> sysPermissionList = sysPermissionDOMapper.getSysPermissionByRoleId(roleId);
            if (!CollectionUtils.isEmpty(sysPermissionList)) {
                return sysPermissionList;
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<SysPermissionDO> getSysPermissionByUserId(Integer userId) {
        if (null == userId) {
            return null;
        }
        return sysPermissionDOMapper.getSysPermissionByUserId(userId);
    }

    /**
     * 递归查询出pmnCode所有子权限
     *
     * @param sysPermissionSet
     * @param pmnId
     */
    private void listDeepChildrenPermission(TreeSet<SysPermissionDO> sysPermissionSet, Integer pmnId) {
        SysPermissionDO sysPermissionDO = sysPermissionDOMapper.selectByPrimaryKey(pmnId);
        if (null != sysPermissionDO) {
            sysPermissionSet.add(sysPermissionDO);
        }
        //查询子节点
        List<SysPermissionDO> sysPermissionDOList = sysPermissionDOMapper.selectChildrenParallelPermission(pmnId);
        //mybatis返回的集合不会为空，如果返回的集合没有数据则无法进入循环，结束递归，查询结束
        for (SysPermissionDO permissionDO : sysPermissionDOList) {
            listDeepChildrenPermission(sysPermissionSet, permissionDO.getId());
        }
    }
}
