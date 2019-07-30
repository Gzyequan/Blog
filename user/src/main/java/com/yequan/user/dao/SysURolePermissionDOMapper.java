package com.yequan.user.dao;

import com.yequan.user.pojo.dbo.SysURolePermissionDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SysURolePermissionDOMapper {
    int insert(SysURolePermissionDO record);

    int insertSelective(SysURolePermissionDO record);
}