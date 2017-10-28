package com.codepath.virtualrobinhood.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Docs {
    @SerializedName("docs")
    private List<Article> articles = new ArrayList<>();

    @SerializedName("docs")
    public List<Article> getArticles() {
        return articles;
    }
}
