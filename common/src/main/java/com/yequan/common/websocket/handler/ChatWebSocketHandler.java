package com.yequan.common.websocket.handler;

import com.yequan.common.util.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/9/24 14:47
 * @Description:
 */
@Service("chatWebSocketHandler")
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final static List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    /**
     * 接收文本信息并发送
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //接收的消息
        String payload = message.getPayload();
        Logger.debug(payload);
        TextMessage textMessage = new TextMessage("reply message: " + payload);
        //发送消息
        session.sendMessage(textMessage);
        String userName = (String) session.getAttributes().get("userName");
        sendMessageToUser(userName, textMessage);
    }

    /**
     * 建立连接后处理
     *
     * @param session
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Logger.debug("connect to the websocket chat success......");
        sessions.add(session);
    }

    /**
     * 抛出异常
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        Logger.debug("websocket chat connection closed......");
        sessions.remove(session);
    }

    /**
     * 连接关闭后
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Logger.debug("websocket chat connection closed......");
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        for (WebSocketSession session : sessions) {
            if (session.getAttributes().get("userName").equals(userName)) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    Logger.error(e.getMessage(), e);
                }
                break;
            }
        }
    }

}
