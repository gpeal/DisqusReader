package reader.disqus.com.disqusreader;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    @Bean VolleyUtils mVolleyUtils;

    private List<Article> mArticles;
    private ArticleListFragment mFragment;

    public ArticleAdapter(Context context) {
    }

    public void init(ArticleListFragment fragment) {
        mFragment = fragment;
        setArticles(new ArrayList<Article>());
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.article_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Article article = mArticles.get(position);
        viewHolder.titleView.setText(article.getTitle());
        viewHolder.imageView.setDefaultImageResId(R.drawable.article_image_placeholder);
        viewHolder.imageView.setImageUrl(article.getImageUrl(), mVolleyUtils.getImageLoader());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.onArticleClicked(article);
            }
        });
        viewHolder.signalsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.onArticleSignalsClicked(article);
            }
        });
        Uri uri = Uri.parse(article.getUrl());
        viewHolder.sourceView.setText(uri.getHost());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) mArticles.get(position).getUrl().hashCode();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView imageView;
        public TextView titleView;
        public ImageButton signalsView;
        public TextView sourceView;

        public ViewHolder(View view) {
            super(view);
            imageView = (NetworkImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.title);
            signalsView = (ImageButton) view.findViewById(R.id.signals);
            sourceView = (TextView) view.findViewById(R.id.source);
        }
    }
}
