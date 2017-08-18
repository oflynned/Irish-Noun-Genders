package com.syzible.irishnoungenders.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.objects.Noun;
import com.syzible.irishnoungenders.utils.LocalStorage;
import com.syzible.irishnoungenders.utils.AnimationsHelper;
import com.syzible.irishnoungenders.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.syzible.irishnoungenders.MainActivity.currentNoun;

/**
 * Created by ed on 15/08/2017.
 */

public class MainFrag extends Fragment {
    private JSONArray nouns, domains;
    private JSONObject hints;
    private TextView gaNounTV, gaNounHintTV, gaNounOtherTV, enTranslationTV, highScoreTV;

    private String category;
    private boolean hasAnimatedNewHighScore = false;

    public static int currentScore = 0;
    public static int currentIteration = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.main_frag, container, false);

        gaNounTV = (TextView) view.findViewById(R.id.ga_noun);
        gaNounHintTV = (TextView) view.findViewById(R.id.ga_hint);
        gaNounOtherTV = (TextView) view.findViewById(R.id.ga_other_words);
        enTranslationTV = (TextView) view.findViewById(R.id.en_translation);
        highScoreTV = (TextView) view.findViewById(R.id.high_score);
        highScoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Longest Noun Streak")
                        .setMessage(LocalStorage.getHighScore(getActivity()) + " is the highest streak so far.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        hints = Utils.loadNounHints(getActivity());
        domains = Utils.loadDomainCategories(getActivity());

        Spinner categoryList = (Spinner) view.findViewById(R.id.current_category);
        final ArrayList<String> adapterDomains = new ArrayList<>();
        adapterDomains.add("All Categories");
        for (int i = 0; i < domains.length(); i++) {
            try {
                adapterDomains.add(domains.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(adapterDomains);
        category = "*";

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, adapterDomains);
        categoryList.setAdapter(categoryAdapter);
        categoryList.setSelection(2); // All categories as the first selected on start
        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = adapterDomains.get(position);
                category = Utils.getFileName(category);
                generateNewNoun(category, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MainActivity.answer = new IAnswer() {
            @Override
            public void onCorrectAnswer() {
                Utils.delay(getActivity());
                generateNewNoun(category, false);
                incrementScore();
            }

            @Override
            public void onWrongAnswer() {
                Utils.returnToMainFrag(getActivity());
                resetScore();
                AnimationsHelper.shakeAnimation(highScoreTV);
            }
        };

        generateNewNoun(category, false);
        resetScore();

        return view;
    }

    private void generateNewNoun(String domain, boolean wasCategoryChanged) {
        if (domain.equals("*")) {
            if (currentIteration % 5 == 0) {
                try {
                    int randomIndex = new Random().nextInt(domains.length());
                    String newDomain = domains.getString(randomIndex);
                    nouns = Utils.loadNounList(getActivity(), newDomain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (wasCategoryChanged)
                nouns = Utils.loadNounList(getActivity(), Utils.getFileName(domain));
        }

        if (!wasCategoryChanged)
            currentIteration++;

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
                AnimationsHelper.pulseAnimation(highScoreTV);
            }
        }
    }

    private void resetScore() {
        currentScore = 0;
        highScoreTV.setText(String.valueOf(currentScore));
        hasAnimatedNewHighScore = false;
    }
}
