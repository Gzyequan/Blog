package com.yequan.commonweb.service;

import com.yequan.commonweb.pojo.User;

/**
 * @Auther: yq
 * @Date: 2019/6/28 11:13
 * @Description:
 */
public interface IUserService {

    User selectUserById(Integer id);

    User insertSelective(User user);

}
