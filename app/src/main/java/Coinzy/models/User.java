package Coinzy.models;

import java.sql.Timestamp;

public class User {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private int roleId;
    private String status;
    private Timestamp createdAt;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(int id, String name, String email, String username, String password, int roleId, String status,
            Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return switch (roleId) {
            case 1 -> "Admin";
            case 2 -> "Member";
            default -> "Unknown";
        };
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
