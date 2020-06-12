package com.neda.newsfeed.api;

import com.neda.newsfeed.model.Post;
import com.neda.newsfeed.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("posts")
    Single<List<Post>> getAllPosts();

    @GET("posts/{postId}")
    Single<Post> getPost(@Path("postId") String postId);

    @GET("users/{userId}")
    Single<User> getUser(@Path("userId") String userId);
}
