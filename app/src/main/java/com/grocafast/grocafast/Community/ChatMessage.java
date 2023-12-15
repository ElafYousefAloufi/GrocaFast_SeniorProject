package com.grocafast.grocafast.Community;

public class ChatMessage {

    private final String sender;
    private final String text;
    private Long timestamp;


    public ChatMessage(String sender, String text, Long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp=timestamp;
    }

    public Long getTimestamp() {
        // Return the timestamp of the ChatMessage
        return timestamp;
    }

    public String getSender() {
        // Return the sender of the ChatMessage
        return sender;
    }

    public String getText() {
        // Return the text of the ChatMessage
        return text;
    }
}
