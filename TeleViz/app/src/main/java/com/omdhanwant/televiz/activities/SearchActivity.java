package com.omdhanwant.televiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.omdhanwant.televiz.R;
import com.omdhanwant.televiz.adapters.TvShowsAdapter;
import com.omdhanwant.televiz.databinding.ActivitySearchBinding;
import com.omdhanwant.televiz.listeners.TvShowListener;
import com.omdhanwant.televiz.models.TvShow;
import com.omdhanwant.televiz.viewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TvShowListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel searchViewModel;
    private List<TvShow> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
        activitySearchBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        doInitialisation();
    }

    private void doInitialisation(){
    activitySearchBinding.imageBack.setOnClickListener(view -> {
        onBackPressed();
    });

    activitySearchBinding.tvShowsRecyclerview.setHasFixedSize(true);
    searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    tvShowsAdapter = new TvShowsAdapter(tvShows, this);
    activitySearchBinding.tvShowsRecyclerview.setAdapter(tvShowsAdapter);

    activitySearchBinding.searchText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(timer != null) {
                timer.cancel();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(!editable.toString().trim().isEmpty()) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            currentPage = 1;
                            totalAvailablePages = 1;
                            getSearchedResult(editable.toString().trim());
                        });
                    }
                }, 800);
            } else {
                tvShows.clear();
                tvShowsAdapter.notifyDataSetChanged();
            }
        }
    });

    activitySearchBinding.tvShowsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(activitySearchBinding.tvShowsRecyclerview.canScrollVertically(1)) {
                if(!activitySearchBinding.searchText.getText().toString().isEmpty()) {
                    if(currentPage  <= totalAvailablePages) {
                    currentPage += 1;
                    getSearchedResult(activitySearchBinding.searchText.getText().toString().trim());
                }}
            }
        }
    });

    activitySearchBinding.searchText.requestFocus();

//    activitySearchBinding.imageSearch.setOnClickListener(view -> {
//        String query = activitySearchBinding.searchText.getText().toString().trim();
//        if(query.equals("") || query.length() == 0 ){
//            return;
//        }
//        getSearchedResult();
//    });

    }

    private void getSearchedResult(String query){
        toggleLoading();
//        String query = activitySearchBinding.searchText.getText().toString().trim();
        searchViewModel.searchTvShow(query,currentPage).observe(this, mostPopularTvShowResponse -> {
            toggleLoading();

            if (mostPopularTvShowResponse != null) {
                totalAvailablePages = mostPopularTvShowResponse.getPages();
                if (mostPopularTvShowResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTvShowResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }


    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetails.class);
        intent.putExtra("tvShow" ,tvShow);
        startActivity(intent);
    }

    private void toggleLoading(){
        if(currentPage == 1) {
            if(activitySearchBinding.getIsLoading() != null && activitySearchBinding.getIsLoading()){
                activitySearchBinding.setIsLoading(false);
            } else {
                activitySearchBinding.setIsLoading(true);
            }
        } else {
            if(activitySearchBinding.getIsLoadingMore() != null && activitySearchBinding.getIsLoadingMore()) {
                activitySearchBinding.setIsLoadingMore(false);
            } else {
                activitySearchBinding.setIsLoadingMore(true);
            }
        }
    }
}