package com.syzible.irishnoungenders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.syzible.irishnoungenders.utils.LocalStorage;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.objects.Noun;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.utils.PulseAnimation;
import com.syzible.irishnoungenders.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.syzible.irishnoungenders.MainActivity.currentNoun;

/**
 * Created by ed on 15/08/2017.
 */

public class MainFrag extends Fragment {
    private JSONArray nouns;
    private JSONObject hints;
    private TextView gaNounTV, gaNounHintTV, gaNounOtherTV, enTranslationTV, highScoreTV;

    private boolean hasAnimatedNewHighScore = false;

    public static int currentScore = 0;
    private ProgressBar loadingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.main_frag, container, false);

        gaNounTV = (TextView) view.findViewById(R.id.ga_noun);
        gaNounHintTV = (TextView) view.findViewById(R.id.ga_hint);
        gaNounOtherTV = (TextView) view.findViewById(R.id.ga_other_words);
        enTranslationTV = (TextView) view.findViewById(R.id.en_translation);
        highScoreTV = (TextView) view.findViewById(R.id.high_score);

        nouns = Utils.loadNounList(getActivity());
        hints = Utils.loadNounHints(getActivity());

        MainActivity.answer = new IAnswer() {
            @Override
            public void onCorrectAnswer() {
                Utils.delay(getActivity());
                generateNewNoun();
                incrementScore();
            }

            @Override
            public void onWrongAnswer() {
                Utils.returnToMainFrag(getActivity());
                resetScore();
            }
        };

        generateNewNoun();
        resetScore();

        return view;
    }

    private void generateNewNoun() {
        currentNoun = Utils.getRandomNoun(nouns);
        boolean shouldShowHints = Utils.shouldShowNounHint(currentNoun, hints);

        if (!shouldShowHints) {
            gaNounTV.setText(currentNoun.getIrishWord());
            gaNounHintTV.setText("");
            gaNounOtherTV.setText("");
        } else {
            String ending = Utils.getHint(currentNoun, hints);
            String firstWord = currentNoun.getIrishWord().split(" ")[0];
            String strippedNoun = firstWord.substring(0, firstWord.length() - ending.length());

            // now we have <stripped><ending>
            // we need <stripped><ending> <word> <word> ... if more words exist after the initial one

            String otherWords = currentNoun.getIrishWord()
                    .substring(firstWord.length(), currentNoun.getIrishWord().length());

            gaNounTV.setText(strippedNoun);
            gaNounHintTV.setText(ending);
            gaNounOtherTV.setText(otherWords);
        }

        enTranslationTV.setText(currentNoun.getEnglishTranslation());
    }


    private void incrementScore() {
        currentScore++;
        highScoreTV.setText(String.valueOf(currentScore));

        if (currentScore > LocalStorage.getHighScore(getActivity())) {
            LocalStorage.setHighScore(getActivity(), currentScore);

            if (!hasAnimatedNewHighScore) {
                hasAnimatedNewHighScore = true;
                PulseAnimation.animateView(highScoreTV);
            }
        }
    }

    private void resetScore() {
        currentScore = 0;
        highScoreTV.setText(String.valueOf(currentScore));
        hasAnimatedNewHighScore = false;
    }
}
