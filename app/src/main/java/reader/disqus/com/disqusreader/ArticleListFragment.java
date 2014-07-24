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

public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";
    private static final boolean DBG = true;
    private static final String URL = "http://jetowls.com:6969/api/content";

    private View mLoadingContainer;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private VolleyUtils mVolleyUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list, container, false);

        mLoadingContainer = view.findViewById(R.id.loading_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new ListItemDecoration(getActivity()));
        mAdapter = new ArticleAdapter(getActivity()) {
            @Override
            protected void handleClick(Article article) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra(ArticleActivity.KEY_URL, article.getUrl());
                startActivity(intent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mVolleyUtils = VolleyUtils.getInstance(getActivity());
        fetchArticles();
    }

    private void fetchArticles() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mAdapter.setArticles(Article.fromJsonResponse(response));
                            mLoadingContainer.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error loading articles.", e);
                            String text = getResources().getString(R.string.error_loading_articles);
                            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String text = getResources().getString(R.string.error_loading_articles);
                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
            }
        });
        mVolleyUtils.getRequestQueue().add(request);
    }
}
