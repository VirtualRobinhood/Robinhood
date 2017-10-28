package com.codepath.virtualrobinhood.network;


import com.codepath.virtualrobinhood.models.ArticlesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTimesService {
    @GET("svc/search/v2/articlesearch.json?api-key=c65383152e094f4883e22c5ccf21709a")
    Call<ArticlesResponse> getArticles(@Query("page") int page, @Query("q") String query);

    @GET("svc/search/v2/articlesearch.json?api-key=c65383152e094f4883e22c5ccf21709a&fl=web_url,multimedia,headline,snippet,pub_date")
    Call<ArticlesResponse> getArticles(@Query("q") String query,
                                       @Query("page") int page,
                                       @Query("begin_date") String beginDate,
                                       @Query("sort") String sortOrder,
                                       @Query("fq") String fq);
}