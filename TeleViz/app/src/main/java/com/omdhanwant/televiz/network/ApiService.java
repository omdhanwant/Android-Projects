package com.omdhanwant.televiz.network;

import com.omdhanwant.televiz.models.TvShowDetails;
import com.omdhanwant.televiz.responses.TvShowDetailResponse;
import com.omdhanwant.televiz.responses.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TvShowDetailResponse> getTvShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TvShowResponse> searchTvShow(@Query("q") String query, @Query("page") int page);
}
