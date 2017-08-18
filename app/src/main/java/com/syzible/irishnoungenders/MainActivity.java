package com.syzible.irishnoungenders;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.syzible.irishnoungenders.fragments.FeminineFrag;
import com.syzible.irishnoungenders.fragments.IAnswer;
import com.syzible.irishnoungenders.fragments.MainFrag;
import com.syzible.irishnoungenders.fragments.MasculineFrag;
import com.syzible.irishnoungenders.objects.Noun;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PAGER_SIZE = 3;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    public static IAnswer answer;
    public static Noun currentNoun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public int getViewPagerIndex() {
        if (viewPager == null) {
            viewPager = (ViewPager) findViewById(R.id.view_pager);
        }

        return viewPager.getCurrentItem();
    }

    public ViewPager getViewPager() {
        if (viewPager == null) {
            viewPager = (ViewPager) findViewById(R.id.view_pager);
        }

        return viewPager;
    }

    private class PageAdapter extends FragmentStatePagerAdapter {
        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MasculineFrag();
                case 1:
                    return new MainFrag();
                case 2:
                    return new FeminineFrag();
            }

            return null;
        }

        @Override
        public int getCount() {
            return PAGER_SIZE;
        }
    }
}
