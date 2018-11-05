package com.example.fasih.instagramapplication.Utils.Models;

/**
 * Created by Fasih on 10/22/18.
 */

public class UserSettings {
    private Users users;
    private UserAccountSettings settings;

    public UserSettings(Users users, UserAccountSettings settings) {
        this.users = users;
        this.settings = settings;
    }

    public UserSettings() {
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public UserAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(UserAccountSettings settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "users=" + users +
                ", settings=" + settings +
                '}';
    }
}
