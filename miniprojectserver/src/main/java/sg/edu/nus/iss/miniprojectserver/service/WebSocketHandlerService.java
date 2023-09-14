package sg.edu.nus.iss.miniprojectserver.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class WebSocketHandlerService extends TextWebSocketHandler {
    
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        String email = session.getUri().getPath().substring(session.getUri().getPath().lastIndexOf('/') + 1);
        sessions.put(email, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Received message: " + message.getPayload());
    }

    public void sendNotificationToUser(String email, String message) throws IOException {
        WebSocketSession userSession = sessions.get(email);
        if (userSession != null && userSession.isOpen()) {
            userSession.sendMessage(new TextMessage(message));
        }
    }

}
