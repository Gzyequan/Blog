package com.yequan.message.websocket.controller;

import com.yequan.common.util.DateUtil;
import com.yequan.message.websocket.service.WebSocketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Auther: yq
 * @Date: 2019/9/24 15:10
 * @Description:
 */
@RestController
@RequestMapping("websocket/")
public class WebsocketController {

    @Resource(name = "websocket")
    private WebSocketService webSocketService;

    @GetMapping(value = "one/{userId}", produces = "application/json;charset=UTF-8")
    public void sendMsgToUser(@PathVariable("userId") Integer userId) {
        try {
            webSocketService.sendMsgToUser(userId, "当前时间: " + DateUtil.getCurrentDateStr());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "all", produces = "application/json;charset=UTF-8")
    public void sendMsgToAllUser() {
        try {
            webSocketService.sendMsgToAllUser("群发消息--->当前时间: " + DateUtil.getCurrentDateStr());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
