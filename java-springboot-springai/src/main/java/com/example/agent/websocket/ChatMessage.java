package com.example.agent.websocket;

import java.time.LocalDateTime;

public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String sessionId;
    private LocalDateTime timestamp;

    public enum MessageType {
        CHAT, JOIN, LEAVE, AGENT_RESPONSE, AGENT_THINKING
    }

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(MessageType type, String content, String sender, String sessionId) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.sessionId = sessionId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
