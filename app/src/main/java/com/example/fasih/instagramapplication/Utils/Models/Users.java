package com.example.fasih.instagramapplication.Utils.Models;

/**
 * Created by Fasih on 10/20/18.
 */

public class Users {
    private String userID;
    private long phone_number;
    private String email;
    private String username;

    public Users() {
    }

    public Users(String userID, long phone_number, String email, String username) {
        this.userID = userID;
        this.phone_number = phone_number;
        this.email = email;
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
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

    @Override
    public String toString() {
        return "Users{" +
                "userID='" + userID + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
