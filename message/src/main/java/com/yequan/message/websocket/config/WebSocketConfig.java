package com.yequan.message.websocket.config;

import com.yequan.message.websocket.handler.SocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

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
        registry.addHandler(SocketHandler(), "/socketServer").addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(SocketHandler(), "/sockjs/socketServer").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler SocketHandler() {
        return new SocketHandler();
    }

    /**
     * @param @return
     * @return ServletServerContainerFactoryBean
     * @Title: createWebSocketContainer
     * @Description: 配置webSocket引擎    用于tomcat 可以不配置
     * @date createTime：2018年4月26日上午11:18:28
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
