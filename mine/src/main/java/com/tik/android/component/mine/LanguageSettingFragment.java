package com.tik.android.component.mine;

import com.tik.android.component.basemvp.BaseMVPFragment;

public class LanguageSettingFragment extends BaseMVPFragment {

    public static final String TAG = "LanguageSettingFragment";

    public static LanguageSettingFragment newInstance() {
        return new LanguageSettingFragment();
    }

    @Override
    public int initLayout() {
        return R.layout.mine_fragment_launguage;
    }

}
