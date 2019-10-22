package com.tik.android.component.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * MVP Fragment基类
 */
public abstract class BaseMVPFragment<T extends BasePresenter> extends BasicFragment implements BaseView {

    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPresenter = PresenterUtil.getBasePresenter(this.getClass());
        if (mPresenter != null) mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

    /**
     * 进入页面时需要刷新的tab页fragment重写该方法
     */
    public void refreshFragment() {

    }
}