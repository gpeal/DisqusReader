package reader.disqus.com.disqusreader;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;

public class SignalsFragment extends Fragment {
    public static final String KEY_SIGNALS = "signals";

    private CharSequence mExistingTitle;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signals, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.signals);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ArrayList<String> signals = getArguments().getStringArrayList(KEY_SIGNALS);
        if (signals.isEmpty()) {
            signals.add(getString(R.string.no_signals));
        }
        mRecyclerView.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View view = inflater.inflate(R.layout.signal, viewGroup, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, int i) {
                viewHolder.signal.setText(signals.get(i));
            }

            @Override
            public int getItemCount() {
                return signals.size();
            }
        });

        view.findViewById(R.id.touch_interceptor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startAnimation();
        mExistingTitle = getActivity().getTitle();
        getActivity().setTitle(getString(R.string.signals));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setTitle(mExistingTitle);
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {
        public TextView signal;

        public ViewHolder(View itemView) {
            super(itemView);
            signal = (TextView) itemView.findViewById(R.id.signal);
        }
    }

    private void startAnimation() {
        Animator translationY = ObjectAnimator.ofFloat(mRecyclerView, "translationY", 2000, 0)
                .setDuration(300);
        translationY.setStartDelay(150);
        Animator alpha = ObjectAnimator.ofFloat(mRecyclerView, "alpha", mRecyclerView.getAlpha(), 1)
                .setDuration(200);
        alpha.setStartDelay(300);
        ValueAnimator background = ValueAnimator.ofInt(0, 200).setDuration(300);
        background.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                if (getView() != null) {
                    getView().setBackgroundColor(Color.argb(value, 0, 0, 0));
                }
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(background);
        set.play(translationY);
        set.play(alpha);
        set.start();
    }

    private void finish() {
        Animator translationY = ObjectAnimator.ofFloat(mRecyclerView, "translationY", 0, 2000)
                .setDuration(400);
        Animator alpha = ObjectAnimator.ofFloat(mRecyclerView, "alpha", mRecyclerView.getAlpha(), 0)
                .setDuration(200);
        alpha.setStartDelay(200);
        ValueAnimator background = ValueAnimator.ofInt(200, 0).setDuration(400);
        background.setInterpolator(new AccelerateInterpolator());
        background.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                if (getView() != null) {
                    getView().setBackgroundColor(Color.argb(value, 0, 0, 0));
                }
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(background);
        set.play(translationY);
        set.play(alpha);
        set.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
        set.start();
    }
}
