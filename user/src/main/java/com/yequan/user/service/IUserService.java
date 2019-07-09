package com.yequan.user.service;

import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
public interface IUserService {

    UserDO selectUserById(Integer id);

    int insertSelective(UserDO userDO);

    List<UserDO> selectUserList(int pageNum, int pageSize);

    int updateUser(UserDO userDO);

    int deleteUserById(Integer id);

    UserDO loginCheck(UserDTO userDTO);

    /**
     * 根据手机号查询用户
     *
     * @param userDTO
     * @return
     */
    public UserDO selectByMobilephone(UserDTO userDTO);
}
