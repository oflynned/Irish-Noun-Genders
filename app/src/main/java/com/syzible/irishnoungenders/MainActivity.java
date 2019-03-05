package com.syzible.irishnoungenders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.syzible.irishnoungenders.Common.Pojo.Noun;
import com.syzible.irishnoungenders.Fragments.Answerable;
import com.syzible.irishnoungenders.Fragments.Feminine.FeminineFrag;
import com.syzible.irishnoungenders.Fragments.Masculine.MasculineFrag;
import com.syzible.irishnoungenders.Fragments.WordChoice.WordChoiceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int PAGER_SIZE = 3;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static Answerable answer;
    public static Noun currentNoun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        PagerAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public int getViewPagerIndex() {
        return viewPager.getCurrentItem();
    }

    public ViewPager getViewPager() {
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
                    return new WordChoiceFragment();
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
