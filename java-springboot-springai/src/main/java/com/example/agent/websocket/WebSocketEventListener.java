package com.example.agent.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpUserRegistry userRegistry;

    public WebSocketEventListener(SimpUserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : "anonymous";
        String sessionId = headerAccessor.getSessionId();
        
        logger.info("WebSocket connection established: user={}, sessionId={}", username, sessionId);
        
        // Send connection acknowledgment
        // This would be handled by the controller
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : "anonymous";
        String sessionId = headerAccessor.getSessionId();
        
        logger.info("WebSocket connection closed: user={}, sessionId={}", username, sessionId);
    }

    public int getConnectedUsersCount() {
        return userRegistry.getUserCount();
    }

    public SimpUser getUser(String username) {
        return userRegistry.getUser(username);
    }
}
