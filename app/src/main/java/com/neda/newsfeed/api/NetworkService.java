package com.neda.newsfeed.api;

import com.neda.newsfeed.Configuration;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton holding RetrofitService object
 */
public class NetworkService {
    private static NetworkService instance;
    private RetrofitService api;

    private final String baseUrl = "https://jsonplaceholder.typicode.com";

    //private constructor.
    private NetworkService() {

    }

    public static NetworkService getInstance() {
        if (instance == null) { //if there is no instance available, create new one
            instance = new NetworkService();
        }
        return instance;
    }

    public RetrofitService getApi() {
        if (api == null)
            api = createApi();
        return api;
    }

    /**
     * Create retrofit api
     * @return retrofit service instance
     */
    @NotNull
    private RetrofitService createApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.readTimeout(Configuration.requestTimeout, TimeUnit.SECONDS).connectTimeout(Configuration.requestTimeout, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(RetrofitService.class);
    }
}
