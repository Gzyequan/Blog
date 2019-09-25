package com.yequan.message.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @Auther: yq
 * @Date: 2019/9/25 16:36
 * @Description:
 */
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request, HandshakeResponse response) {
        // 获取当前Http连接的session
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        // 将http session信息注入websocket session
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
