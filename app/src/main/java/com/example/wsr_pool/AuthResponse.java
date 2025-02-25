package com.example.wsr_pool;

public class AuthResponse {
    public String status;
    public String message;
    public String token;
    public Users user;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public Users getUser() {
        return user;
    }
}
