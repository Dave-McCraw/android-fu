package e036307.mpgs.com.catalogue.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by e036307 on 01/06/2016.
 */
public class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String urldisplay;
    DownloadBitmapListener listener;

    public DownloadBitmapTask(ImageView bmImage, DownloadBitmapListener listener) {
        this.bmImage = bmImage;
        this.listener = listener;
    }

    protected Bitmap doInBackground(String... urls) {
        urldisplay = urls[0];
        System.out.println("Loading image from "+urldisplay);
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        listener.onTaskComplete(urldisplay, result);

        bmImage.setImageBitmap(result);
    }
}
