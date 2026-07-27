package org.example.tidaswebmanagement.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{userId}")
public class ChatEndpoint {

    private static ConcurrentHashMap<String, Session> onlineUsers = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        onlineUsers.put(userId, session);
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        onlineUsers.remove(userId);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        // message格式: receiverId|内容
        String[] parts = message.split("\\|", 2);
        if (parts.length < 2) return;
        String receiverId = parts[0];
        String content = parts[1];
        Session receiverSession = onlineUsers.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.getAsyncRemote().sendText(userId + "|" + content);
        }
    }
}