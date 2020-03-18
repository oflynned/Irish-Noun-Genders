package com.syzible.irishnoungenders.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.common.FeatureFlag;
import com.syzible.irishnoungenders.common.common.GameMode;
import com.syzible.irishnoungenders.common.firebase.GameServices;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainMenuFragment extends MvpFragment<MainMenuView, MainMenuPresenter>
        implements MainMenuView, TextView.OnClickListener {

    @BindView(R.id.main_menu_gender_mode)
    TextView genderMode;
    @BindView(R.id.main_menu_sign_in)
    TextView signIn;
    @BindView(R.id.main_menu_sign_out)
    TextView signOut;
    @BindView(R.id.main_menu_settings)
    TextView settings;
    @BindView(R.id.main_menu_how_to_play)
    TextView howToPlay;
    @BindView(R.id.main_menu_leaderboards)
    TextView leaderboards;
    @BindView(R.id.main_menu_achievements)
    TextView achievements;

    private GameServices gameServices;
    private Unbinder unbinder;
    private Listener listener;

    public MainMenuFragment() {
    }

    public static MainMenuFragment getInstance() {
        return new MainMenuFragment();
    }

    @NonNull
    @Override
    public MainMenuPresenter createPresenter() {
        return new MainMenuPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasManuallySignedOut = LocalStorage.getBooleanPref(getContext(), LocalStorage.Pref.HAS_SIGNED_OUT);

        if (!hasManuallySignedOut && !gameServices.isSignedIn()) {
            gameServices.signInExplicitly();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        genderMode.setOnClickListener(this);
        signIn.setOnClickListener(this);
        signOut.setOnClickListener(this);
        settings.setOnClickListener(this);

        setupMenuOptions();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(gameServices.isSignedIn());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setupMenuOptions() {
        // TODO enable generally
        if (FeatureFlag.VIEW_ACHIEVEMENTS.isEnabled()) {
            achievements.setOnClickListener(this);
        } else {
            achievements.setVisibility(View.GONE);
        }

        howToPlay.setVisibility(View.GONE);
        leaderboards.setVisibility(View.GONE);
    }

    @Override
    public void showSignIn() {
        signIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSignIn() {
        signIn.setVisibility(View.GONE);
    }

    @Override
    public void showSignOut() {
        signOut.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSignOut() {
        signOut.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_menu_gender_mode:
                listener.onStartGameRequested(GameMode.GENDER);
                break;
            case R.id.main_menu_sign_in:
                LocalStorage.setBooleanPref(getContext(), LocalStorage.Pref.HAS_SIGNED_OUT, false);
                listener.onSignInButtonClicked();
                break;
            case R.id.main_menu_sign_out:
                LocalStorage.setBooleanPref(getContext(), LocalStorage.Pref.HAS_SIGNED_OUT, true);
                listener.onSignOutButtonClicked();
                break;
            case R.id.main_menu_settings:
                listener.onSettingsClicked();
                break;
            case R.id.main_menu_achievements:
                if (FeatureFlag.VIEW_ACHIEVEMENTS.isEnabled()) {
                    listener.onShowAchievementsRequested();
                }
                break;
            case R.id.main_menu_how_to_play:
                if (FeatureFlag.SHOW_TUTORIAL_SANDBOX.isEnabled()) {
                    // TODO stub
                }
            case R.id.main_menu_leaderboards:
                if (FeatureFlag.SHOW_LEADERBOARDS.isEnabled()) {
                    // TODO stub
                }
                break;
        }
    }

    public void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            hideSignIn();
            showSignOut();
            return;
        }

        showSignIn();
        hideSignOut();
    }

    public void setGameServices(GameServices gameServices) {
        this.gameServices = gameServices;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onStartGameRequested(GameMode gameMode);

        void onSettingsClicked();

        void onShowAchievementsRequested();

        void onShowLeaderboardsRequested();

        void onSignInButtonClicked();

        void onSignOutButtonClicked();

        void onSuccessfulSignOut();
    }
}
