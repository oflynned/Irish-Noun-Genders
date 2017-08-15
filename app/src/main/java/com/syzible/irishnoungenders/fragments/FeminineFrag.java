package com.syzible.irishnoungenders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;

import static com.syzible.irishnoungenders.MainActivity.currentNoun;

/**
 * Created by ed on 15/08/2017.
 */

public class FeminineFrag extends Fragment {

    private static final int FRAGMENT_INDEX = 2;
    private static final int MAIN_INDEX = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fem_frag, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisible()) {
            int currentPage = ((MainActivity) getActivity()).getViewPagerIndex();
            if (currentPage == FRAGMENT_INDEX) {
                if(!currentNoun.isMasculine()) {
                    MainActivity.answer.onCorrectAnswer();
                } else {
                    MainActivity.answer.onWrongAnswer();
                }

                ((MainActivity) getActivity()).getViewPager().setCurrentItem(MAIN_INDEX);
            }
        }
    }
}
