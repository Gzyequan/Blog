package com.yequan.user.service;

import com.yequan.user.pojo.User;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
public interface IUserService {

    User selectUserById(Integer id);

    int insertSelective(User user);

    List<User> selectUserList(int pageNum, int pageSize);

    int updateUser(User user);

    int deleteUserById(Integer id);
}
