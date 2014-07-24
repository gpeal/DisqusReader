package reader.disqus.com.disqusreader;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";
    private static final boolean DBG = true;
    private static final String KEY_ARTICLES = "articles";

    private View mLoadingContainer;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private VolleyUtils mVolleyUtils;
    private List<Article> mArticles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list, container, false);

        mLoadingContainer = view.findViewById(R.id.loading_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ArticleAdapter(getActivity()) {
            @Override
            protected void handleClick(Article article) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra(ArticleActivity.KEY_URL, article.getUrl());
                startActivity(intent);
            }
        };

        mVolleyUtils = VolleyUtils.getInstance(getActivity());

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ARTICLES)) {
            mArticles = savedInstanceState.getParcelableArrayList(KEY_ARTICLES);
            Log.d(TAG, "Restored " + mArticles.size() + " articles.");
            mAdapter.setArticles(mArticles);
        } else {
            Log.d(TAG, "Fetching new articles.");
            fetchArticles();
        }

        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mVolleyUtils.getRequestQueue().cancelAll(this);
    }

    private void fetchArticles() {
        mVolleyUtils.fetchArticles(false, new VolleyUtils.ArticleCallback() {
            @Override
            public void onArticlesLoaded(List<Article> articles) {
                Log.d(TAG, "Got " + articles.size() + " articles.");
                mArticles = articles;
                mAdapter.setArticles(mArticles);
                mLoadingContainer.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}
