package com.patane.riccardo.guardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String LOG_TAG = NewsActivity.class.getSimpleName();
    private NewsAdapter newsAdapter;
    private static final int LOADER_ID = 0;
    private ProgressBar progressBar;
    private TextView empty_view;
    private static final String API_KEY = "acf6b60a-8e40-4b1a-803e-74fb04c94761";
    private static final String TG_REQUEST_URL =
        "http://content.guardianapis.com/search?api-key=" + API_KEY + "&show-fields=thumbnail&q=m5s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());

        ListView listView = (ListView) findViewById(R.id.list);
        // set the empty view.
        empty_view = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(empty_view);
        listView.setAdapter(newsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem currentNewsItem = newsAdapter.getItem(position);
                Uri itemUri = Uri.parse(currentNewsItem.getmUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, itemUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // check if network is available
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // start loader
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            Log.e(LOG_TAG, "PROBLEM with loader!");
            progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            empty_view.setText(R.string.no_internet_conn);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, TG_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
        newsAdapter.clear();
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }
        empty_view.setText(R.string.no_news_found);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        newsAdapter.clear();
    }
}
