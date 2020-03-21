package com.syzible.irishnoungenders.screens.modes.gender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.firebase.AchievementListener;
import com.syzible.irishnoungenders.common.firebase.Event;
import com.syzible.irishnoungenders.common.firebase.FirebaseLogger;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.screens.modes.common.domainchoice.DomainChoiceFragment;
import com.syzible.irishnoungenders.screens.modes.common.ui.CircularTextView;

import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GenderFragment extends MvpFragment<GenderView, GenderPresenter>
        implements GenderView {

    private Unbinder unbinder;
    private Handler handler;
    private Runnable runnable;

    private AchievementListener achievementListener;

    @BindView(R.id.game_area)
    View gameArea;

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

    @BindView(R.id.draggable_button)
    CircularTextView draggableButton;

    @BindView(R.id.answer_target)
    ImageView answerTarget;

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
        this.unbinder = ButterKnife.bind(this, view);
        this.handler = new Handler();

        setupGuessListeners();
        setupGuessAutoTransition();

        category.setOnClickListener(v -> presenter.showCategoryScreen(getActivity()));
        backButton.setOnClickListener(v -> presenter.returnToMainMenu());

        presenter.checkNewHighScore(getActivity());
        try {
            presenter.fetchNouns(getActivity());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
        setupTouchEvent(maleButton, Noun.Gender.MASCULINE, getString(R.string.masc));
        setupTouchEvent(femaleButton, Noun.Gender.FEMININE, getString(R.string.fem));
        gameArea.setOnClickListener(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupTouchEvent(CircularTextView optionChosen, Noun.Gender guess, String label) {
        optionChosen.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    Point point = getLocation(optionChosen);
                    draggableButton.setText(label);
                    draggableButton.setVisibility(View.VISIBLE);
                    draggableButton.setX(point.x);
                    draggableButton.setY(point.y - ((float) draggableButton.getHeight() / 2));
                    optionChosen.setVisibility(View.GONE);

                    animateViewOut(optionChosen == maleButton ? femaleButton : maleButton);
                    emphasiseView(answerTarget);
                    break;

                case MotionEvent.ACTION_MOVE:
                    draggableButton.setX(motionEvent.getRawX() - draggableButton.getWidth());
                    draggableButton.setY(motionEvent.getRawY() - draggableButton.getHeight());
                    break;

                case MotionEvent.ACTION_UP:
                    if (isWithinTargetArea(draggableButton)) {
                        presenter.makeGuess(getContext(), guess);
                    }

                    draggableButton.setVisibility(View.GONE);
                    optionChosen.setVisibility(View.VISIBLE);
                    if (optionChosen == maleButton) {
                        animateViewIn(femaleButton);
                    } else {
                        animateViewIn(maleButton);
                    }
                    break;

                default:
                    return false;
            }
            return true;
        });
    }

    private void setupGuessAutoTransition() {
        runnable = () -> gameArea.performClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPostGuessListeners() {
        // first prevent the listeners from being draggable
        femaleButton.setOnTouchListener(null);
        maleButton.setOnTouchListener(null);
        handler.postDelayed(runnable, 2000);

        gameArea.setOnClickListener(v -> {
            // reenable touch listeners for dragging options
            handler.removeCallbacks(runnable);
            presenter.pickNoun(getContext());
            setupGuessListeners();
            setupGuessAutoTransition();
        });
    }

    @Override
    public void showChoiceButtons() {
        maleButton.setVisibility(View.VISIBLE);
        femaleButton.setVisibility(View.VISIBLE);
        animateViewFirstDraw(maleButton);
        animateViewFirstDraw(femaleButton);
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
        cardTranslation.setText(
                getString(R.string.noun_is_answer,
                        noun.getTitle(),
                        noun.getGender().equals(Noun.Gender.MASCULINE) ?
                                getString(R.string.masculine) : getString(R.string.feminine)
                )
        );
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
                .setMessage(getString(R.string.end_of_deck_description))
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
        FirebaseLogger.logEvent(getContext(), Event.LEAVE_GAME, "score", presenter.getCurrentScore());
        MainActivity.popFragment(getFragmentManager());
    }

    private void emphasiseView(View view) {
        YoYo.with(Techniques.RubberBand).duration(750).playOn(view);
    }

    private void animateViewFirstDraw(View view) {
        YoYo.with(Techniques.FadeIn).duration(600).playOn(view);
    }

    private void animateViewIn(View view) {
        YoYo.with(Techniques.RollIn).duration(300).playOn(view);
    }

    private void animateViewOut(View view) {
        YoYo.with(Techniques.RollOut).duration(300).playOn(view);
    }

    private Point getLocation(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    private boolean isWithinTargetArea(View guessButton) {
        Point point = getLocation(answerTarget);

        float targetX1 = guessButton.getX() + (float) (guessButton.getWidth() / 2);
        float targetY1 = guessButton.getY() + (float) (guessButton.getHeight() / 2);

        boolean isWithinXTarget = targetX1 > point.x && targetX1 < (point.x + answerTarget.getWidth());
        boolean isWithinYTarget = targetY1 > point.y && targetY1 < (point.y + answerTarget.getHeight());
        return isWithinXTarget && isWithinYTarget;
    }

    @Override
    public AchievementListener getAchievementListener() {
        return achievementListener;
    }

    public void setAchievementListener(AchievementListener achievementListener) {
        this.achievementListener = achievementListener;
    }
}
