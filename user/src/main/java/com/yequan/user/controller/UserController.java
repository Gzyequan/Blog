package com.yequan.user.controller;

import com.yequan.common.annotation.AccessLimit;
import com.yequan.common.jms.service.JmsProducerService;
import com.yequan.common.quartz.SchedulerService;
import com.yequan.common.quartz.proxy.AsyncJobProxy;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.service.IUserService;
import com.yequan.user.test.task.NetworkMonitor;
import com.yequan.user.test.task.NetworkMonitorListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;
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

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private JmsProducerService jmsProducerService;

    @Autowired
    @Qualifier("queueDestination")
    private Destination destination;

    @RequestMapping(value = "/user/{pageNum}/{pageSize}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<UserDO>> getUsers(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {
        try {
            List<UserDO> userDOS = iUserService.selectUserList(pageNum, pageSize);
            if (userDOS != null && userDOS.size() > 0) {
                return ResponseEntity.ok(userDOS);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<UserDO> getUserById(@PathVariable("id") Integer id) {
        try {
            UserDO userDO = iUserService.selectUserById(id);
            if (null == userDO) {
                //资源不存在,响应404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //响应200
            return ResponseEntity.ok(userDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> saveUser(@RequestBody UserDO userDO) {
        try {
            int count = iUserService.insertSelective(userDO);
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
    public ResponseEntity<UserDO> updateUser(@PathVariable("id") Integer id, @RequestBody UserDO userDO) {
        try {
            UserDO currentUserDO = iUserService.selectUserById(id);
            if (null == currentUserDO) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (null != userDO) {
                userDO.setId(id);
                int update = iUserService.updateUser(userDO);
                if (update > 0) {
                    UserDO updatedUserDO = iUserService.selectUserById(id);
                    if (null != updatedUserDO) {
                        return ResponseEntity.ok(updatedUserDO);
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
            UserDO userDO = iUserService.selectUserById(id);
            if (null == userDO) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            int delete = iUserService.deleteUserById(id);
            if (delete > 0) {
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/timer", method = RequestMethod.GET)
    public ResponseEntity<Void> startQuartz() {
        final String host = "172.16.20.73";
        int pingTimes = 5;
        int timeout = 3000;
        NetworkMonitor networkMonitor = new NetworkMonitor(new NetworkMonitorListener() {
            public void onMessage(boolean isReachable) {
                System.out.println(isReachable ? (host + " 可达") : (host + " 不可达"));
            }
        }, host);
        schedulerService.startJob(
                "NetworkMonitor",
                "NetworkMonitor",
                AsyncJobProxy.class,
                "0/5 * * * * ?",
                networkMonitor);
        return ResponseEntity.ok().build();
    }

    @AccessLimit(limit = 4, sec = 4)
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public ResponseEntity<Void> sendMessage() {
        jmsProducerService.sendMessage(destination, "这是一条消息");
        return ResponseEntity.ok().build();
    }
}
