package com.yequan.user.controller.manager;

import com.yequan.common.websocket.handler.ChatWebSocketHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: yq
 * @Date: 2019/9/24 15:10
 * @Description:
 */
@RestController
@RequestMapping("websocket/")
public class WebsocketController {

    @Resource(name = "chatWebSocketHandler")
    private ChatWebSocketHandler handler;

    @GetMapping("send")
    public void send(HttpServletRequest request){

    }

}
