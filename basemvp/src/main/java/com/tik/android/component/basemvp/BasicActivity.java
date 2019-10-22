package com.tik.android.component.basemvp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * 无MVP的activity基类
 */
public abstract class BasicActivity extends RxActivity implements BaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = initLayout();
        if (layoutId > 0) {
            setContentView(initLayout());
        }
        ButterKnife.bind(this);

        init();
    }

    public abstract int initLayout();

    protected void init() {
    }

    /**
     * to solve java.lang.IllegalStateException Fragment not attached to a context
     * 如果有屏幕旋转适配等需求， 那这个解决问题的方案就需要斟酌下
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }
}