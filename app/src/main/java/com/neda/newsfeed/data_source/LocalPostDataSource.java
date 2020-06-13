package com.neda.newsfeed.data_source;

import com.neda.newsfeed.data_source.PostDataSource;
import com.neda.newsfeed.db.PostDao;
import com.neda.newsfeed.model.Post;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Using the Room database as a data source.
 */
public class LocalPostDataSource implements PostDataSource {

    private final PostDao postDao;

    public LocalPostDataSource(PostDao postDao) {
        this.postDao = postDao;
    }

    @Override
    public Flowable<List<Post>> getAllPosts() {
        return postDao.getAllPosts();
    }

    @Override
    public Flowable<Post> getPost(String postId) {
        return postDao.getPost(postId);
    }

    @Override
    public Completable insertPosts(List<Post> posts) {
        return postDao.insertPosts(posts);
    }

    @Override
    public void deleteAllPosts() {
        postDao.deletePosts();
    }

    @Override
    public Completable deletePost(String postId) {
        return postDao.deletePost(postId);
    }
}
