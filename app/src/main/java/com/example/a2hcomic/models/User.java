package com.example.a2hcomic.models;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String avatar_url;
    private int role;
    private int status;
    private int create_at;

    // Constructor rỗng cần thiết cho Firebase
    public User() {
    }

    // Constructor đầy đủ
    public User(String id, String username, String email, String password, String avatar_url, int role, int status, int create_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar_url = avatar_url;
        this.role = role;
        this.status = status;
        this.create_at = create_at;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreate_at() {
        return create_at;
    }

    public void setCreate_at(int create_at) {
        this.create_at = create_at;
    }
}
