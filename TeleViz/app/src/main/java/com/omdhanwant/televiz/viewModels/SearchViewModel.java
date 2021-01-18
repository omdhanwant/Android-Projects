package com.omdhanwant.televiz.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.omdhanwant.televiz.repositories.SearchTvShowRepository;
import com.omdhanwant.televiz.responses.TvShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTvShowRepository searchTvShowRepository;

    public SearchViewModel() {
        searchTvShowRepository = new SearchTvShowRepository();
    }

    public LiveData<TvShowResponse> searchTvShow(String query, int page) {
        return searchTvShowRepository.searchTvShow(query,page);
    }
}
