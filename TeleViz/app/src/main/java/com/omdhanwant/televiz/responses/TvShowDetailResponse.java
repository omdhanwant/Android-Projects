package com.omdhanwant.televiz.responses;

import com.google.gson.annotations.SerializedName;
import com.omdhanwant.televiz.models.TvShowDetails;

public class TvShowDetailResponse {

    @SerializedName("tvShow")
    private TvShowDetails tvShowDetails;

    public TvShowDetails getTvShowDetails() {
        return tvShowDetails;
    }

    public void setTvShowDetails(TvShowDetails tvShowDetails) {
        this.tvShowDetails = tvShowDetails;
    }
}
