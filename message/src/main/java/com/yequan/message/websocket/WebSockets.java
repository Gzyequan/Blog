package com.yequan.message.websocket;

import com.alibaba.fastjson.JSON;
import com.yequan.user.pojo.dbo.SysUserDO;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: yq
 * @Date: 2019/9/25 16:31
 * @Description:
 */
@ServerEndpoint("/websocket/{userId}/{cnName}")
public class WebSockets {
    private static int onlineCount = 0;
    public static Map<SysUserDO, WebSockets> clients = new ConcurrentHashMap<SysUserDO, WebSockets>();
    private Session session;
    private Integer userId;//用户id
    private String cnName;//用户真实名称
    private SysUserDO user;//用户实体
    public static List<SysUserDO> list = new ArrayList<>();   //在线列表,记录用户名称

    /**
     *@description: 连接websocket
     *@author:cyb
     *@date: 2018-12-03 19:12
     *@param: userId 用户id
    *@param: cnName 用户真实名称
    *@param: session
     *@return: void
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Integer userId, @PathParam("cnName") String cnName, Session session) throws IOException {
        this.userId = userId;
        this.session = session;
        this.cnName=cnName;
        System.out.println("cnName:"+cnName+"userId"+userId);
        user= new SysUserDO();
        user.setId(userId);
        user.setNickname(cnName);
        list.add(user);
        addOnlineCount();//添加在线人数
        clients.put(user, this);
       /* String message = getMessage("["+cnName+"]进入聊天室,当前在线人数为"+getOnlineCount()+"位", "notice",  list);
        broadcast(message);//调用广播
        System.out.println("已连接");*/
    }
    /**
     * 广播消息
     * @param message
     */
    public void broadcast(String message){
        for (WebSockets item : clients.values()) {
            String cnName = item.cnName;
            //
            String msg = message.replace("{userName}",cnName);
            item.session.getAsyncRemote().sendText(msg);
        }
    }
    /**
     * 组装返回给前台的消息
     * @param message   交互信息
     * @param type      信息类型
     * @param list      在线列表
     * @return
     */
    public String getMessage(String message, String type, List list){
        com.alibaba.fastjson.JSONObject member = new com.alibaba.fastjson.JSONObject();
        member.put("message", message);
        member.put("type", type);
        member.put("list", list);
        return member.toString();
    }
    /**
     * 对特定用户发送消息
     * @param message
     * @param session
     */
    public void singleSend(String message, Session session){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *@description: 关闭连接
     *@author:cyb
     *@date: 2018-12-03 14:33
     *@param:
     *@return: void
     */
    @OnClose
    public void onClose() throws IOException {
        System.out.println("关闭当前用户连接："+user.getNickname());
        clients.remove(user);
        list.remove(user);        //从在线列表移除这个用户
        subOnlineCount();
    }
    /**
     *@description:前台发送过来的消息
     *@author:cyb
     *@date: 2018-12-03 14:33
     *@param: _message
     *@return: void
     */
    @OnMessage
    public void onMessage(String _message) throws IOException {
        com.alibaba.fastjson.JSONObject chat = JSON.parseObject(_message);
        com.alibaba.fastjson.JSONObject message = JSON.parseObject(chat.get("message").toString());
        if(message.get("to") == null || message.get("to").equals("")){
            //如果to为空,则广播;如果不为空,则对指定的用户发送消息
            broadcast(_message);
        }else{
            String [] userlist = message.get("to").toString().split(",");
            sendMessageTo(_message,message.get("from").toString());
//            singleSend(_message, (Session) routetab.get(message.get("from"))); //发送给自己
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    /**
     *@description:消息发送给指定人员
     *@author:cyb
     *@date: 2018-12-03 14:35
     *@param: message
    *@param: To
     *@return: void
     */
    public void sendMessageTo(String message, String To) throws IOException {
        for (WebSockets item : clients.values()) {
            if (item.userId.equals(To) )
                item.session.getAsyncRemote().sendText(message);
        }
    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSockets.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSockets.onlineCount--;
    }
    public static synchronized Map<SysUserDO, WebSockets> getClients() {
        return clients;
    }
}
