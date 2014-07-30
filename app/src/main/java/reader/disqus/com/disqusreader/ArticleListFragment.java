package reader.disqus.com.disqusreader;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.article_list)
public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";

    /** Used to animate the spinner on the first launch only */
    private static boolean IS_FIRST_LAUNCH = true;

    @ViewById(R.id.spinner) LoadingSpinner mSpinner;
    @ViewById(R.id.recycler_view) RecyclerView mRecyclerView;
    @ViewById(R.id.loading_background) View mLoadingBackground;

    @Bean VolleyUtils mVolleyUtils;

    private ArticleAdapter mAdapter;
    private List<Article> mArticles;

    @AfterViews
    void afterViews() {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        getActivity().setTitle(getString(R.string.app_name));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = ArticleAdapter_.getInstance_(getActivity());
        mAdapter.init(this);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        fetchArticles();
    }

    private void fetchArticles() {
        mVolleyUtils.fetchArticles(false, new VolleyUtils.ArticleCallback() {
            @Override
            public void onArticlesLoaded(List<Article> articles) {
                Log.d(TAG, "Got " + articles.size() + " articles.");
                mArticles = articles;
                Log.d(TAG, "setArticles");
                mAdapter.setArticles(mArticles);
                if (IS_FIRST_LAUNCH) {
                    mSpinner.finish(new LoadingSpinner.Callback() {
                        @Override
                        public void onComplete() {
                            mSpinner.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            ViewCompat.animate(mRecyclerView).alpha(1f).setDuration(200).start();
                            mRecyclerView.setTranslationY(200);
                            ViewCompat.animate(mRecyclerView).translationY(0).setDuration(400).start();
                            ViewCompat.animate(mLoadingBackground).alpha(0f).setDuration(400).start();
                        }
                    });
                    IS_FIRST_LAUNCH = false;
                } else {
                    mSpinner.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setAlpha(1f);
                    mRecyclerView.setTranslationY(0);
                    ViewCompat.animate(mLoadingBackground).alpha(0f).setDuration(400).start();
                }
            }
        });
    }

    void onArticleClicked(Article article) {
        Fragment frag = ArticleFragment_.builder().url(article.getUrl()).build();
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content, frag)
                .addToBackStack(null)
                .commit();
    }

    void onArticleSignalsClicked(Article article) {
        ((ReaderActivity) getActivity()).showSignals((ArrayList<String>) article.getSignals());
    }
}
