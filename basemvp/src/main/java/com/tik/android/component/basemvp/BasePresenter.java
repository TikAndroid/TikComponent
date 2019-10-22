package com.tik.android.component.basemvp;

public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();
}
