package com.intelligence.edge.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Maps;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import org.apache.commons.lang3.StringUtils;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author shik2
 * @date 2020/06/27
 **/
@ServerEndpoint("/imserver/{carID}")
@Component
@Slf4j(topic = "c.WebSocketServer")
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收carID*/
    private String carID="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("carID") String carID) {
        this.session = session;
        this.carID=carID;
        if(webSocketMap.containsKey(carID)){
            webSocketMap.remove(carID);
            webSocketMap.put(carID,this);
            //加入set中
        }else{
            webSocketMap.put(carID,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+carID+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+carID+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(carID)){
            webSocketMap.remove(carID);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+carID+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+carID+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromcarID",this.carID);
                String tocarID=jsonObject.getString("tocarID");
                //传送给对应tocarID用户的websocket
                if(StringUtils.isNotBlank(tocarID)&&webSocketMap.containsKey(tocarID)){
                    webSocketMap.get(tocarID).sendMessage(jsonObject.toJSONString());
                }else{
                    log.error("请求的carID:"+tocarID+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.carID+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("carID") String carID) throws IOException {
        log.info("发送消息到:"+carID+"，报文:"+message);
        if(StringUtils.isNotBlank(carID)&&webSocketMap.containsKey(carID)){
            webSocketMap.get(carID).sendMessage(message);
        }else{
            log.error("设备："+carID+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}