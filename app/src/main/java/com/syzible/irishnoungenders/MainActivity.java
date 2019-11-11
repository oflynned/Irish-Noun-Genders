package com.syzible.irishnoungenders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.syzible.irishnoungenders.common.common.GameMode;
import com.syzible.irishnoungenders.common.common.UIHelper;
import com.syzible.irishnoungenders.common.firebase.AchievementListener;
import com.syzible.irishnoungenders.common.firebase.Achievements;
import com.syzible.irishnoungenders.common.firebase.GameServices;
import com.syzible.irishnoungenders.common.languageselection.BaseActivity;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;
import com.syzible.irishnoungenders.screens.MainMenuFragment;
import com.syzible.irishnoungenders.screens.intro.IntroActivity;
import com.syzible.irishnoungenders.screens.modes.gender.GenderFragment;
import com.syzible.irishnoungenders.screens.options.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity
        implements GameServices, AchievementListener, UIHelper, MainMenuFragment.Listener {

    public static final int RC_SIGN_IN = 1;
    public static final int RC_ACHIEVEMENTS = 2;
    public static final int RC_LEADERBOARD = 3;

    private AchievementsClient achievementsClient;
    private GoogleSignInClient googleSignInClient;

    private MainMenuFragment mainMenuFragment;

    private Unbinder unbinder;

    @BindView(R.id.fragment_content)
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        googleSignInClient = getClient();
        mainMenuFragment = MainMenuFragment.getInstance();

        if (!LocalStorage.getBooleanPref(this, LocalStorage.Pref.FIRST_RUN_COMPLETE)) {
            setupInitialSettings();
            startActivity(new Intent(this, IntroActivity.class));
        } else {
            mainMenuFragment.setGameServices(this);
            mainMenuFragment.setListener(this);
            setFragment(getSupportFragmentManager(), mainMenuFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_game)
                .setMessage(R.string.confirm_exit_game)
                .setPositiveButton(R.string.close, (dialogInterface, i) -> MainActivity.this.finish())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setupInitialSettings() {
        LocalStorage.setBooleanPref(this, LocalStorage.Pref.SHOW_HINTS, true);
        LocalStorage.setBooleanPref(this, LocalStorage.Pref.FORCE_IRISH_LANGUAGE, false);
    }

    public static void setFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragmentManager != null) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_content, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    public static void setFragmentBackstack(FragmentManager fragmentManager, Fragment fragment) {
        if (fragmentManager != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_content, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    public static void popFragment(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }

    public GoogleSignInClient getClient() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        return GoogleSignIn.getClient(this, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                onDisconnected();
            }
        }
    }

    @Override
    public void signInSilently() {
        googleSignInClient.silentSignIn().addOnCompleteListener(this,
                task -> {
                    if (task.isSuccessful()) {
                        onConnected(task.getResult());
                    } else {
                        onDisconnected();
                    }
                });
    }

    public void startSignInIntent() {
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    public void signOut() {
        if (!isSignedIn()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.sign_out))
                .setMessage(getString(R.string.sign_out_information))
                .setPositiveButton(getString(R.string.sign_out), (dialogInterface, i) ->
                        googleSignInClient.signOut()
                                .addOnCompleteListener(this, task -> {
                                            onDisconnected();
                                            onSuccessfulSignOut();
                                        }
                                ))
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    public void onConnected(GoogleSignInAccount account) {
        // we don't need any information about the user aside from their anonymous id
        // to help with segregating some past results for helping future predictions
        // for the given user
        LocalStorage.setStringPref(this, LocalStorage.Pref.USER_ID, account.getId());

        Games.getGamesClient(this, account).setViewForPopups(view);
        mainMenuFragment.setShouldShowSignIn(false);
        achievementsClient = Games.getAchievementsClient(this, account);
    }

    public void onDisconnected() {
        mainMenuFragment.setShouldShowSignIn(true);
        achievementsClient = null;
    }

    @Override
    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    @Override
    public void onAchievementUnlocked(Achievements.Achievement achievement) {
        if (isSignedIn()) {
            switch (achievement) {
                case FIRST_STEPS:
                    // TODO sync this on launch as well in case a user has been playing offline
                    achievementsClient.unlock(getString(R.string.achievement_first_steps));
                    break;
                default:
                    showMessage("Whoops, we haven't implemented this achievement just yet!");
                    break;
            }
        } else {
            showAnonymousAchievementToast(achievement);
        }
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    private void syncAchievements() {
        if (!isSignedIn()) {
            return;
        }

        showMessage("Offline cache sync not implemented");
    }

    private void showAnonymousAchievementToast(Achievements.Achievement achievementUnlocked) {
        showMessage("Achievement unlocked: " + achievementUnlocked.toString());
    }

    @Override
    public void onStartGameRequested(GameMode gameMode) {
        switch (gameMode) {
            case GENDER:
                GenderFragment fragment = GenderFragment.getInstance();
                fragment.setAchievementListener(this);
                setFragmentBackstack(getSupportFragmentManager(), fragment);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSettingsClicked() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            achievementsClient.getAchievementsIntent()
                    .addOnSuccessListener(intent -> startActivityForResult(intent, RC_ACHIEVEMENTS))
                    .addOnFailureListener(exception -> showGenericError());

        } else {
            showRequestSignIn();
        }
    }

    @Override
    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            // TODO stub
        } else {
            showRequestSignIn();
        }
    }

    @Override
    public void onSignInButtonClicked() {
        startSignInIntent();
    }

    @Override
    public void onSignOutButtonClicked() {
        signOut();
        clearUserId();
    }

    @Override
    public void onSuccessfulSignOut() {
        showMessage("You have been signed out successfully.");
    }

    @Override
    public void showRequestSignIn() {
        showMessage("Please sign in to access this feature.");
    }

    @Override
    public void showGenericError() {
        showMessage("Something went wrong.");
    }

    private void clearUserId() {
        LocalStorage.setStringPref(this, LocalStorage.Pref.USER_ID, null);
    }
}
