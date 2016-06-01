package e036307.mpgs.com.catalogue.images;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by e036307 on 01/06/2016.
 */
public class BitmapCache extends LruCache<String, Bitmap> implements DownloadBitmapListener {
    public BitmapCache(int cacheSize){
        super(cacheSize);
    }


    @Override
    public void onTaskComplete(String s, Bitmap bm) {
        this.put(s, bm);
    }
}
