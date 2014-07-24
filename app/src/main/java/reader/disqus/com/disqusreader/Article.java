package reader.disqus.com.disqusreader;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Article implements Parcelable {
    private static final String TAG = "Article";

    public static ArrayList<Article> fromJsonResponse(JSONObject json) throws JSONException {
        ArrayList<Article> articles = new ArrayList<Article>();
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

    private final List<String> mSignals;
    private final String mDescription;
    private final String mTitle;
    private final String mImageUrl;
    private final String mUrl;


    public Article(JSONObject json) throws JSONException {
        JSONArray signals = json.getJSONArray("signals");
        mSignals = new ArrayList<String>();
        for (int i = 0; i < signals.length(); i++) {
            mSignals.add(signals.getJSONObject(i).getString("name"));
        }
        mDescription = json.getString("description");
        mTitle = json.getString("title");
        mUrl = json.getString("original_url");
        JSONObject image = null;
        try {
            image = json.getJSONObject("image");
        } catch (JSONException e) {
            Log.w(TAG, mUrl + " has no image.");
        }
        if (image == null) {
            mImageUrl = "";
        } else {
            mImageUrl = image.getString("url");
        }
    }

    private Article(Parcel in) {
        mSignals = new ArrayList<String>();
        in.readStringList(mSignals);
        mTitle = in.readString();
        mUrl = in.readString();
        mImageUrl = in.readString();
        mDescription = in.readString();
    }

    public List<String> getSignals() {
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
                "mSignals=" + mSignals +
                ", mDescription='" + mDescription + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }

    public static final Parcelable.Creator<Article> CREATOR  = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(mSignals);
        dest.writeString(mTitle);
        dest.writeString(mUrl);
        dest.writeString(mImageUrl);
        dest.writeString(mDescription);
    }
}
