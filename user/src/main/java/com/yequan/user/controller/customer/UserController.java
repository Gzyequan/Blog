package com.yequan.user.controller.customer;

import com.yequan.common.annotation.AccessLimit;
import com.yequan.common.application.AppConstant;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.jms.service.JmsProducerService;
import com.yequan.common.quartz.SchedulerService;
import com.yequan.common.quartz.proxy.AsyncJobProxy;
import com.yequan.user.pojo.UserDO;
import com.yequan.user.pojo.UserDTO;
import com.yequan.user.service.IUserService;
import com.yequan.user.test.task.NetworkMonitor;
import com.yequan.user.test.task.NetworkMonitorListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;
import javax.servlet.http.HttpSession;
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
    public AppResult<List<UserDO>> getUsers(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        try {
            if (null == pageNum || null == pageSize) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            List<UserDO> userDOS = iUserService.selectUserList(pageNum, pageSize);
            if (userDOS != null && userDOS.size() > 0) {
                return AppResultBuilder.success(userDOS, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.RESULE_DATA_NONE);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> getUserById(@PathVariable("id") Integer id) {
        try {
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDO userDO = iUserService.selectUserById(id);
            if (null != userDO) {
                return AppResultBuilder.success(userDO, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.RESULE_DATA_NONE);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> register(@RequestBody UserDO userDO) {
        try {
            if (null == userDO) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setMobilephone(userDO.getMobilephone());
            //判断手机号是否已被注册
            UserDO dbUser = iUserService.selectByMobilephone(userDTO);
            if (null != dbUser) {
                return AppResultBuilder.failure(ResultCode.USER_MOBILE_EXISTED);
            }
            int insert = iUserService.insertSelective(userDO);
            if (insert > 0) {
                UserDO newUser = iUserService.selectByMobilephone(userDTO);
                return AppResultBuilder.success(newUser, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_CREATE_ERROR);
    }

    /**
     * 更新用户信息:只能更新当前登录用户自己的信息
     *
     * @param id
     * @param userDO
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public AppResult<UserDO> updateUser(HttpSession session, @PathVariable("id") Integer id, @RequestBody UserDO userDO) {
        try {
            if (null == userDO || null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_NOT_COMPLETE);
            }
            UserDO user = (UserDO) session.getAttribute(AppConstant.SessionConstant.SESSION_USER_KEY);
            if (!user.getId().equals(id)) {
                return AppResultBuilder.failure(ResultCode.PERMISSION_NO_ACCESS);
            }
            UserDO currentUserDO = iUserService.selectUserById(id);
            if (null == currentUserDO) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            userDO.setId(id);
            int update = iUserService.updateUser(userDO);
            if (update > 0) {
                UserDO updatedUserDO = iUserService.selectUserById(id);
                return AppResultBuilder.success(updatedUserDO, ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_UPDATE_ERROR);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public AppResult deleteUserById(@PathVariable("id") Integer id) {
        try {
            if (null == id) {
                return AppResultBuilder.failure(ResultCode.PARAM_IS_BLANK);
            }
            UserDO userDO = iUserService.selectUserById(id);
            if (null == userDO) {
                return AppResultBuilder.failure(ResultCode.USER_NOT_EXIST);
            }
            int delete = iUserService.deleteUserById(id);
            if (delete > 0) {
                return AppResultBuilder.successNoData(ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResultBuilder.failure(ResultCode.USER_DELETE_ERROR);
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
        return AppResultBuilder.successNoData(ResultCode.SUCCESS);
    }

    @AccessLimit(limit = 4, sec = 4)
    @RequestMapping(value = "/message", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public AppResult<Void> sendMessage() {
        jmsProducerService.sendMessage(destination, "这是一条消息");
        return AppResultBuilder.successNoData(ResultCode.SUCCESS);
    }
}
