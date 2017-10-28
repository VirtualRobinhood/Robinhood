package com.codepath.virtualrobinhood.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NYTimesClient {
    private final String API_BASE_URL = "http://api.nytimes.com/";
    private final NYTimesService nyTimesService;
    private static final NYTimesClient INSTANCE = new NYTimesClient();

    private NYTimesClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nyTimesService = retrofit.create(NYTimesService.class);
    }

    public static NYTimesClient getInstance() {
        return INSTANCE;
    }

    public NYTimesService getNYTimesService() {
        return nyTimesService;
    }
}