package com.yequan.user.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yequan.common.application.constant.PermissionConsts;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.DateUtil;
import com.yequan.user.dao.SysPermissionDOMapper;
import com.yequan.user.pojo.dbo.SysPermissionDO;
import com.yequan.user.service.IAdminPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        if (null == pmnId) {
            return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
        }
        List<SysPermissionDO> sysPermissionDOList = sysPermissionDOMapper.selectChildrenParallelPermission(pmnId);
        if (CollectionUtils.isEmpty(sysPermissionDOList)) {
            return AppResultBuilder.failure(ResultCode.RESULE_DATA_NONE);
        }
        return AppResultBuilder.success(sysPermissionDOList, ResultCode.SUCCESS);
    }

    /**
     * 递归获取pmnCode下一级及其子权限中的所有权限
     *
     * @param pmnId
     * @return
     */
    @Override
    public AppResult<List<SysPermissionDO>> listDeepSysPermissions(Integer pmnId) {
        if (null == pmnId) {
            return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
        }
        HashSet<SysPermissionDO> sysPermissionSet = Sets.newHashSet();
        listDeepChildrenPermission(sysPermissionSet, pmnId);
        ArrayList<SysPermissionDO> sysPermissionDOList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(sysPermissionSet)) {
            sysPermissionDOList.addAll(sysPermissionSet);
        }
        return AppResultBuilder.success(sysPermissionDOList, ResultCode.SUCCESS);
    }

    @Override
    public AppResult<Void> insertOneSysPermission(SysPermissionDO sysPermissionDO) {
        if (null == sysPermissionDO) {
            return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
        }
        Integer parentId = sysPermissionDO.getParentId();
        String type = sysPermissionDO.getType();
        if (null == parentId || StringUtils.isEmpty(type) || StringUtils.isEmpty(sysPermissionDO.getPmnName())) {
            return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
        }

        //parentId为0表示添加的是根节点,校验parentId有效性
        if (parentId != 0) {
            SysPermissionDO sysPermissionDB = sysPermissionDOMapper.selectByPrimaryKey(parentId);
            if (null == sysPermissionDB) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_INVALID);
            }
        }
        //校验type有效性
        if (!PermissionConsts.PERMISSION_TYPE_MODULE.equals(type)
                && !PermissionConsts.PERMISSION_TYPE_PAGE.equals(type)) {
            return AppResultBuilder.failure(ResultCode.PERMISSION_TYPE_ERROR);
        }

        //获取当前创建人
        Integer currentUserId = CurrentUserLocal.getUserId();
        if (currentUserId==null){
            return AppResultBuilder.failure(ResultCode.ERROR);
        }
        sysPermissionDO.setCreatorId(currentUserId);
        //设置创建时间
        sysPermissionDO.setCreateTime(DateUtil.getCurrentDate());
        int insert = sysPermissionDOMapper.insertSelective(sysPermissionDO);
        if (insert > 0) {
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }
        return AppResultBuilder.failure(ResultCode.PERMISSION_CREATE_ERROR);
    }

    /**
     * 递归查询出pmnCode所有子权限
     *
     * @param sysPermissionSet
     * @param pmnId
     */
    private void listDeepChildrenPermission(HashSet<SysPermissionDO> sysPermissionSet, Integer pmnId) {
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
