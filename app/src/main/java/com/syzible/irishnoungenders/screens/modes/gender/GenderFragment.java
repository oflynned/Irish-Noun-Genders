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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.models.Noun;
import com.syzible.irishnoungenders.databinding.FragmentGenderMainBinding;
import com.syzible.irishnoungenders.screens.modes.common.domainchoice.DomainChoiceFragment;
import com.syzible.irishnoungenders.screens.modes.common.ui.CircularTextView;

public class GenderFragment extends Fragment implements GenderView {

    private FragmentGenderMainBinding binding;

    private Handler handler;
    private Runnable runnable;

    private GenderPresenter presenter;

    public GenderFragment() {
        super(R.layout.fragment_gender_main);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGenderMainBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new GenderPresenter(this);
        handler = new Handler();

        setupGuessListeners();
        setupGuessAutoTransition();

        binding.genderGameCategory.setOnClickListener(v -> presenter.showCategoryScreen(getActivity()));
        binding.genderGameBackButton.setOnClickListener(v -> presenter.returnToMainMenu());

        presenter.checkNewHighScore(getActivity());
        presenter.fetchNouns(getActivity());
        presenter.pickNoun(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void showTitle(String title) {
        binding.genderGameComponentNounCard.genderGameCardTitle.setVisibility(View.VISIBLE);
        binding.genderGameComponentNounCard.genderGameCardTitle.setText(title);
    }

    @Override
    public void showHint(String hint) {
        binding.genderGameComponentNounCard.genderGameCardHint.setVisibility(View.VISIBLE);
        binding.genderGameComponentNounCard.genderGameCardHint.setText(hint);
    }

    @Override
    public void showTranslation(String translation) {
        binding.genderGameComponentNounCard.genderGameCardTranslation.setVisibility(View.VISIBLE);
        binding.genderGameComponentNounCard.genderGameCardTranslation.setText(translation);
    }

    private void setupGuessListeners() {

        setupTouchEvent(binding.genderGameComponentNounCard.genderGameMaleButton, Noun.Gender.MASCULINE, getString(R.string.masc));
        setupTouchEvent(binding.genderGameComponentNounCard.genderGameFemaleButton, Noun.Gender.FEMININE, getString(R.string.fem));
        binding.answerTarget.setOnClickListener(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupTouchEvent(CircularTextView optionChosen, Noun.Gender guess, String label) {
        optionChosen.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    Point point = getLocation(optionChosen);
                    binding.draggableButton.setText(label);
                    binding.draggableButton.setVisibility(View.VISIBLE);
                    binding.draggableButton.setX(point.x);
                    binding.draggableButton.setY(point.y - ((float) binding.draggableButton.getHeight() / 2));
                    optionChosen.setVisibility(View.GONE);

                    animateViewOut(optionChosen == binding.genderGameComponentNounCard.genderGameMaleButton ? binding.genderGameComponentNounCard.genderGameFemaleButton : binding.genderGameComponentNounCard.genderGameMaleButton);
                    emphasiseView(binding.answerTarget);
                    break;

                case MotionEvent.ACTION_MOVE:
                    binding.draggableButton.setX(motionEvent.getRawX() - binding.draggableButton.getWidth());
                    binding.draggableButton.setY(motionEvent.getRawY() - binding.draggableButton.getHeight());
                    break;

                case MotionEvent.ACTION_UP:
                    if (isWithinTargetArea(binding.draggableButton)) {
                        presenter.makeGuess(getContext(), guess);
                    }

                    binding.draggableButton.setVisibility(View.GONE);
                    optionChosen.setVisibility(View.VISIBLE);
                    if (optionChosen == binding.genderGameComponentNounCard.genderGameMaleButton) {
                        animateViewIn(binding.genderGameComponentNounCard.genderGameFemaleButton);
                    } else {
                        animateViewIn(binding.genderGameComponentNounCard.genderGameMaleButton);
                    }
                    break;

                default:
                    return false;
            }
            return true;
        });
    }

    private void setupGuessAutoTransition() {
        runnable = () -> binding.gameArea.performClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPostGuessListeners() {
        // first prevent the listeners from being draggable
        binding.genderGameComponentNounCard.genderGameFemaleButton.setOnTouchListener(null);
        binding.genderGameComponentNounCard.genderGameMaleButton.setOnTouchListener(null);
        handler.postDelayed(runnable, 2000);

        binding.gameArea.setOnClickListener(v -> {
            // re-enable touch listeners for dragging options
            handler.removeCallbacks(runnable);
            presenter.pickNoun(getContext());
            setupGuessListeners();
            setupGuessAutoTransition();
        });
    }

    @Override
    public void showChoiceButtons() {
        binding.genderGameComponentNounCard.genderGameMaleButton.setVisibility(View.VISIBLE);
        animateViewIn(binding.genderGameComponentNounCard.genderGameMaleButton);

        binding.genderGameComponentNounCard.genderGameFemaleButton.setVisibility(View.VISIBLE);
        animateViewIn(binding.genderGameComponentNounCard.genderGameFemaleButton);
    }

    @Override
    public void notifyCorrectGuess(Noun noun) {
        binding.genderGameComponentNounCard.genderGameCardTitle.setText(R.string.correct);
        binding.genderGameComponentNounCard.genderGameCardTranslation.setText(
                getString(R.string.noun_is_answer,
                        noun.getTitle(),
                        noun.getGender().equals(Noun.Gender.MASCULINE) ?
                                getString(R.string.masculine) : getString(R.string.feminine)
                )
        );
        binding.genderGameComponentNounCard.genderGameCardHint.setVisibility(View.GONE);

        if (noun.getGender() == Noun.Gender.MASCULINE) {
            binding.genderGameComponentNounCard.genderGameFemaleButton.setVisibility(View.GONE);
        } else {
            binding.genderGameComponentNounCard.genderGameMaleButton.setVisibility(View.GONE);
        }

        setupPostGuessListeners();
    }

    @Override
    public void notifyWrongGuess(Noun noun) {
        binding.genderGameComponentNounCard.genderGameCardTitle.setText(getString(R.string.oops));
        binding.genderGameComponentNounCard.genderGameCardTranslation.setText(
                getString(R.string.noun_is_answer,
                        noun.getTitle(),
                        noun.getGender().equals(Noun.Gender.MASCULINE) ?
                                getString(R.string.masculine) : getString(R.string.feminine)
                )
        );
        binding.genderGameComponentNounCard.genderGameCardHint.setVisibility(View.GONE);

        if (noun.getGender() == Noun.Gender.MASCULINE) {
            binding.genderGameComponentNounCard.genderGameMaleButton.setVisibility(View.GONE);
        } else {
            binding.genderGameComponentNounCard.genderGameFemaleButton.setVisibility(View.GONE);
        }

        setupPostGuessListeners();
    }

    @Override
    public void showHighScore(String highScore) {
        this.binding.genderGameHighScore.setText(highScore);
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
        binding.genderGameScoreCount.setText(score);
    }

    @Override
    public void setChosenCategory(String category) {
        binding.genderGameCategory.setText(category);
    }

    @Override
    public void showCategoryScreen(String currentCategory) {
        MainActivity.setFragmentBackstack(getFragmentManager(), new DomainChoiceFragment());
    }

    @Override
    public void returnToMainMenu() {
        MainActivity.popFragment(getFragmentManager());
    }

    private void emphasiseView(View view) {
        YoYo.with(Techniques.RubberBand).duration(750).playOn(view);
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
        Point point = getLocation(binding.answerTarget);

        float targetX1 = guessButton.getX() + (float) (guessButton.getWidth() / 2);
        float targetY1 = guessButton.getY() + (float) (guessButton.getHeight() / 2);

        boolean isWithinXTarget = targetX1 > point.x && targetX1 < (point.x + binding.answerTarget.getWidth());
        boolean isWithinYTarget = targetY1 > point.y && targetY1 < (point.y + binding.answerTarget.getHeight());

        return isWithinXTarget && isWithinYTarget;
    }
}
