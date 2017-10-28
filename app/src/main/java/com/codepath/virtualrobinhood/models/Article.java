package com.codepath.virtualrobinhood.models;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Article {
    @SerializedName("_id")
    String id;
    Headline headline;
    String snippet;
    @SerializedName("web_url")
    String webUrl;
    @SerializedName("pub_date")
    String publishDate;
    @SerializedName("multimedia")
    List<ArticleMedia> medias = new ArrayList<>();

    public Article () {}

    public String getId() {
        return id;
    }

    public String getSnippet() {
        return snippet;
    }

    public Headline getHeadline() {
        return headline;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getPublishDate() {return publishDate;}

    @SerializedName("multimedia")
    public List<ArticleMedia> getMultimedia() {
        return medias;
    }
}