package reader.disqus.com.disqusreader;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";
    private static final boolean DBG = true;
    private static final String KEY_ARTICLES = "articles";

    private View mLoadingContainer;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private List<Article> mArticles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list, container, false);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        getActivity().setTitle(getString(R.string.app_name));

        mLoadingContainer = view.findViewById(R.id.loading_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ArticleAdapter(getActivity()) {
            @Override
            protected void handleClick(Article article) {
                ArticleFragment frag = new ArticleFragment();
                Bundle args = new Bundle();
                args.putString(ArticleFragment.KEY_URL, article.getUrl());
                frag.setArguments(args);
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.content, frag)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            protected void handleSignalsClick(Article article) {
                ((ReaderActivity) getActivity()).showSignals((ArrayList<String>) article.getSignals());
            }
        };
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        fetchArticles();
        return view;
    }

    private void fetchArticles() {
        VolleyUtils.getInstance(getActivity()).fetchArticles(false, new VolleyUtils.ArticleCallback() {
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
