package com.yequan.user.dao;

import com.yequan.pojo.entity.SysRoleDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleDOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleDO record);

    int insertSelective(SysRoleDO record);

    SysRoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleDO record);

    int updateByPrimaryKey(SysRoleDO record);
}