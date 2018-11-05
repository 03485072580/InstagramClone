package com.example.fasih.instagramapplication.Utils.Models;

/**
 * Created by Fasih on 10/24/18.
 */

public class MessageEvent {
    private String password;

    public MessageEvent(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
