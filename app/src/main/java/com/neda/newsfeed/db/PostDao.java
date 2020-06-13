package com.neda.newsfeed.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.neda.newsfeed.model.Post;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Data Access Object for the posts table.
 */
@Dao
public interface PostDao {
    /**
     * Get posts from the table.
     *
     * @return posts from the table
     */
    @Query("SELECT * FROM posts")
    Flowable<List<Post>> getAllPosts();

    /**
     * Get single post from the table
     *
     * @param postId id
     * @return post with given id
     */
    @Query("SELECT * FROM posts WHERE id=:postId")
    Flowable<Post> getPost(String postId);

    /**
     * Insert list of post to the table
     *
     * @param posts posts to insert
     * @return completion of the insert query
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPosts(List<Post> posts);

    /**
     * Delete all posts.
     */
    @Query("DELETE FROM posts")
    void deletePosts();

    /**
     * Delete post with given id from the table
     *
     * @param postId post id
     * @return completion of the delete query
     */
    @Query("DELETE FROM posts WHERE id=:postId")
    Completable deletePost(String postId);
}
