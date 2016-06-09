package com.mpgs.asma.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * This task retrieves an arbitrary image URL, converts to Bitmap
 * and applies it to an ImageView. It also calls any interested listeners.
 * <p/>
 * TODO: It would be nice if it called back to update the ImageView instead of taking it as a param.
 * TODO: Maybe extending the ImageViews to make them listeners (but prefer composition to inheritance!)
 */
public class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {
    public interface DownloadBitmapListener {
        void onBitmapDownloaded(String url, Bitmap bm);
    }

    ImageView bmImage;
    String requestedUrl;
    DownloadBitmapListener[] listeners;

    public DownloadBitmapTask(ImageView bmImage, DownloadBitmapListener... listeners) {
        this.bmImage = bmImage;
        this.listeners = listeners;
    }

    protected Bitmap doInBackground(String... urls) {
        requestedUrl = urls[0];

        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(requestedUrl).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        for (DownloadBitmapListener listener : listeners) {
            listener.onBitmapDownloaded(requestedUrl, result);
        }

        bmImage.setImageBitmap(result);
    }
}
