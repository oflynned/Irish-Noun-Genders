package com.syzible.irishnoungenders.screens.intro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.syzible.irishnoungenders.MainActivity;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.common.firebase.Event;
import com.syzible.irishnoungenders.common.firebase.FirebaseLogger;
import com.syzible.irishnoungenders.common.languageselection.BaseActivity;
import com.syzible.irishnoungenders.common.persistence.LocalStorage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(slide1());
        addSlide(slide2());
        addSlide(slide3());
        addSlide(slide4());
        addSlide(slide5());

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startMain();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startMain();
    }

    private void startMain() {
        FirebaseLogger.logEvent(getApplicationContext(), Event.COMPLETE_FIRST_RUN);
        LocalStorage.setBooleanPref(this, LocalStorage.Pref.FIRST_RUN_COMPLETE, true);
        LocalStorage.setBooleanPref(this, LocalStorage.Pref.HAS_SIGNED_OUT, false);
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private AppIntroFragment slide1() {
        return AppIntroFragment.newInstance(
                getString(R.string.intro_slide_1_title, getString(R.string.app_name)),
                R.font.open_sans,
                getString(R.string.intro_slide_1_description),
                R.font.open_sans,
                R.drawable.lightbulb,
                ContextCompat.getColor(this, R.color.lightCarminePink));
    }

    private AppIntroFragment slide2() {
        return AppIntroFragment.newInstance(
                getString(R.string.intro_slide_2_title),
                R.font.open_sans,
                getString(R.string.intro_slide_2_description),
                R.font.open_sans,
                R.drawable.test,
                ContextCompat.getColor(this, R.color.lapisLazuli));
    }

    private AppIntroFragment slide3() {
        return AppIntroFragment.newInstance(
                getString(R.string.intro_slide_3_title),
                R.font.open_sans,
                getString(R.string.intro_slide_3_description),
                R.font.open_sans,
                R.drawable.medal,
                ContextCompat.getColor(this, R.color.mediumTurquoise));
    }

    private AppIntroFragment slide4() {
        return AppIntroFragment.newInstance(
                getString(R.string.intro_slide_4_title),
                R.font.open_sans,
                getString(R.string.intro_slide_4_description),
                R.font.open_sans,
                R.drawable.flask,
                ContextCompat.getColor(this, R.color.lightCarminePink));
    }

    private AppIntroFragment slide5() {
        return AppIntroFragment.newInstance(
                getString(R.string.intro_slide_5_title),
                R.font.open_sans,
                getString(R.string.intro_slide_5_description),
                R.font.open_sans,
                R.drawable.diploma,
                ContextCompat.getColor(this, R.color.jet));
    }
}
