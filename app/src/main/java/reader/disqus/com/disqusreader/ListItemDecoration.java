package reader.disqus.com.disqusreader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "ListItemDecoration";

    private final Paint mPaint;
    private final int mDividerHeight;

    public ListItemDecoration(Context context) {
        Resources res = context.getResources();
        mDividerHeight = res.getDimensionPixelSize(R.dimen.article_list_divider_height);
        int color = res.getColor(R.color.list_item_divider_color);
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        Resources res = parent.getContext().getResources();
        outRect.bottom = mDividerHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() + params.leftMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int top = bottom - mDividerHeight;
            int right = child.getRight() - params.rightMargin;
            Rect rect = new Rect(left, top, right, bottom);
            c.drawRect(rect, mPaint);
        }
    }
}
