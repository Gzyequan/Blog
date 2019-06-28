package com.yequan.commonweb.controller;

import com.yequan.commonweb.pojo.User;
import com.yequan.commonweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: yq
 * @Date: 2019/6/28 14:07
 * @Description:
 */
@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        try {
            User user = iUserService.selectUserById(id);
            if (null == user) {
                //资源不存在,响应404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //响应200
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<User> saveUser(User user) {
        try {
            User newCreateUser = iUserService.insertSelective(user);
            if (null == newCreateUser) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
