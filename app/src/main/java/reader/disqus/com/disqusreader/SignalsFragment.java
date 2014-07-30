package reader.disqus.com.disqusreader;

import android.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.signals)
public class SignalsFragment extends Fragment {

    @ViewById(R.id.signals) RecyclerView mRecyclerView;
    @ViewById(R.id.scrim) View mScrim;

    @FragmentArg ArrayList<String> signals;

    private CharSequence mExistingTitle;

    @AfterViews
    void afterViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        startAnimation();
        mExistingTitle = getActivity().getTitle();
        getActivity().setTitle(getString(R.string.signals));
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

    @Click(R.id.touch_interceptor)
    void onTouchIntercepted() {
        finish();
    }

    private void startAnimation() {
        mRecyclerView.setTranslationY(2000);
        ViewCompat.animate(mRecyclerView).translationY(0).setDuration(300).setStartDelay(150).start();
        ViewCompat.animate(mRecyclerView).alpha(1).setDuration(200).setStartDelay(300).start();
        ViewCompat.animate(mScrim).alpha(.78f).setDuration(300).start();
    }

    private void finish() {
        ViewCompat.animate(mRecyclerView).translationY(2000).setDuration(400).start();
        ViewCompat.animate(mRecyclerView).alpha(0).setDuration(400).setStartDelay(200).start();
        ViewCompat.animate(mScrim).alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(400)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().getFragmentManager().popBackStackImmediate();
                    }
                })
                .start();
    }
}
