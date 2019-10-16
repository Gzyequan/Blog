package com.yequan.user.dao;

import com.yequan.pojo.dto.UserDTO;
import com.yequan.pojo.entity.SysUserDO;
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

    SysUserDO selectByMobilephone(String mobilephone);

    SysUserDO loginCheck(UserDTO userDTO);
}