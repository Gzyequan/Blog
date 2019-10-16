package com.yequan.user.dao;

import com.yequan.pojo.entity.SysRolePermissionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRolePermissionDOMapper {
    int insert(SysRolePermissionDO record);

    int insertSelective(SysRolePermissionDO record);

    int clearRolePermission(Integer roleId);

    int insertBatch(@Param("rolePermissionList") List<SysRolePermissionDO> rolePermissionList);

    List<SysRolePermissionDO> selectPermissionsByRoleId(Integer roleId);
}