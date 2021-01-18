package com.omdhanwant.televiz.listeners;

import com.omdhanwant.televiz.models.TvShow;

public interface WatchlistListener {

    void onTvShowClicked(TvShow tvShow);

    void removeTvShowFromWatchlist(TvShow tvShow, int position);
}
