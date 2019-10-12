package com.yequan.user.dao;

import com.yequan.pojo.entity.SysURolePermissionDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SysURolePermissionDOMapper {
    int insert(SysURolePermissionDO record);

    int insertSelective(SysURolePermissionDO record);
}