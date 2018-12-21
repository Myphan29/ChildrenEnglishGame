package com.example.us.childrenenglishgame;

import java.util.Date;

public class User {

    public String username;
    public String email;
    public String token;
    public int score;

    public User(String username, String email, String token, int score) {
        this.username = username;
        this.email = email;
        this.token = token;
        this.score = score;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void clearInfo() {
        this.username = null;
        this.email = null;
        this.token = null;
        this.score = 0;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public int getScore() {
        return score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
