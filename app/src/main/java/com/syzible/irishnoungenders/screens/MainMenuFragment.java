package com.syzible.irishnoungenders.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.common.GameMode;
import com.syzible.irishnoungenders.databinding.FragmentMainMenuBinding;

public class MainMenuFragment extends Fragment implements TextView.OnClickListener {
    private FragmentMainMenuBinding binding;
    private Listener listener;

    public MainMenuFragment() {
        super(R.layout.fragment_main_menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.mainMenuGenderMode.setOnClickListener(this);
        binding.mainMenuSettings.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.main_menu_gender_mode) {
            listener.onStartGameRequested(GameMode.GENDER);
        } else if (view.getId() == R.id.main_menu_settings) {
            listener.onSettingsClicked();
        }
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
    }
}
