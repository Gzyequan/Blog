package com.yequan.message.websocket.intercepter;

import com.yequan.common.util.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/9/25 16:04
 * @Description:
 */
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception exceptions) {
        Logger.debug("=================执行 afterHandshake ：handler: " + handler + "exceptions: " + exceptions);
        super.afterHandshake(request, response, handler, exceptions);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
                                   Map<String, Object> map) throws Exception {
        Logger.debug("==================执行 beforeHandshake ：handler: " + handler + "map: " + map.values());
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String user = servletRequest.getParameter("user");
        map.put("user", user);
        return super.beforeHandshake(request, response, handler, map);
    }

}
