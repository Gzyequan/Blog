package com.yequan.message.websocket.controller;

import com.yequan.common.util.Logger;
import com.yequan.message.websocket.handler.MessageWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: yq
 * @Date: 2019/9/24 15:10
 * @Description:
 */
@RestController
@RequestMapping("websocket/")
public class WebsocketController {

    @Bean//这个注解会从Spring容器拿出Bean
    public MessageWebSocketHandler infoHandler() {
        return new MessageWebSocketHandler();
    }

    @GetMapping("send")
    public void send(HttpServletRequest request) {
        String username = request.getParameter("username");
        Logger.debug(username);
        TextMessage textMessage = new TextMessage("欲穷千里目,更上一层楼");
        infoHandler().sendMessageToUser(username, textMessage);
    }

}
