package com.omdhanwant.televiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.omdhanwant.televiz.R;
import com.omdhanwant.televiz.databinding.ItemContainerTvShowBinding;
import com.omdhanwant.televiz.listeners.TvShowListener;
import com.omdhanwant.televiz.models.TvShow;

import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder>{
    private List<TvShow> mTvShows;
    private LayoutInflater mLayoutInflater;
    private TvShowListener tvShowListener;

    public TvShowsAdapter(List<TvShow> tvShows, TvShowListener tvShowListener){
        this.mTvShows = tvShows;
        this.tvShowListener = tvShowListener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                mLayoutInflater, R.layout.item_container_tv_show, parent, false
        );
        return new TvShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        holder.bindTvShow(mTvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return mTvShows.size();
    }

     class TvShowViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerTvShowBinding mItemContainerTvShowBinding;

        public TvShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            mItemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTvShow(TvShow tvShow) {
            mItemContainerTvShowBinding.setTvShow(tvShow);
            mItemContainerTvShowBinding.executePendingBindings();
            mItemContainerTvShowBinding.getRoot().setOnClickListener(v -> tvShowListener.onTvShowClicked(tvShow));
        }
    }
}
