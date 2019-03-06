package com.syzible.irishnoungenders.presentation.dashboard;

abstract class Presenter<V extends Presenter.View> {

    private V view;

    public V getView() {
        return view;
    }

    public void setView(V view) {
        this.view = view;
    }

    public void initialise() {
    }

    public interface View {
        void showLoading();

        void hideLoading();
    }
}
