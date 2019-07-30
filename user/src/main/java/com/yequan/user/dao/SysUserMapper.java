package com.yequan.user.dao;

import com.yequan.user.pojo.dbo.SysUserDO;
import com.yequan.user.pojo.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserDO record);

    int insertSelective(SysUserDO record);

    SysUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserDO record);

    int updateByPrimaryKey(SysUserDO record);

    List<SysUserDO> selectUserList();

    SysUserDO selectByMobilephone(UserDTO userDTO);

    SysUserDO loginCheck(UserDTO userDTO);
}