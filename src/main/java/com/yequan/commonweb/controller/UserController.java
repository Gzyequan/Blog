package com.yequan.commonweb.controller;

import com.yequan.commonweb.pojo.User;
import com.yequan.commonweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/6/28 14:07
 * @Description:
 */
@RestController
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/user/{pageNum}/{pageSize}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<User>> getUsers(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        try {
            List<User> users = iUserService.selectUserList(pageNum, pageSize);
            if (users != null && users.size() > 0) {
                return ResponseEntity.ok(users);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> saveUser(@RequestBody User user) {
        try {
            int count = iUserService.insertSelective(user);
            if (count > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        try {
            User currentUser = iUserService.selectUserById(id);
            if (null == currentUser) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (null != user) {
                user.setId(id);
                int update = iUserService.updateUser(user);
                if (update > 0) {
                    User updatedUser = iUserService.selectUserById(id);
                    if (null != updatedUser) {
                        return ResponseEntity.ok(updatedUser);
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Integer id) {
        try {
            User user = iUserService.selectUserById(id);
            if (null==user){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            int delete=iUserService.deleteUserById(id);
            if (delete>0){
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
