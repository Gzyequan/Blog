package com.yequan.message.websocket.handler;

import com.yequan.common.util.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: yq
 * @Date: 2019/9/25 15:31
 * @Description:
 */
public class MessageWebSocketHandler extends TextWebSocketHandler {

    //存放在线用户
    private final static Map<String, WebSocketSession> userMap;

    static {
        userMap = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String userId = getUserId(webSocketSession);
        if (null == userMap.get(userId)) {
            userMap.put(String.valueOf(userId), webSocketSession);
            Logger.debug("WebSocket: 用户{}成功建立连接", userId);
        } else {
            Logger.debug("WebSocket: 用户{}已连接", userId);
        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        webSocketSession.sendMessage(webSocketMessage);
        Logger.debug("WebSocket: 发送消息");
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        Logger.debug("WebSocket: 发生异常: {}", throwable.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        Logger.debug("WebSocket: {} 已关闭", webSocketSession.getId());
        //关闭连接,将相应的连接删除
        String userId = getUserId(webSocketSession);
        if (userMap.get(userId) != null) {
            userMap.remove(userId);
            Logger.debug("WebSocket: {}用户已经移除", userId);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送信息给某个用户
     *
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        WebSocketSession session = userMap.get(userId);
        if (null != session && session.isOpen()) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给所有用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        Set<String> userIds = userMap.keySet();
        for (String userId : userIds) {
            sendMessageToUser(userId, message);
        }
    }

    private String getUserId(WebSocketSession session) {
        return session.getAttributes().get("user").toString();
    }

}
