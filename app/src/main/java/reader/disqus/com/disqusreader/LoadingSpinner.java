package reader.disqus.com.disqusreader;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class LoadingSpinner extends View {
    private static final int[] COLORS = {
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF
    };
    private static final int SPINNER_RADIUS = 175;
    private static final int CIRCLE_RADIUS = 25;
    private static final int PERIOD = 1200;

    private final Paint mPaint;
    private final ValueAnimator mSpinnerAnimator;

    private int mSpinnerRadius = SPINNER_RADIUS;
    private int mCircleRadius = CIRCLE_RADIUS;

    public interface Callback {
        public void onComplete();
    }

    public LoadingSpinner(Context context) {
        this(context, null);
    }

    public LoadingSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mSpinnerAnimator = ValueAnimator.ofFloat(0f, 2f * (float) Math.PI).setDuration(PERIOD);
        mSpinnerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mSpinnerAnimator.setInterpolator(new LinearInterpolator());
        mSpinnerAnimator.setRepeatMode(ValueAnimator.RESTART);
        mSpinnerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mSpinnerAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radians = (Float) mSpinnerAnimator.getAnimatedValue();
        int cx = canvas.getWidth() / 2;
        int cy = canvas.getHeight() / 2;

        for (int i = 0; i < COLORS.length; i++) {
            mPaint.setColor(COLORS[i]);
            float radianOffset = 2f * (float) Math.PI / (float) COLORS.length * (float) i;
            int x = (int) (cx + Math.cos(radians + radianOffset) * mSpinnerRadius);
            int y = (int) (cy + Math.sin(radians + radianOffset) * mSpinnerRadius);
            canvas.drawCircle(x, y, mCircleRadius, mPaint);
        }
        invalidate();
    }

    public void finish(final Callback cb) {
        ValueAnimator finishAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(500);
        finishAnimator.setInterpolator(new AccelerateInterpolator());
        finishAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSpinnerRadius = (int) ((float) SPINNER_RADIUS * (Float) animation.getAnimatedValue());
                mCircleRadius = (int) ((float) CIRCLE_RADIUS * (Float) animation.getAnimatedValue());
            }
        });
        finishAnimator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cb.onComplete();
                mSpinnerAnimator.cancel();
            }
        });
        finishAnimator.start();
    }
}
