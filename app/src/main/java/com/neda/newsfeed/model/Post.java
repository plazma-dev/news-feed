package com.neda.newsfeed.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Post model
 * Used for {@link retrofit2.Retrofit} response and as {@link Entity}
 */
@Entity(tableName = "posts")
public class Post {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;
    @ColumnInfo(name = "userId")
    @SerializedName("userId")
    private String userId;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;
    @ColumnInfo(name = "body")
    @SerializedName("body")
    private String body;

    //region Getters
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
    //endregion

    //region Setters
    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }
    //endregion
}
