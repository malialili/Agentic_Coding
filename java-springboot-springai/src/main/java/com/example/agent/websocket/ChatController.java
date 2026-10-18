package com.example.agent.websocket;

import com.example.agent.service.AgentService;
import com.example.agent.service.MistralService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MistralService mistralService;
    private final AgentService agentService;
    private final WebSocketEventListener eventListener;

    public ChatController(MistralService mistralService,
                         AgentService agentService,
                         WebSocketEventListener eventListener) {
        this.mistralService = mistralService;
        this.agentService = agentService;
        this.eventListener = eventListener;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                              SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("sessionId", chatMessage.getSessionId());
        
        return chatMessage;
    }

    @MessageMapping("/agent.execute")
    @SendToUser("/queue/replies")
    public ChatMessage executeAgent(@Payload ChatMessage chatMessage,
                                   SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = (String) headerAccessor.getSessionAttributes().get("sessionId");
        
        // Send thinking message
        ChatMessage thinkingMessage = new ChatMessage(
            ChatMessage.MessageType.AGENT_THINKING,
            "Agent is thinking...",
            "Agent",
            sessionId
        );
        
        // Execute agent and get response
        String agentResponse = agentService.executeAgent(chatMessage.getContent());
        
        ChatMessage responseMessage = new ChatMessage(
            ChatMessage.MessageType.AGENT_RESPONSE,
            agentResponse,
            "Agent",
            sessionId
        );
        
        return responseMessage;
    }

    @MessageMapping("/agent.stream")
    public void streamAgent(@Payload ChatMessage chatMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = (String) headerAccessor.getSessionAttributes().get("sessionId");
        
        // For streaming, we would use Spring AI's streaming capabilities
        // This is a simplified version
        
        String agentResponse = agentService.executeAgent(chatMessage.getContent());
        
        ChatMessage responseMessage = new ChatMessage(
            ChatMessage.MessageType.AGENT_RESPONSE,
            agentResponse,
            "Agent",
            sessionId
        );
        
        // Send directly to user's queue
        headerAccessor.getMessageChannel().send(
            org.springframework.messaging.MessageBuilder
                .withPayload(responseMessage)
                .setHeader("simpMessageType", SimpMessageType.MESSAGE)
                .build()
        );
    }

    @MessageMapping("/chat.getConnectedUsers")
    @SendTo("/topic/public")
    public ChatMessage getConnectedUsers(@Payload ChatMessage chatMessage) {
        int count = eventListener.getConnectedUsersCount();
        chatMessage.setContent("Connected users: " + count);
        return chatMessage;
    }
}
