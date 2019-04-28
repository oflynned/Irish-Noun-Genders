package com.syzible.irishnouns.games.gender;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnouns.R;
import com.syzible.irishnouns.common.models.Noun;
import com.syzible.irishnouns.ui.CircularTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GenderFragment extends MvpFragment<GenderView, GenderPresenter> implements GenderView {

    private Unbinder unbinder;

    @BindView(R.id.gender_game_back_button)
    ImageView backButton;

    @BindView(R.id.gender_game_category)
    TextView category;

    @BindView(R.id.gender_game_high_score)
    TextView highScore;

    @BindView(R.id.gender_game_score_count)
    TextView score;

    @BindView(R.id.gender_game_card_title)
    TextView cardTitle;

    @BindView(R.id.gender_game_card_translation)
    TextView cardTranslation;

    @BindView(R.id.gender_game_male_button)
    CircularTextView maleButton;

    @BindView(R.id.gender_game_female_button)
    CircularTextView femaleButton;

    public GenderFragment() {
    }

    public static GenderFragment getInstance() {
        return new GenderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_gender_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        maleButton.setOnClickListener(v -> presenter.makeGuess(Noun.Gender.MASCULINE));
        femaleButton.setOnClickListener(v -> presenter.makeGuess(Noun.Gender.FEMININE));

        presenter.fetchNouns();
        presenter.pickNoun();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public GenderPresenter createPresenter() {
        return new GenderPresenter();
    }

    @Override
    public void showTitle(String title) {
        cardTitle.setText(title);
    }

    @Override
    public void showTranslation(String translation) {
        cardTranslation.setText(translation);
    }

    @Override
    public void showHint() {

    }

    @Override
    public void notifyCorrectGuess() {
    }

    @Override
    public void notifyWrongGuess() {
    }

    @Override
    public void notifyEndOfDeck(String currentDomain, int deckSize) {
        new AlertDialog.Builder(getActivity())
                .setTitle("End of " + currentDomain + " deck")
                .setMessage("The end of the current deck of " + deckSize + " nouns has been reached. " +
                        "Would you like to go again or choose another deck?")
                .setPositiveButton("New Deck", (dialogInterface, i) -> {
                    presenter.resetCurrentDeck();
                    presenter.pickNoun();
                })
                .setNegativeButton("Restart", ((dialogInterface, i) -> {
                    presenter.resetCurrentDeck();
                    presenter.pickNoun();
                }))
                .show();
    }

    @Override
    public void setScore(String score) {
        this.score.setText(score);
    }

    @Override
    public void setChosenCategory(String category) {
        this.category.setText(category);
    }
}
