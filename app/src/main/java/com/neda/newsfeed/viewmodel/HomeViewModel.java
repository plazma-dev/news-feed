package com.neda.newsfeed.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neda.newsfeed.data_source.PostDataSource;
import com.neda.newsfeed.Configuration;
import com.neda.newsfeed.api.NetworkService;
import com.neda.newsfeed.api.RetrofitService;
import com.neda.newsfeed.model.Post;

import java.util.List;
import java.util.Observable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private RetrofitService service = NetworkService.getInstance().getApi();
    private final PostDataSource dataSource;

    private String TAG = "HomeViewModel";

    private DisposableSingleObserver<List<Post>> disposableSingleObserver;
    private MutableLiveData<List<Post>> listMutableLiveData;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public HomeViewModel(PostDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LiveData<List<Post>> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
            loadPosts();
        }
        return listMutableLiveData;
    }

    }

    public void reloadPosts() {
        loadPosts();
    }

    public void loadPosts() {
        Single<List<Post>> allPosts = service.getAllPosts();
        disposableSingleObserver = allPosts.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        //listMutableLiveData.setValue(posts);
                        for (Post post : posts) {
                            post.setTimestamp(System.currentTimeMillis());
                        }
                        mDisposable.add(insertPosts(posts)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                            Log.d(TAG, "insert finish");
                                        },
                                        throwable -> Log.e(TAG, "Unable to update", throwable)));
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposableSingleObserver.dispose();
        mDisposable.clear();
    }
}
