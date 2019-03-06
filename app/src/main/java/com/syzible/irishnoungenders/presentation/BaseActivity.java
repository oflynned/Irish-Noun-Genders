package com.syzible.irishnoungenders.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindViews();
        initView();
    }

    private void bindViews() {
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();
}
