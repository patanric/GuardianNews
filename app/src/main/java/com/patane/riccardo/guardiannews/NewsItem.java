package com.patane.riccardo.guardiannews;


public class NewsItem {

    private String mThumbnailUrl;
    private String mDate;
    private String mSection;
    private String mTitle;
    private String mUrl;

    public NewsItem(String thumbnail, String date, String section, String title, String url) {
        this.mThumbnailUrl = thumbnail;
        this.mDate = date;
        this.mSection = section;
        this.mTitle = title;
        this.mUrl = url;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }
}
