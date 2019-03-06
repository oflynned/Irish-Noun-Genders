package com.syzible.irishnoungenders.presentation;

import com.syzible.irishnoungenders.IrishNounsApplication;
import com.syzible.irishnoungenders.R;
import com.syzible.irishnoungenders.domain.model.Noun;
import com.syzible.irishnoungenders.presentation.dashboard.DashboardPresenter;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements DashboardPresenter.View {

    @Inject
    DashboardPresenter dashboardPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initialiseDagger();
        initialisePresenter();
        dashboardPresenter.initialise();
    }

    private void initialiseDagger() {
        IrishNounsApplication app = (IrishNounsApplication) getApplication();
        app.getMainComponent().inject(this);
    }

    private void initialisePresenter() {
        dashboardPresenter.setView(this);
    }

    @Override
    public void showNouns(List<Noun> nounList) {
        for (Noun noun : nounList) {
            System.out.println(noun.toString());
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
