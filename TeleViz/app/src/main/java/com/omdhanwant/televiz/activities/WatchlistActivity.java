package com.omdhanwant.televiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.omdhanwant.televiz.R;
import com.omdhanwant.televiz.adapters.WatchlistAdapter;
import com.omdhanwant.televiz.databinding.ActivityWatchlistBinding;
import com.omdhanwant.televiz.listeners.WatchlistListener;
import com.omdhanwant.televiz.models.TvShow;
import com.omdhanwant.televiz.utilities.TempDataHolder;
import com.omdhanwant.televiz.viewModels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel watchlistViewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TvShow> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_watchlist);
        activityWatchlistBinding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);
        doInitialisation();
    }

    private void doInitialisation() {
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchlistBinding.imageBack.setOnClickListener(view -> {
            onBackPressed();
        });
        watchList = new ArrayList<>();
        loadWatchList();
    }

    private void loadWatchList() {
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchlistViewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchlistBinding.setIsLoading(false);
                    if (watchList.size() > 0) {
                        watchList.clear();
                    }

                    watchList.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchList, this);
                    activityWatchlistBinding.watchlistRecyclerView.setAdapter(watchlistAdapter);
                    activityWatchlistBinding.watchlistRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TempDataHolder.IS_WATCHLIST_UPDATED) {
            loadWatchList();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetails.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTvShowFromWatchlist(TvShow tvShow, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(watchlistViewModel.removeTvShowFromWatchlist(tvShow).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchList.remove(position);
                    watchlistAdapter.notifyItemRemoved(position);
                    watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                    compositeDisposableForDelete.dispose();
                })
        );
    }
}