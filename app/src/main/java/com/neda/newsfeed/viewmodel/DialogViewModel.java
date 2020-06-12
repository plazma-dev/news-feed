package com.neda.newsfeed.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neda.newsfeed.api.NetworkService;
import com.neda.newsfeed.api.RetrofitService;
import com.neda.newsfeed.model.User;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DialogViewModel extends ViewModel {
    private static final String TAG = "DialogViewModel";
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<String> errorMessage;
    private String userId;

    RetrofitService service = NetworkService.getInstance().getApi();
    private DisposableSingleObserver<User> disposableSingleObserver;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
            getUser();
        }
        return userMutableLiveData;
    }

    private void getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        Single<User> user = service.getUser(userId);
        disposableSingleObserver = user.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d(TAG, "User fetched");
                        userMutableLiveData.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error fetching user");
                        e.printStackTrace();
                        if (errorMessage == null)
                            errorMessage = new MutableLiveData<>();
                        errorMessage.setValue(e.getLocalizedMessage());
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        if (errorMessage == null)
            errorMessage = new MutableLiveData<>();
        return errorMessage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposableSingleObserver.dispose();
    }
}
