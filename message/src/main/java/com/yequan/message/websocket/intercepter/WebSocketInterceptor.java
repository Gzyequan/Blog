package com.yequan.message.websocket.intercepter;

import com.yequan.common.util.Logger;
import com.yequan.user.pojo.dbo.SysUserDO;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/9/25 16:04
 * @Description:
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception exceptions) {
        Logger.debug("=================执行 afterHandshake ：handler: "+handler+"exceptions: "+exceptions);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
                                   Map<String, Object> map) throws Exception {
        Logger.debug("==================执行 beforeHandshake ：handler: "+handler+"map: "+map.values());
        if(request instanceof ServerHttpRequest){
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            if(session!=null){
                SysUserDO user=(SysUserDO)session.getAttribute("user");
                map.put(String.valueOf(user.getId()), user);
            }
        }
        return true;
    }

}
