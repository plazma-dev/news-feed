package com.neda.newsfeed.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neda.newsfeed.Configuration;
import com.neda.newsfeed.api.NetworkService;
import com.neda.newsfeed.api.RetrofitService;
import com.neda.newsfeed.data_source.PostDataSource;
import com.neda.newsfeed.model.Post;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private final String TAG = "HomeViewModel";

    //Network service instance, used for communication with api
    private RetrofitService service = NetworkService.getInstance().getApi();
    //Data source, in this case used for communication with database
    private final PostDataSource dataSource;

    //Api
    private DisposableSingleObserver<List<Post>> disposableSingleObserver;
    //Database
    private CompositeDisposable mDisposable = new CompositeDisposable();

    //Mutable live data observed in fragment
    private MutableLiveData<List<Post>> postListMutableLiveData;

    /**
     * Constructor
     * @param dataSource injected data source
     */
    public HomeViewModel(PostDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LiveData<List<Post>> getPostListMutableLiveData() {
        if (postListMutableLiveData == null) {
            postListMutableLiveData = new MutableLiveData<>();
            //Try to delete expired posts
            deleteExpiredPosts();
        }
        return postListMutableLiveData;
    }

    /**
     * Method first executes query for deleting expired posts
     * After completion, continues with post loading from database
     */
    public void deleteExpiredPosts() {
        Log.d(TAG, "Delete expired posts");
        mDisposable.add(dataSource.deleteExpiredPosts(System.currentTimeMillis() - Configuration.postLifetime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.d("Deleted expired posts", String.valueOf(result));
                            //Proceed with posts loading from database
                            //Cached posts have priority
                            loadPostsFromDatabase();
                        },
                        Throwable::printStackTrace));
    }

    /**
     * Initiate posts loading from database
     */
    public void loadPostsFromDatabase() {
        Log.d(TAG, "Load from DB");
        mDisposable.add(dataSource.getAllPosts()
                .map(posts -> posts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                            if (posts.size() > 0)
                                postListMutableLiveData.setValue(posts);
                            else {
                                //Fetch posts from api only if there are no posts cached
                                getPostsFromApi();
                            }
                        },
                        Throwable::printStackTrace));
    }

    /**
     * Get posts from api and save them to database
     */
    public void getPostsFromApi() {
        Log.d(TAG, "Get posts from api");
        Single<List<Post>> allPosts = service.getAllPosts();
        disposableSingleObserver = allPosts.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        //Add timestamp to all posts for later use
                        for (Post post : posts) {
                            post.setTimestamp(System.currentTimeMillis());
                        }
                        //Insert posts to database
                        mDisposable.add(insertPosts(posts)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                            Log.d(TAG, "Insert finished");
                                        },
                                        Throwable::printStackTrace));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Insert/update all posts
     *
     * @param posts posts to be inserted/updated
     * @return a {@link Completable} that completes when posts are inserted
     */
    public Completable insertPosts(final List<Post> posts) {
        return dataSource.insertPosts(posts);
    }

    /**
     * Perform cleanup
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        disposableSingleObserver.dispose();
        mDisposable.clear();
    }
}
