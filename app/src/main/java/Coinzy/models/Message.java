package Coinzy.models;

import java.sql.Timestamp;

public class Message {
    private int id;
    private int chatId;
    private int senderId;
    private String message;
    private Timestamp timestamp;

    // Default constructor
    public Message() {
    }

    // Parameterized constructor
    public Message(int id, int chatId, int senderId, String message, Timestamp timestamp) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", senderId=" + senderId +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
