package com.yequan.user.dao;

import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);

    List<UserDO> selectUserList();

    UserDO selectByMobilephone(UserDTO userDTO);
}