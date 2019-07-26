package com.yequan.user.controller.customer;

import com.yequan.common.annotation.AccessLimit;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.jms.service.JmsProducerService;
import com.yequan.common.quartz.SchedulerService;
import com.yequan.common.quartz.proxy.AsyncJobProxy;
import com.yequan.common.redis.RedisService;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.service.IOrdinaryUserService;
import com.yequan.user.test.task.NetworkMonitor;
import com.yequan.user.test.task.NetworkMonitorListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;

/**
 * @Auther: yq
 * @Date: 2019/6/28 14:07
 * @Description:
 */
@RestController
@RequestMapping("user/")
public class OrdinaryUserController {

    @Autowired
    private IOrdinaryUserService iOrdinaryUserService;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private JmsProducerService jmsProducerService;

    @Autowired
    private RedisService redisService;

    @Autowired
    @Qualifier("queueDestination")
    private Destination destination;

    /**
     * 根据用户主键id查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> getCurrentUserById(@PathVariable("id") Integer id) {
        return iOrdinaryUserService.selectUserById(id);
    }

    /**
     * 注销用户
     *
     * @param id
     * @return
     */
    @GetMapping(value = "unregister/{id}", produces = "application/json;charset=UTF-8")
    public AppResult<String> unregisterCurrentUser(@PathVariable Integer id) {
        return iOrdinaryUserService.unregisterUser(id);
    }

    /**
     * 更新用户信息:只能更新当前登录用户自己的信息
     *
     * @param id
     * @param userDO
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> updateCurrentUser(@PathVariable("id") Integer id, @RequestBody UserDO userDO) {
        return iOrdinaryUserService.updateUser(id, userDO);
    }

    @RequestMapping(value = "/timer", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<Void> startQuartz() {
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
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }

    @AccessLimit(limit = 4, sec = 4)
    @RequestMapping(value = "/message", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<Void> sendMessage() {
        jmsProducerService.sendMessage(destination, "这是一条消息");
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }
}
