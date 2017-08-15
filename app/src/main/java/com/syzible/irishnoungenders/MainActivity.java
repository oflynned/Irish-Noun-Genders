package com.syzible.irishnoungenders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private JSONArray nouns;
    private JSONObject hints;
    private TextView gaNounTV, gaNounHintTV, gaNounOtherTV, enTranslationTV, highScoreTV;

    private int currentScore = 0;
    private boolean hasAnimatedNewHighScore = false;

    private Noun currentNoun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gaNounTV = (TextView) findViewById(R.id.ga_noun);
        gaNounHintTV = (TextView) findViewById(R.id.ga_hint);
        gaNounOtherTV = (TextView) findViewById(R.id.ga_other_words);
        enTranslationTV = (TextView) findViewById(R.id.en_translation);
        highScoreTV = (TextView) findViewById(R.id.high_score);

        nouns = Utils.loadNounList(this);
        hints = Utils.loadNounHints(this);

        Button markMasculine = (Button) findViewById(R.id.mark_masculine);
        markMasculine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewNoun();
                if(currentNoun.isMasculine()) {
                    incrementScore();
                } else {
                    resetScore();
                }
            }
        });

        Button markFeminine = (Button) findViewById(R.id.mark_feminine);
        markFeminine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewNoun();
                if (currentNoun.isMasculine()) {
                    resetScore();
                } else {
                    incrementScore();
                }
            }
        });

        generateNewNoun();
        resetScore();
    }

    private void generateNewNoun() {
        Noun noun = Utils.getRandomNoun(nouns);
        currentNoun = noun;
        boolean shouldShowHints = Utils.shouldShowNounHint(noun, hints);

        if (!shouldShowHints) {
            gaNounTV.setText(noun.getIrishWord());
            gaNounHintTV.setText("");
            gaNounOtherTV.setText("");
        } else {
            String ending = Utils.getHint(noun, hints);
            String firstWord = noun.getIrishWord().split(" ")[0];
            String strippedNoun = firstWord.substring(0, firstWord.length() - ending.length());

            // now we have <stripped><ending>
            // we need <stripped><ending> <word> <word> ... if more words exist after the initial one

            String otherWords = noun.getIrishWord()
                    .substring(firstWord.length(), noun.getIrishWord().length());

            gaNounTV.setText(strippedNoun);
            gaNounHintTV.setText(ending);
            gaNounOtherTV.setText(otherWords);
        }

        enTranslationTV.setText(noun.getEnglishTranslation());
    }

    private void incrementScore() {
        currentScore++;
        highScoreTV.setText(String.valueOf(currentScore));

        if (currentScore > LocalStorage.getHighScore(this)) {
            // LocalStorage.setHighScore(this, currentScore);
            hasAnimatedNewHighScore = true;
            animateHighScore();
        }
    }

    private void resetScore() {
        currentScore = 0;
        highScoreTV.setText(String.valueOf(currentScore));
        hasAnimatedNewHighScore = false;
    }

    private void animateHighScore() {

    }
}
