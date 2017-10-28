package com.codepath.virtualrobinhood.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Multimedia {
    @SerializedName("multimedia")
    private List<ArticleMedia> medias = new ArrayList<>();

    @SerializedName("multimedia")
    public List<ArticleMedia> getMedias() {
        return medias;
    }
}