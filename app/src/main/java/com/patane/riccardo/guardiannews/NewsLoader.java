package com.patane.riccardo.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        List<NewsItem> newsItemList = QueryUtils.fetchEarthquakeData(mUrl);
        return newsItemList;
    }
}
