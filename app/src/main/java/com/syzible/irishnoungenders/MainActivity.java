package com.syzible.irishnoungenders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.syzible.irishnoungenders.common.common.GameMode;
import com.syzible.irishnoungenders.common.firebase.Event;
import com.syzible.irishnoungenders.common.firebase.FirebaseLogger;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;
import com.syzible.irishnoungenders.databinding.ActivityMainBinding;
import com.syzible.irishnoungenders.screens.MainMenuFragment;
import com.syzible.irishnoungenders.screens.modes.gender.GenderFragment;
import com.syzible.irishnoungenders.screens.options.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements MainMenuFragment.Listener {
    private ActivityMainBinding binding;
    private final MainMenuFragment mainMenuFragment = new MainMenuFragment();

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupInitialSettings();

        mainMenuFragment.setListener(this);
        setFragment(getSupportFragmentManager(), mainMenuFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
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
                .setPositiveButton(R.string.close, (dialogInterface, i) -> MainActivity.this.finishAndRemoveTask())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setupInitialSettings() {
        LocalStorage.setBooleanPref(this, LocalStorage.Pref.SHOW_HINTS, true);
    }

    @Override
    public void onStartGameRequested(GameMode gameMode) {
        if (gameMode == GameMode.GENDER) {
            FirebaseLogger.logEvent(getApplicationContext(), Event.START_GAME_MODE, "start_game", "gender");
            GenderFragment fragment = new GenderFragment();
            setFragmentBackstack(getSupportFragmentManager(), fragment);
        }
    }

    @Override
    public void onSettingsClicked() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
