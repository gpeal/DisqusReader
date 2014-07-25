package reader.disqus.com.disqusreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private final VolleyUtils mVolleyUtils;

    private List<Article> mArticles;

    public ArticleAdapter(Context context) {
        mVolleyUtils = VolleyUtils.getInstance(context);
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
        viewHolder.descriptionView.setText(article.getDescription());
        viewHolder.imageView.setDefaultImageResId(R.drawable.article_image_placeholder);
        viewHolder.imageView.setImageUrl(article.getImageUrl(), mVolleyUtils.getImageLoader());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(article);
            }
        });
        viewHolder.signalsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignalsClick(article);
            }
        });
    }

    protected void handleSignalsClick(Article article) {
    }
    protected void handleClick(Article article) {
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
        public TextView descriptionView;
        public TextView signalsView;

        public ViewHolder(View view) {
            super(view);
            imageView = (NetworkImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.title);
            descriptionView = (TextView) view.findViewById(R.id.description);
            signalsView = (TextView) view.findViewById(R.id.signals);
        }
    }
}
