package com.neda.newsfeed;

import java.util.concurrent.TimeUnit;

public class Configuration {
    //Post lifetime in database
    public static final long postLifetime = TimeUnit.MINUTES.toMillis(5);
    //Retrofit request timeout in seconds
    public static final int requestTimeout = 20;
}
