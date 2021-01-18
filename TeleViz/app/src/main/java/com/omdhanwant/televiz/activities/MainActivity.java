package com.omdhanwant.televiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.omdhanwant.televiz.R;
import com.omdhanwant.televiz.adapters.TvShowsAdapter;
import com.omdhanwant.televiz.databinding.ActivityMainBinding;
import com.omdhanwant.televiz.listeners.TvShowListener;
import com.omdhanwant.televiz.models.TvShow;
import com.omdhanwant.televiz.viewModels.MostPopularTvShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvShowListener {
    private ActivityMainBinding mActivityMainBinding;
    private MostPopularTvShowsViewModel viewModel;
    private List<TvShow> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        setContentView(R.layout.activity_main);
        doInitialisation();
    }

    private void doInitialisation() {
        mActivityMainBinding.tvShowsRecyclerview.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTvShowsViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(tvShows, this);
        mActivityMainBinding.tvShowsRecyclerview.setAdapter(tvShowsAdapter);
        mActivityMainBinding.tvShowsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(mActivityMainBinding.tvShowsRecyclerview.canScrollVertically(1)) {
                    if(currentPage  <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTvShows();
                    }
                }
            }
        });

        mActivityMainBinding.imageWatchList.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), WatchlistActivity.class));
        });
        mActivityMainBinding.imageSearch.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        });
        getMostPopularTvShows();
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetails.class);
//        intent.putExtra("id" , tvShow.getId());
//        intent.putExtra("name" , tvShow.getName());
//        intent.putExtra("startDate" , tvShow.getStartDate());
//        intent.putExtra("country" , tvShow.getCountry());
//        intent.putExtra("network" , tvShow.getNetwork());
//        intent.putExtra("status" , tvShow.getStatus());
        intent.putExtra("tvShow" ,tvShow);
        startActivity(intent);
    }

    private void getMostPopularTvShows() {
        toggleLoading();
        viewModel.getMostPopularTvShows(currentPage).observe(this, mostPopularTvShowResponse -> {
//            Toast.makeText(getApplicationContext(), "Total Pages: " + mostPopularTvShowResponse.getPages(), Toast.LENGTH_SHORT).show();
            toggleLoading();

            if (mostPopularTvShowResponse != null) {
                totalAvailablePages = mostPopularTvShowResponse.getPages();
                if (mostPopularTvShowResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTvShowResponse.getTvShows());
//                    tvShowsAdapter.notifyDataSetChanged();
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if(currentPage == 1) {
            if(mActivityMainBinding.getIsLoading() != null && mActivityMainBinding.getIsLoading()){
                mActivityMainBinding.setIsLoading(false);
            } else {
                mActivityMainBinding.setIsLoading(true);
            }
        } else {
            if(mActivityMainBinding.getIsLoadingMore() != null && mActivityMainBinding.getIsLoadingMore()) {
                mActivityMainBinding.setIsLoadingMore(false);
            } else {
                mActivityMainBinding.setIsLoadingMore(true);
            }
        }
    }
}