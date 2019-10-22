package com.tik.android.component.basemvp;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;


public abstract class BaseMvpActivity<T extends BasePresenter> extends BasicActivity {
    @Nullable
    protected T mPresenter;

    public BaseMvpActivity() {
        mPresenter = PresenterUtil.getBasePresenter(this.getClass());
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

    @Override
    protected void init() {
        super.init();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

}