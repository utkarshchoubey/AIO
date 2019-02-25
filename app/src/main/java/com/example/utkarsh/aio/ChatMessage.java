package com.example.utkarsh.aio;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private Long messageTime;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime=new Date().getTime();

    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }
}
