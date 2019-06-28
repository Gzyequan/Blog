package com.yequan.commonweb.service.impl;

import com.yequan.commonweb.dao.UserMapper;
import com.yequan.commonweb.pojo.User;
import com.yequan.commonweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    public User selectUserById(Integer id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectByPrimaryKey(id);
    }

    public User insertSelective(User user) {
        if (null == user) {
            return null;
        }
        int count = userMapper.insertSelective(user);
        if (count > 0) {
            return userMapper.selectByPrimaryKey(user.getId());
        }
        return null;
    }
}
