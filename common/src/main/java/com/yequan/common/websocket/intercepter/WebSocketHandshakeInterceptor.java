package com.yequan.common.websocket.intercepter;

import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/9/24 14:25
 * @Description: handler处理器的拦截器
 */
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String userName = servletRequest.getParameter("userName");
            Integer userId = CurrentUserLocal.getUserId();
            if (null != userId) {
                attributes.put("userId", userId);
                attributes.put("userName", userName);
                Logger.debug("userId :{}  userName :{}", userId, userName);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
