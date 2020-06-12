package com.neda.newsfeed.api;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class NetworkService {
    private static NetworkService instance;
    private RetrofitService api;

    private final String baseUrl = "https://jsonplaceholder.typicode.com";

    //private constructor.
    private NetworkService() {

    }

    public static NetworkService getInstance() {
        if (instance == null) { //if there is no instance available... create new one
            instance = new NetworkService();
        }

        return instance;
    }

    public RetrofitService getApi() {
        if(api == null)
            api = createApi();
        return api;
    }

    @NotNull
    private RetrofitService createApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        //httpClient.readTimeout(0, TimeUnit.SECONDS).connectTimeout(0, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(RetrofitService.class);
    }
}
