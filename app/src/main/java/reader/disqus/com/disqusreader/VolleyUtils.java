package reader.disqus.com.disqusreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class VolleyUtils {
    private static final String TAG = "VolleyUtils";
    private static final String URL = "http://jetowls.com:6969/api/content";

    public interface ArticleCallback {
        public void onArticlesLoaded(List<Article> articles);
    }

    @RootContext Context mContext;

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private List<Article> mArticles;

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

    public void fetchArticles(final boolean refetch, final ArticleCallback cb) {
        if (mArticles == null || refetch) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    URL, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                mArticles = Article.fromJsonResponse(response);
                                cb.onArticlesLoaded(mArticles);
                            } catch (JSONException e) {
                                Log.e(TAG, "Error loading articles.", e);
                                String text = mContext.getResources()
                                        .getString(R.string.error_loading_articles);
                                Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Refetching articles after error.");
                                fetchArticles(refetch, cb);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error loading articles: " + error.getLocalizedMessage());
                    String text = mContext.getResources().getString(R.string.error_loading_articles);
                    Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Refetching articles after error.");
                    fetchArticles(refetch, cb);
                }
            });
            request.setTag(this);
            getRequestQueue().add(request);
        } else {
            cb.onArticlesLoaded(mArticles);
        }
    }
}
