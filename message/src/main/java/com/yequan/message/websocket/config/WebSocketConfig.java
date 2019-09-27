package com.yequan.message.websocket.config;

import com.yequan.message.websocket.handler.MessageWebSocketHandler;
import com.yequan.message.websocket.intercepter.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Auther: yq
 * @Date: 2019/9/25 16:02
 * @Description:
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //浏览器支持websocket
        registry.addHandler(socketHandler(), "/socketServer").setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor());
        //浏览器不支持websocket,使用socketjs模拟websocket
        registry.addHandler(socketHandler(), "/sockjs/socketServer").setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor()).withSockJS();
    }

    @Bean
    public org.springframework.web.socket.WebSocketHandler socketHandler() {
        return new MessageWebSocketHandler();
    }
}
