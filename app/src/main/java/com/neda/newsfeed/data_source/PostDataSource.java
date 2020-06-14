package com.neda.newsfeed.data_source;

import com.neda.newsfeed.model.Post;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Access point for managing Post data.
 */
public interface PostDataSource {

    /**
     * Gets all posts from the data source
     *
     * @return posts from the data source
     */
    Flowable<List<Post>> getAllPosts();

    /**
     * Gets post from the data source
     *
     * @param postId id of the post to be fetched
     * @return post from the data source
     */
    Flowable<Post> getPost(String postId);

    /**
     * Inserts posts into the data source
     *
     * @param posts the posts to be inserted.
     */
    Completable insertPosts(List<Post> posts);

    /**
     * Deletes all posts from the data source.
     */
    void deleteAllPosts();

    /**
     * Deletes post from the data source
     *
     * @param postId id of the post to be deleted
     * @return
     */
    Completable deletePost(String postId);


    /**
     * Delete posts that are older than given time
     *
     * @param time given time in millis
     * @return number of rows affected by the query, in this case number of deleted posts from the table
     */
    Single<Integer> deleteExpiredPosts(long time);
}