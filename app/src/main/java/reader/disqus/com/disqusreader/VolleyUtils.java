package reader.disqus.com.disqusreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyUtils {

    private static VolleyUtils sInstance = null;

    public static VolleyUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleyUtils(context);
        }
        return sInstance;
    }

    private final Context mContext;

    private ImageLoader mImageLoader = null;
    private RequestQueue mRequestQueue = null;

    private VolleyUtils(Context context) {
        mContext = context;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }
        return mImageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

}
