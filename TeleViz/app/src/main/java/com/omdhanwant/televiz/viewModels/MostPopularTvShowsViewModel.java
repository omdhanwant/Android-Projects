package com.omdhanwant.televiz.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.omdhanwant.televiz.repositories.MostPopularTvShowsRepository;
import com.omdhanwant.televiz.responses.TvShowResponse;

public class MostPopularTvShowsViewModel extends ViewModel {

    private MostPopularTvShowsRepository mMostPopularTvShowsRepository;

    public MostPopularTvShowsViewModel(){
        mMostPopularTvShowsRepository = new MostPopularTvShowsRepository();
    }

    public LiveData<TvShowResponse> getMostPopularTvShows(int page) {
        return mMostPopularTvShowsRepository.getMostPopularTvShows(page);
    }
}
