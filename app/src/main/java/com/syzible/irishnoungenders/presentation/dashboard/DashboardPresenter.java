package com.syzible.irishnoungenders.presentation.dashboard;

import android.support.annotation.NonNull;

import com.syzible.irishnoungenders.domain.model.Noun;
import com.syzible.irishnoungenders.domain.usecase.GetNounsByDomain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class DashboardPresenter extends Presenter<DashboardPresenter.View> {

    private GetNounsByDomain getNounsByDomain;

    @Inject
    public DashboardPresenter(@NonNull GetNounsByDomain getNounsByDomain) {
        this.getNounsByDomain = getNounsByDomain;
    }

    @Override
    public void initialise() {
        super.initialise();
        getView().showLoading();
        getNounsByDomain.execute(new DisposableObserver<List<Noun>>() {
            @Override
            public void onNext(List<Noun> nouns) {
                getView().showNouns(nouns);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                getView().hideLoading();
            }
        });
    }

    public interface View extends Presenter.View {
        void showNouns(List<Noun> nounList);
    }
}
