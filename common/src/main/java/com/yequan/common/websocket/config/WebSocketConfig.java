package com.yequan.common.websocket.config;

import com.yequan.common.websocket.handler.ChatWebSocketHandler;
import com.yequan.common.websocket.intercepter.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @Auther: yq
 * @Date: 2019/9/24 14:09
 * @Description: websocket入口类
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Resource(name = "chatWebSocketHandler")
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //允许连接的域
        String[] allowedOrigins = {"http://www.xxx.com"};

        //注册WebSocket通道
        registry.addHandler(chatWebSocketHandler,"/webSocketIMServer").setAllowedOrigins(allowedOrigins).addInterceptors(websocketInterceptor());
    }

    private WebSocketHandshakeInterceptor websocketInterceptor(){
        return new WebSocketHandshakeInterceptor();
    }

}
