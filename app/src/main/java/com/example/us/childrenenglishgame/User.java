package com.example.us.childrenenglishgame;

import java.util.Date;

public class User {

    public String username;
    public String email;
    public String password;
    public Date birdthday;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirdthday() {
        return birdthday;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirdthday(Date birdthday) {
        this.birdthday = birdthday;
    }
}
