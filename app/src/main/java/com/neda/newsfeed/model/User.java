package com.neda.newsfeed.model;

import androidx.annotation.NonNull;

/**
 * User model
 */
public class User {
    private int id;
    private String name;
    private String username;
    private String email;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    @NonNull
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
