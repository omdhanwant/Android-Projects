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

public class SearchTvShowRepository {

    private ApiService mApiService;

    public SearchTvShowRepository() {
        mApiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponse> searchTvShow(String query, int page){
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        mApiService.searchTvShow(query, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return  data;
    }
}
