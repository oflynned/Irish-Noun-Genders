package com.syzible.irishnoungenders.Fragments.Feminine;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.syzible.irishnoungenders.MainActivity.currentNoun;

/**
 * Created by ed on 15/08/2017.
 */

public class FeminineFrag extends Fragment {

    private static final int FRAGMENT_INDEX = 2;

    private Unbinder unbinder;

    @BindView(R.id.feminine_text)
    TextView feminineText;

    public FeminineFrag() {

    }

    public static FeminineFrag getInstance() {
        return new FeminineFrag();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fem_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisible()) {
            feminineText.animate().rotation(0).start();
            int currentPage = ((MainActivity) getActivity()).getViewPagerIndex();
            if (currentPage == FRAGMENT_INDEX) {
                if (!currentNoun.isMasculine()) {
                    MainActivity.answer.onCorrectAnswer();
                } else {
                    MainActivity.answer.onWrongAnswer();
                }
            }

            final Handler handler = new Handler();
            final Runnable returnAction = () -> feminineText.animate().rotation(90).start();

            Runnable background = () -> {
                try {
                    Thread.sleep(1000);
                    handler.post(returnAction);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            new Thread(background).start();
        }
    }
}
