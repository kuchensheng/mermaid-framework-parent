package com.mermaid.framework.websocket.declaration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mermaid.framework.websocket.constant.EnumMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Component
@ServerEndpoint(value = "${mermaid.websocket.server.endpoint.uri:/websocket")
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, WebSocketServer> webSocketServers = new ConcurrentHashMap<>();

    private WebSocketSession webSocketSession;

    private Session session;

    @Resource
    private AbstractMessageHandler handler;

    @Value("${mermaid.websocket.connect.welcome:connect successed}")
    private String welcome;
    @Value("${mermaid.websocket.disconnect:disconnect successed}")
    private String disconnect;

    @Value("${mermaid.websocket.clientId:userId}")
    private static String sessionClientId;

    @OnOpen
    public void init(WebSocketSession session) {
        setWebSocketSession(session);
        String clientId = getClientId(session);
        webSocketServers.put(clientId, this);
        addOnlineCount();
        logger.info("new session is connected! now online session is {}", getOnlineCount());
        try {
            sendTextMessage(welcome);
        } catch (IOException e) {
            logger.error("IO异常", e);
        }
    }

    private String getClientId(WebSocketSession session) {
        String clientId = (String) session.getAttributes().get(sessionClientId);
        return clientId;
    }

    /**
     * 接收消息，并将消息转发，message格式为json.
     * {"transfer":false,"recivedUserIds":["001","002"],"msgType":"text","message","messgeObject"}
     *
     * @param messageJson
     */
    @OnMessage
    public void onMessage(String messageJson) throws Exception {
        logger.info("recived client message : {}", messageJson);
        JSONObject json = JSON.parseObject(messageJson);
        if (!json.keySet().contains("transfer") || !json.keySet().contains("recivedUserIds")
                || !json.keySet().contains("msgType") || !json.keySet().contains("message"))
            throw new Exception("json text is unvalidatated");
        Boolean transfer = json.getBoolean("transfer");
        JSONArray userIds = json.getJSONArray("recivedUserIds");
        String msgType = json.getString("msgType");
        EnumMessageType messageType = EnumMessageType.valueOf(msgType);
        String message = json.getString("message");
        if (null == transfer || !transfer) {
            handler.handlerMessage(messageType, message);
            handler.handlerMessageWithReturn(messageType, message);
        } else {
            if (null == userIds || userIds.size() == 0) {
                sendAllMessage(messageType, message);
            } else {
                for (Object u : userIds) {
                    String userId = (String) u;

                    sendMessageToUser(userId, messageType, message);
                }
            }
        }
    }

    @OnClose
    public void close() {
        webSocketServers.remove(getClientId(getWebSocketSession()));
        subOnlineCount();
        logger.info("one session closed success");
        try {
            sendMessage(disconnect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        getWebSocketSession().sendMessage(new TextMessage(message));
    }

    private void sendMessageToUser(String userId, EnumMessageType msgType, String message) throws Exception {
        WebSocketServer webSocketServer = webSocketServers.get(userId);
        WebSocketSession toSession = webSocketServer.webSocketSession;
        try {
            switch (msgType) {
                case TEXT:
                    toSession.sendMessage(new TextMessage(String.valueOf(message)));
                    break;
                case BYTE:
                    toSession.sendMessage(new BinaryMessage(message.getBytes()));
                    break;
                case PONG:
                    toSession.sendMessage(new PongMessage());
                    break;
                default:
                    logger.error("unvalidate message type {}", msgType);
                    throw new Exception("unvalidate message type,allow message type is in " + EnumMessageType.values());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllMessage(EnumMessageType msgType, String message) throws Exception {
        String currentUserId = this.getClientId(getWebSocketSession());
        for (Map.Entry<String, WebSocketServer> entry : webSocketServers.entrySet()) {
            String userId = entry.getKey();
            if (currentUserId.equals(userId))
                continue;
            sendMessageToUser(userId, msgType, message);
        }
    }


    public void sendTextMessage(String welcome) throws IOException {
        this.webSocketSession.sendMessage(new TextMessage(welcome));
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static int getOnlineCount() {
        return onlineCount;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}


