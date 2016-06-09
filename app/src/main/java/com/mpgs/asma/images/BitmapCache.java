package com.mpgs.asma.images;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * This class extends the standard image cache with a callback
 * mechanism, so that it self-populates with results from the {@link DownloadBitmapTask}
 */
public class BitmapCache extends LruCache<String, Bitmap> implements DownloadBitmapTask.DownloadBitmapListener {
    public BitmapCache(int cacheSize) {
        super(cacheSize);
    }


    @Override
    public void onBitmapDownloaded(String s, Bitmap bm) {
        this.put(s, bm);
    }
}
