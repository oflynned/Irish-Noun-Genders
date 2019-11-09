package com.syzible.irishnoungenders.screens;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.screens.modes.gender.GenderFragment;
import com.syzible.irishnoungenders.screens.options.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainMenuFragment extends MvpFragment<MainMenuView, MainMenuPresenter>
        implements MainMenuView {

    private static final int RC_SIGN_IN = 1;

    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog;

    private Unbinder unbinder;
    private View view;

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

    public MainMenuFragment() {
    }

    @NonNull
    @Override
    public MainMenuPresenter createPresenter() {
        return new MainMenuPresenter();
    }

    public static MainMenuFragment getInstance() {
        return new MainMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), options);
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
        this.view = view;

        genderMode.setOnClickListener(v -> MainActivity.setFragment(getFragmentManager(), GenderFragment.getInstance()));
        settings.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));
        signIn.setOnClickListener(v -> startSignInIntent());
        signOut.setOnClickListener(v -> signOut());

        progressDialog = new ProgressDialog(getActivity());

        // TODO ultimately remove this or use some feature flag service
        hideUnimplementedOptions();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isSignedIn()) {
            hideSignIn();
            showSignOut();
        } else {
            showSignIn();
            hideSignOut();
        }

        signInSilently();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

                new AlertDialog.Builder(getContext())
                        .setMessage("Unable to sign in!")
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    private void hideUnimplementedOptions() {
        howToPlay.setVisibility(View.GONE);
        leaderboards.setVisibility(View.GONE);
        achievements.setVisibility(View.GONE);
    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(getContext()) != null;
    }

    private void signInSilently() {
        googleSignInClient.silentSignIn().addOnCompleteListener(getActivity(),
                task -> {
                    if (task.isSuccessful()) {
                        onConnected(task.getResult());
                    } else {
                        onDisconnected();
                    }
                });
    }

    private void startSignInIntent() {
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    private void signOut() {
        if (!isSignedIn()) {
            return;
        }

        googleSignInClient.signOut().addOnCompleteListener(getActivity(), task ->
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sign out")
                        .setMessage("Are you sure you want to sign out? You will not be able to track achievements and leaderboards.")
                        .setPositiveButton("Sign out", (dialogInterface, i) -> onDisconnected())
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show());
    }

    private void onConnected(GoogleSignInAccount account) {
        Games.getGamesClient(getActivity(), account).setViewForPopups(view);
        hideSignIn();
        showSignOut();
        hideProgressDialog();
    }

    private void onDisconnected() {
        showSignedOutSuccessfully();
        showSignIn();
        hideSignOut();
        hideProgressDialog();
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
    public void showProgressDialog() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.cancel();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSignedInSuccessfully(String displayName) {
        showMessage("Welcome back " + displayName + "!");
    }

    @Override
    public void showSignedOutSuccessfully() {
        showMessage("You have been signed out successfully.");
    }
}
