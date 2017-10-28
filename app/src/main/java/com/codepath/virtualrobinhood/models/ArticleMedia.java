package com.codepath.virtualrobinhood.models;


import org.parceler.Parcel;

@Parcel
public class ArticleMedia {
    private static final String IMAGE_PREFIX = "http://www.nytimes.com/";

    private String type;
    private String url;
    private int height;
    private int width;

    public String getType() {
        return type;
    }

    public String getUrl() {
        return IMAGE_PREFIX + url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}