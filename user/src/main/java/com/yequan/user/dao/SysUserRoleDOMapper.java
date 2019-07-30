package com.yequan.user.dao;

import com.yequan.user.pojo.dbo.SysUserRoleDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRoleDOMapper {

    int insert(SysUserRoleDO record);

    int insertSelective(SysUserRoleDO record);
}