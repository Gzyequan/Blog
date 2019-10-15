package com.yequan.user.dao;

import com.yequan.pojo.entity.SysPermissionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysPermissionDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPermissionDO record);

    int insertSelective(SysPermissionDO record);

    int insertBatch(@Param("sysPermissionList") List<SysPermissionDO> sysPermissionList);

    SysPermissionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermissionDO record);

    int updateByPrimaryKey(SysPermissionDO record);

    List<SysPermissionDO> selectChildrenParallelPermission(Integer parentId);
}