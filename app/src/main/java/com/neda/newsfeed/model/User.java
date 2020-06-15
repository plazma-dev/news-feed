package com.neda.newsfeed.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * User model
 */
public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    //region Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    //endregion

    @Override
    @NonNull
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
