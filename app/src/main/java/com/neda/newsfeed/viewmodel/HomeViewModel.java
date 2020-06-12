package com.neda.newsfeed.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neda.newsfeed.api.NetworkService;
import com.neda.newsfeed.api.RetrofitService;
import com.neda.newsfeed.model.Post;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    RetrofitService service = NetworkService.getInstance().getApi();

    private String TAG = "HomeViewModel";

    private DisposableSingleObserver<List<Post>> disposableSingleObserver;
    private MutableLiveData<List<Post>> listMutableLiveData;

    public LiveData<List<Post>> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
            loadPosts();
        }
        return listMutableLiveData;
    }

    private void loadPosts() {
        Single<List<Post>> allPosts = service.getAllPosts();
        disposableSingleObserver = allPosts.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Post>>() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        Log.d(TAG, "onSuccess");
                        listMutableLiveData.setValue(posts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposableSingleObserver.dispose();
    }
}
