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
import com.syzible.irishnouns.MainActivity;
import com.syzible.irishnouns.R;
import com.syzible.irishnouns.common.models.Noun;
import com.syzible.irishnouns.games.common.domainchoice.DomainChoiceFragment;
import com.syzible.irishnouns.games.common.ui.CircularTextView;
import com.syzible.irishnouns.mainmenu.MainMenuFragment;

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

    @BindView(R.id.gender_game_card_hint)
    TextView cardHint;

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
        return inflater.inflate(R.layout.fragment_gender_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        maleButton.setOnClickListener(v -> presenter.makeGuess(getActivity(), Noun.Gender.MASCULINE));
        femaleButton.setOnClickListener(v -> presenter.makeGuess(getActivity(), Noun.Gender.FEMININE));
        category.setOnClickListener(v -> presenter.showCategoryScreen(getActivity()));
        backButton.setOnClickListener(v -> presenter.returnToMainMenu());

        presenter.checkNewHighScore(getActivity());
        presenter.fetchNouns(getActivity());
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
    public void showHint(String hint) {
        cardHint.setText(hint);
    }

    @Override
    public void showTranslation(String translation) {
        cardTranslation.setText(translation);
    }

    @Override
    public void notifyCorrectGuess() {
    }

    @Override
    public void notifyWrongGuess() {
    }

    @Override
    public void showHighScore(String highScore) {
        this.highScore.setText(highScore);
    }

    @Override
    public void notifyEndOfDeck(String currentDomain, int deckSize) {
        new AlertDialog.Builder(getActivity())
                .setTitle("End of " + currentDomain)
                .setMessage("The end of the current deck of " + deckSize + " nouns has been reached. " +
                        "Would you like to try again or choose another deck?")
                .setPositiveButton("New Deck", (dialogInterface, i) -> {
                    presenter.resetCurrentDeck();
                    presenter.pickNoun();
                    presenter.showCategoryScreen(getActivity());
                })
                .setNegativeButton("Restart", ((dialogInterface, i) -> {
                    presenter.resetCurrentDeck();
                    presenter.pickNoun();
                }))
                .setCancelable(false)
                .show();
    }

    @Override
    public void notifyLeavingGame() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Session in progress")
                .setMessage("Are you sure you want to leave the current game? Your progress will be lost.")
                .setPositiveButton("OK", (dialogInterface, i) -> returnToMainMenu())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void notifyProgressLoss() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Session in progress")
                .setMessage("Are you sure you want to change category? Your progress will be reset if you return.")
                .setPositiveButton("OK", (dialogInterface, i) -> presenter.changeCategory(getActivity()))
                .setNegativeButton("Cancel", null)
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

    @Override
    public void showCategoryScreen(String currentCategory) {
        MainActivity.setFragmentBackstack(getFragmentManager(), DomainChoiceFragment.getInstance());
    }

    @Override
    public void returnToMainMenu() {
        MainActivity.setFragment(getFragmentManager(), MainMenuFragment.getInstance());
    }
}
