package com.omdhanwant.televiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.omdhanwant.televiz.R;
import com.omdhanwant.televiz.adapters.EpisodesAdapter;
import com.omdhanwant.televiz.adapters.ImageSliderAdapter;
import com.omdhanwant.televiz.databinding.ActivityTvShowDetailsBinding;
import com.omdhanwant.televiz.databinding.LayoutEpisodsBottomSheetBinding;
import com.omdhanwant.televiz.models.TvShow;
import com.omdhanwant.televiz.utilities.TempDataHolder;
import com.omdhanwant.televiz.viewModels.TvShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TvShowDetails extends AppCompatActivity {

    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TvShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodsBottomSheetBinding layoutEpisodsBottomSheetBinding;
    private TvShow tvShow;
    private Boolean isTvShowAvailableInWatchList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details);
//        setContentView(R.layout.activity_tv_show_details);
        activityTvShowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        doInitialisation();
    }

    private void doInitialisation() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        activityTvShowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        tvShow = (TvShow) getIntent().getSerializableExtra("tvShow");
        checkTvShowInWatchList();
        getTvShowDetails();
    }

    private void checkTvShowInWatchList() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTvShowFromWishList(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTvShowAvailableInWatchList = true;
                    activityTvShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_check);
                    compositeDisposable.dispose();
                })
        );
    }

    private void getTvShowDetails() {
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTvShowDetails(tvShowId).observe(
                this, tvShowDetailResponse -> {
                    activityTvShowDetailsBinding.setIsLoading(false);
                    if (tvShowDetailResponse.getTvShowDetails() != null) {
                        if (tvShowDetailResponse.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailResponse.getTvShowDetails().getPictures());
                        }
                        activityTvShowDetailsBinding.setTvShowImageURL(
                                tvShowDetailResponse.getTvShowDetails().getImagePath()
                        );
                        activityTvShowDetailsBinding.imageTvShow.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailResponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.textReadMore.setOnClickListener(view -> {
                            if (activityTvShowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTvShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvShowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvShowDetailsBinding.textReadMore.setText(R.string.read_less);
                            } else {
                                activityTvShowDetailsBinding.textDescription.setMaxLines(4);
                                activityTvShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvShowDetailsBinding.textReadMore.setText(R.string.read_more);
                            }
                        });

                        activityTvShowDetailsBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailResponse.getTvShowDetails().getRating())
                                )
                        );

                        if (tvShowDetailResponse.getTvShowDetails().getGenres() != null) {
                            activityTvShowDetailsBinding.setGenre(tvShowDetailResponse.getTvShowDetails().getGenres()[0]);
                        } else {
                            activityTvShowDetailsBinding.setGenre("N/A");
                        }

                        activityTvShowDetailsBinding.setRuntime(tvShowDetailResponse.getTvShowDetails().getRunTime() + " min");
                        activityTvShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.buttonWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTvShowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);

                        activityTvShowDetailsBinding.buttonEpisodes.setOnClickListener(view -> {
                            if (episodeBottomSheetDialog == null) {
                                episodeBottomSheetDialog = new BottomSheetDialog(TvShowDetails.this);
                                layoutEpisodsBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TvShowDetails.this),
                                        R.layout.layout_episods_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );
                                episodeBottomSheetDialog.setContentView(layoutEpisodsBottomSheetBinding.getRoot());
                                layoutEpisodsBottomSheetBinding.episodesRecyclerView.setAdapter(
                                        new EpisodesAdapter(tvShowDetailResponse.getTvShowDetails().getEpisodes())
                                );

                                layoutEpisodsBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", tvShow.getName())
                                );

                                layoutEpisodsBottomSheetBinding.imageClose.setOnClickListener(v -> {
                                    episodeBottomSheetDialog.dismiss();
                                });
                            }

                            FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );

                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }

                            episodeBottomSheetDialog.show();

                        });

                        activityTvShowDetailsBinding.imageWatchList.setOnClickListener(view -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();

                            if(isTvShowAvailableInWatchList) {
                                compositeDisposable.add(tvShowDetailsViewModel.removeTvShowFromWatchlist(tvShow)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTvShowAvailableInWatchList = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_watchlist);
                                            Toast.makeText(getApplicationContext(), "Removed from WatchList", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            } else {
                                compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvShowDetailsBinding.imageWatchList.setImageResource(R.drawable.ic_check);
                                            Toast.makeText(getApplicationContext(), "Added ToWatchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            }


                        });
                        activityTvShowDetailsBinding.imageWatchList.setVisibility(View.VISIBLE);

                        loadBasicTvShowDetails();
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderImages) {
        activityTvShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvShowDetailsBinding.layoutSliderIndicator.addView(indicators[i]);
        }

        activityTvShowDetailsBinding.layoutSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCounts = activityTvShowDetailsBinding.layoutSliderIndicator.getChildCount();
        for (int i = 0; i < childCounts; i++) {
            ImageView imageView = (ImageView) activityTvShowDetailsBinding.layoutSliderIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTvShowDetails() {
        activityTvShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvShowDetailsBinding.setNetworkCountry(
                tvShow.getNetwork() + "( " +
                        tvShow.getCountry() + " )"
        );

        activityTvShowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvShowDetailsBinding.setStartDate(tvShow.getStartDate());
        activityTvShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
        activityTvShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
    }
}