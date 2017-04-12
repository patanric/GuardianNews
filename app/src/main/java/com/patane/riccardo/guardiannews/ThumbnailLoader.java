package com.patane.riccardo.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;


public class ThumbnailLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = ThumbnailLoader.class.getSimpleName();
    private String mUrl;

    public ThumbnailLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {

        try {
            InputStream is = (InputStream) new URL(mUrl).getContent();
            Drawable d = Drawable.createFromStream(is, "src_name");
            Log.v(LOG_TAG, "TEST: " + d);
            return d;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in fetching image: " + e);
            return null;
        }
    }
}
