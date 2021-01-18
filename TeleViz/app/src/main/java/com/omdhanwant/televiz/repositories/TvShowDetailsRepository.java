package com.omdhanwant.televiz.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omdhanwant.televiz.models.TvShowDetails;
import com.omdhanwant.televiz.network.ApiClient;
import com.omdhanwant.televiz.network.ApiService;
import com.omdhanwant.televiz.responses.TvShowDetailResponse;
import com.omdhanwant.televiz.responses.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsRepository {
    private ApiService mApiService;

    public TvShowDetailsRepository(){
        mApiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowDetailResponse> getTvShowDetails(String id){
        MutableLiveData<TvShowDetailResponse> data = new MutableLiveData<>();
        mApiService.getTvShowDetails(id).enqueue(new Callback<TvShowDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetailResponse> call, @NonNull Response<TvShowDetailResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowDetailResponse> call, @NonNull Throwable t) {
                System.out.println(t.getCause());
                data.setValue(null);
            }
        });

        return data;
    }
}
