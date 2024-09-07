package Coinzy.models;

import java.sql.Timestamp;

public class Announcement {
    private int id;
    private String title;
    private String message;
    private int createdBy;
    private Timestamp timestamp;

    // Default constructor
    public Announcement() {
    }

    // Parameterized constructor
    public Announcement(int id, String title, String message, int createdBy, Timestamp timestamp) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdBy = createdBy;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", createdBy=" + createdBy +
                ", timestamp=" + timestamp +
                '}';
    }
}
