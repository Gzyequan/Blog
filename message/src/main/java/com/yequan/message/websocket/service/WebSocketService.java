package com.yequan.message.websocket.service;


import com.yequan.common.util.Logger;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Auther: yq
 * @Date: 2019/9/27 09:32
 * @Description: @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@Service("websocket")
@ServerEndpoint("/websocket/{userId}")
public class WebSocketService {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Map，服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static ConcurrentHashMap<Integer, WebSocketService> webSocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        this.session=session;
        //加入set中
        webSocketMap.put(userId, this);
        //在线数加1
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") Integer userId) {
        webSocketMap.remove(userId);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("来自客户端的消息:" + message);
    }

    /**
     * 发生错误时调用
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 向某个用户发送消息
     *
     * @param userId
     * @param msg
     */
    public void sendMsgToUser(Integer userId, String msg) throws IOException {
        if (webSocketMap.containsKey(userId)) {
            WebSocketService webSocketService = webSocketMap.get(userId);
            webSocketService.sendMessage(msg);
        } else {
            Logger.error("用户{}未建立连接", userId);
        }
    }

    /**
     * 向所有用户发送消息
     *
     * @param msg
     * @throws IOException
     */
    public void sendMsgToAllUser(String msg) throws IOException {
        Set<Map.Entry<Integer, WebSocketService>> allConnection = webSocketMap.entrySet();
        for (Map.Entry<Integer, WebSocketService> connection : allConnection) {
            WebSocketService webSocketService = connection.getValue();
            webSocketService.sendMessage(msg);
        }
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

}

