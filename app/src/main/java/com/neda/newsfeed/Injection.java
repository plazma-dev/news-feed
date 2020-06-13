package com.neda.newsfeed;

import android.content.Context;

import com.neda.newsfeed.data_source.LocalPostDataSource;
import com.neda.newsfeed.data_source.PostDataSource;
import com.neda.newsfeed.db.AppDatabase;
import com.neda.newsfeed.viewmodel.ViewModelFactory;

/**
 * Enables injection of data source.
 */
public class Injection {

    private static PostDataSource provideLocalPostDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new LocalPostDataSource(database.postDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PostDataSource dataSource = provideLocalPostDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
