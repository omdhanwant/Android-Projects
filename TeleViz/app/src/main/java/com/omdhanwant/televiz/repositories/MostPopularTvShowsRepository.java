package com.omdhanwant.televiz.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omdhanwant.televiz.network.ApiClient;
import com.omdhanwant.televiz.network.ApiService;
import com.omdhanwant.televiz.responses.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvShowsRepository{
    private ApiService mApiService;

    public MostPopularTvShowsRepository(){
        mApiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponse> getMostPopularTvShows(int page){
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        mApiService.getMostPopularTvShows(page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                System.out.println(t.getCause());
            }
        });

        return data;
    }
}
