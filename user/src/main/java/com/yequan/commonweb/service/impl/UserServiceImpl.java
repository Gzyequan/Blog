package com.yequan.commonweb.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yequan.commonweb.dao.UserMapper;
import com.yequan.commonweb.pojo.User;
import com.yequan.commonweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public int insertSelective(User user) {
        if (null == user) {
            return 0;
        }
        return userMapper.insertSelective(user);
    }

    public List<User> selectUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectUserList();
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        return pageInfo.getList();
    }

    public int updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public int deleteUserById(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }
}
