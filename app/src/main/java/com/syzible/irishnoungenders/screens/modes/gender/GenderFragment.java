package com.syzible.irishnoungenders.screens.modes.gender;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.screens.MainMenuFragment;
import com.syzible.irishnoungenders.screens.modes.common.domainchoice.DomainChoiceFragment;
import com.syzible.irishnoungenders.screens.modes.common.ui.CircularTextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GenderFragment extends MvpFragment<GenderView, GenderPresenter> implements GenderView {

    private Unbinder unbinder;

    @BindView(R.id.gender_game_component_noun_card)
    View card;

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

        setupGuessListeners();
        category.setOnClickListener(v -> presenter.showCategoryScreen(getActivity()));

        backButton.setOnClickListener(v -> presenter.returnToMainMenu());

        presenter.checkNewHighScore(getActivity());
        presenter.fetchNouns(getActivity());
        presenter.pickNoun(getContext());
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
        cardTitle.setVisibility(View.VISIBLE);
        cardTitle.setText(title);
    }

    @Override
    public void showHint(String hint) {
        cardHint.setVisibility(View.VISIBLE);
        cardHint.setText(hint);
    }

    @Override
    public void showTranslation(String translation) {
        cardTranslation.setVisibility(View.VISIBLE);
        cardTranslation.setText(translation);
    }

    private void setupGuessListeners() {
        maleButton.setOnClickListener(v -> presenter.makeGuess(getContext(), Noun.Gender.MASCULINE));
        femaleButton.setOnClickListener(v -> presenter.makeGuess(getContext(), Noun.Gender.FEMININE));
        card.setOnClickListener(null);
    }

    private void setupPostGuessListeners() {
        femaleButton.setOnClickListener(v -> {
            presenter.pickNoun(getContext());
            setupGuessListeners();
        });
        maleButton.setOnClickListener(v -> {
            presenter.pickNoun(getContext());
            setupGuessListeners();
        });
        card.setOnClickListener(v -> {
            presenter.pickNoun(getContext());
            setupGuessListeners();
        });
    }

    @Override
    public void showChoiceButtons() {
        maleButton.setVisibility(View.VISIBLE);
        femaleButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyCorrectGuess(Noun noun) {
        cardTitle.setText(R.string.correct);
        cardTranslation.setText(
                getString(R.string.noun_is_answer,
                        noun.getTitle(),
                        noun.getGender().equals(Noun.Gender.MASCULINE) ?
                                getString(R.string.masculine) : getString(R.string.feminine)
                )
        );
        cardHint.setVisibility(View.GONE);

        if (noun.getGender() == Noun.Gender.MASCULINE) {
            femaleButton.setVisibility(View.GONE);
        } else {
            maleButton.setVisibility(View.GONE);
        }

        setupPostGuessListeners();
    }

    @Override
    public void notifyWrongGuess(Noun noun) {
        cardTitle.setText(getString(R.string.oops));
        String hintContents = noun.getTitle() + " is " + noun.getGender().name().toLowerCase(Locale.US);
        cardTranslation.setText(hintContents);
        cardHint.setVisibility(View.GONE);
        if (noun.getGender() == Noun.Gender.MASCULINE) {
            maleButton.setVisibility(View.GONE);
        } else {
            femaleButton.setVisibility(View.GONE);
        }

        setupPostGuessListeners();
    }

    @Override
    public void showHighScore(String highScore) {
        this.highScore.setText(highScore);
    }

    @Override
    public void notifyEndOfDeck(String currentDomain, int deckSize) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.end_of_deck, currentDomain))
                .setMessage(getString(R.string.end_of_deck_description, deckSize))
                .setPositiveButton(getString(R.string.new_deck), (dialogInterface, i) -> {
                    presenter.resetCurrentDeck();
                    presenter.pickNoun(getContext());
                    presenter.showCategoryScreen(getActivity());
                })
                .setNegativeButton(getString(R.string.restart), ((dialogInterface, i) -> {
                    presenter.resetCurrentDeck();
                    presenter.pickNoun(getContext());
                }))
                .setCancelable(false)
                .show();
    }

    @Override
    public void notifyLeavingGame() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.session_in_progress)
                .setMessage(R.string.confirm_leave_when_in_session)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> returnToMainMenu())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void notifyProgressLoss() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.session_in_progress)
                .setMessage(R.string.confirm_leave_when_changing_category)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> presenter.changeCategory(getActivity()))
                .setNegativeButton(R.string.cancel, null)
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
