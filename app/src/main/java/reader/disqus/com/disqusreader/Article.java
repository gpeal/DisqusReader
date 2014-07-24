package reader.disqus.com.disqusreader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Article {
    private static final String TAG = "Article";

    public static List<Article> fromJsonResponse(JSONObject json) throws JSONException {
        List<Article> articles = new ArrayList<Article>();
        JSONArray children = json.getJSONArray("results");
        for (int i = 0; i < children.length(); i++) {
            JSONObject child = children.getJSONObject(i);
            try {
                articles.add(new Article(child));
            } catch (JSONException e) {
                Log.e(TAG, "Error processing one of the articles!", e);
            }
        }
        return articles;
    }

    private final String[] mSignals;
    private final String mDescription;
    private final String mTitle;
    private final String mImageUrl;
    private final String mUrl;


    public Article(JSONObject json) throws JSONException {
        JSONArray signals = json.getJSONArray("signals");
        mSignals = new String[signals.length()];
        for (int i = 0; i < signals.length(); i++) {
            mSignals[i] = signals.getJSONObject(i).getString("name");
        }
        mDescription = json.getString("description");
        mTitle = json.getString("title");
        mUrl = json.getString("original_url");
        JSONObject image = json.getJSONObject("image");
        if (image == null) {
            mImageUrl = "";
            Log.w(TAG, mUrl + " has no image.");
        } else {
            mImageUrl = image.getString("url");
        }
        Log.d(TAG, "Created Article: " + this);
    }

    public String[] getSignals() {
        return mSignals;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return "Article{" +
                "mSignals=" + Arrays.toString(mSignals) +
                ", mDescription='" + mDescription + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
