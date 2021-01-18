package com.omdhanwant.televiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.omdhanwant.televiz.R;
import com.omdhanwant.televiz.databinding.ItemContainerTvShowBinding;
import com.omdhanwant.televiz.listeners.WatchlistListener;
import com.omdhanwant.televiz.models.TvShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistAdapterHolder>{
    private List<TvShow> mTvShows;
    private LayoutInflater mLayoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TvShow> tvShows, WatchlistListener watchlistListener){
        this.mTvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public WatchlistAdapter.WatchlistAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                mLayoutInflater, R.layout.item_container_tv_show, parent, false
        );
        return new WatchlistAdapter.WatchlistAdapterHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapter.WatchlistAdapterHolder holder, int position) {
        holder.bindTvShow(mTvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return mTvShows.size();
    }

    class WatchlistAdapterHolder extends RecyclerView.ViewHolder{
        private ItemContainerTvShowBinding mItemContainerTvShowBinding;

        public WatchlistAdapterHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            mItemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTvShow(TvShow tvShow) {
            mItemContainerTvShowBinding.setTvShow(tvShow);
            mItemContainerTvShowBinding.executePendingBindings();
            mItemContainerTvShowBinding.getRoot().setOnClickListener(v -> watchlistListener.onTvShowClicked(tvShow));
            mItemContainerTvShowBinding.imageDelete.setOnClickListener(view -> {
                watchlistListener.removeTvShowFromWatchlist(tvShow, getAdapterPosition());
            });
            mItemContainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }
}
