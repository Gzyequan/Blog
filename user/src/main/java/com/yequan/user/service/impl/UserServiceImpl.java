package com.yequan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yequan.common.util.MD5Util;
import com.yequan.user.dao.UserMapper;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import com.yequan.user.service.IUserService;
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


    public UserDO selectUserById(Integer id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增用户
     * 对用户密码进行md5摘要
     *
     * @param userDO
     * @return
     */
    public int insertSelective(UserDO userDO) {
        if (null == userDO) {
            return 0;
        }
        String password = userDO.getPassword();
        String md5Password = MD5Util.encrypt(password);
        userDO.setPassword(md5Password);
        return userMapper.insertSelective(userDO);
    }

    public List<UserDO> selectUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDO> userDOList = userMapper.selectUserList();
        PageInfo<UserDO> pageInfo = new PageInfo<UserDO>(userDOList);
        return pageInfo.getList();
    }

    public int updateUser(UserDO userDO) {
        return userMapper.updateByPrimaryKeySelective(userDO);
    }

    public int deleteUserById(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 登录校验:
     * 1.手机号密码校验
     * 2.用户状态校验:已注销用户返回空
     *
     * @param userDTO
     * @return
     */
    public UserDO loginCheck(UserDTO userDTO) {
        String password = userDTO.getPassword();
        String md5Password = MD5Util.encrypt(password);
        userDTO.setPassword(md5Password);
        return userMapper.loginCheck(userDTO);
    }

    /**
     * 根据手机号查询用户
     *
     * @param userDTO
     * @return
     */
    public UserDO selectByMobilephone(UserDTO userDTO) {
        UserDO userDO = null;
        if (null != userDTO) {
            userDO = userMapper.selectByMobilephone(userDTO);
        }
        return userDO;
    }

}
