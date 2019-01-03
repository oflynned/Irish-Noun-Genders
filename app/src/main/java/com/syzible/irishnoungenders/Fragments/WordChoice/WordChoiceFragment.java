package com.syzible.irishnoungenders.Fragments.WordChoice;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.syzible.irishnoungenders.Fragments.Answerable;
import com.syzible.irishnoungenders.Common.Pojo.Noun;
import com.syzible.irishnoungenders.Common.Utils.AnimationsHelper;
import com.syzible.irishnoungenders.Common.Utils.LocalStorage;
import com.syzible.irishnoungenders.Common.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.syzible.irishnoungenders.MainActivity.currentNoun;

/**
 * Created by ed on 15/08/2017.
 */

public class WordChoiceFragment extends Fragment implements WordChoiceView {

    private Unbinder unbinder;

    @BindView(R.id.current_category)
    Spinner categoryList;

    @BindView(R.id.ga_noun)
    TextView gaNounTV;

    @BindView(R.id.ga_hint)
    TextView gaNounHintTV;

    @BindView(R.id.ga_other_words)
    TextView gaNounOtherTV;

    @BindView(R.id.en_translation)
    TextView enTranslationTV;

    @BindView(R.id.word_category)
    TextView categoryTV;

    @BindView(R.id.high_score)
    TextView highScoreTV;

    private JSONArray nouns, domains;
    private JSONObject hints;

    private String category, newDomain;
    private boolean hasAnimatedNewHighScore = false;

    public static int currentScore = 0;
    public static int currentIteration = 0;

    public WordChoiceFragment() {

    }

    public static Fragment getInstance() {
        return new WordChoiceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        hints = Utils.loadNounHints(getActivity());
        domains = Utils.loadDomainCategories(getActivity());

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

        MainActivity.answer = new Answerable() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void generateNewNoun(String domain, boolean wasCategoryChanged) {
        if (domain.equals("*")) {
            if (currentIteration % 5 == 0) {
                try {
                    int randomIndex = new Random().nextInt(domains.length());
                    newDomain = domains.getString(randomIndex);
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

        if (category.equals("*") && !newDomain.equals("")) {
            categoryTV.setVisibility(View.VISIBLE);
            categoryTV.setText("(" + newDomain.toLowerCase() + ")");
            categoryTV.setTypeface(null, Typeface.ITALIC);
        } else {
            categoryTV.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public void resetScore() {
        currentScore = 0;
        highScoreTV.setText(String.valueOf(currentScore));
        hasAnimatedNewHighScore = false;
    }

    @Override
    public void showNewGuess(Noun noun) {

    }

    @Override
    public void onHighScore() {

    }

    @Override
    public void onCorrectAnswer() {

    }

    @Override
    public void onWrongAnswer() {

    }
}
