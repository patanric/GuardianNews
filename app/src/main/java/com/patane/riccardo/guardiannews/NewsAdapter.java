package com.patane.riccardo.guardiannews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewsAdapter extends ArrayAdapter<NewsItem> {

    public static final String LOG_TAG = NewsActivity.class.getName();

    public NewsAdapter(Context context, ArrayList<NewsItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        NewsItem currentNewsItem = getItem(position);

        // thumbnail stuff
        String thumbnailUrl = currentNewsItem.getmThumbnailUrl();
        // thumbnail: https://media.guim.co.uk/3ee6aa5fdded7ec01ba73b4a36dc8432da3f9641/0_29_4000_2401/500.jpg
        ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.thumbnail);
        new ImageDownloaderTask(thumbnailImageView).execute(thumbnailUrl);


        // title stuff
        String title = currentNewsItem.getmTitle();
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(title);


        // date and section stuff
        String date = currentNewsItem.getmDate();
        String section = currentNewsItem.getmSection();
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_section);
        String formattedDate = formatDate(date);
        dateTextView.setText(concatDateSection(formattedDate, section));

        return listItemView;
    }

    private String formatDate(String dateString) {

        Date dateDate = null;

        SimpleDateFormat dateFormatterInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            dateDate = dateFormatterInput.parse(dateString);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
        }

        SimpleDateFormat dateFormatterOutput = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormatterOutput.format(dateDate);
    }

    private String concatDateSection(String date, String section) {
        String result = date + " - " + section;
        return result;
    }




    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return QueryUtils.downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }



    }

}



