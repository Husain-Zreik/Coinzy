package Coinzy.models;

import java.time.LocalDate;

public class Chat {
    private int id;
    private int ownerId; // Changed from initiatorId to ownerId
    private int memberId; // Changed from participantId to memberId
    private LocalDate chatDate; // Added chatDate

    // Default constructor
    public Chat() {
    }

    // Parameterized constructor
    public Chat(int id, int ownerId, int memberId, LocalDate chatDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.memberId = memberId;
        this.chatDate = chatDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getChatDate() {
        return chatDate;
    }

    public void setChatDate(LocalDate chatDate) {
        this.chatDate = chatDate;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", memberId=" + memberId +
                ", chatDate=" + chatDate +
                '}';
    }
}
