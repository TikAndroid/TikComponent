package com.tik.android.component.basemvp;


import android.content.Intent;

import com.tik.android.component.libcommon.ReflectUtil;

/**
 * @describe : 二级界面包装Activity
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/20.
 */
public class WrapperActivity extends BasicActivity {

    public static final String EXTRA_FRAGMENT = "extra_fragment";
    public static final String EXTRA_FRAGMENT_ARGS = "extra_fragment_args";

    @Override
    protected void init() {
        super.init();
        try {
            Intent intent = getIntent();
            String argTag = intent.getStringExtra(EXTRA_FRAGMENT);
            Class cls = Class.forName(argTag);
            if (BasicFragment.class.isAssignableFrom(cls) && ReflectUtil.hasDefaultConstructor(cls)) {
                BasicFragment to = (BasicFragment) cls.newInstance();
                to.setArguments(intent.getBundleExtra(EXTRA_FRAGMENT_ARGS));
                loadRootFragment(R.id.fl_container, to);
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public int initLayout() {
        return R.layout.basemvp_activity_wrapper;
    }
}
