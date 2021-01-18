package com.omdhanwant.televiz.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.omdhanwant.televiz.database.TvShowDatabase;
import com.omdhanwant.televiz.models.TvShow;
import com.omdhanwant.televiz.repositories.TvShowDetailsRepository;
import com.omdhanwant.televiz.responses.TvShowDetailResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TvShowDetailsViewModel extends AndroidViewModel {
    private TvShowDetailsRepository tvShowDetailsRepository;
    private TvShowDatabase tvShowDatabase;

    public TvShowDetailsViewModel(@NonNull Application application){
        super(application);
        tvShowDetailsRepository = new TvShowDetailsRepository();
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TvShowDetailResponse> getTvShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTvShowDetails(tvShowId);
    }

    public Completable addToWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TvShow> getTvShowFromWishList(String tvShowId) {
        return tvShowDatabase.tvShowDao().getTvShowFromWatchList(tvShowId);
    }

    public Completable removeTvShowFromWatchlist(TvShow tvShow) {
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
