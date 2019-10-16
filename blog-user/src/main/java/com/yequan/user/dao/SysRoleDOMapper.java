package com.yequan.user.dao;

import com.yequan.pojo.dto.SysRoleDto;
import com.yequan.pojo.entity.SysRoleDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleDOMapper {

    int deleteByPrimaryKey(Integer id);

    int setSysRoleStatus(SysRoleDto sysRoleDto);

    int insert(SysRoleDO record);

    int insertSelective(SysRoleDO record);

    SysRoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleDO record);

    int updateByPrimaryKey(SysRoleDO record);

    List<SysRoleDO> getRoleByUserId(Integer userId);


}