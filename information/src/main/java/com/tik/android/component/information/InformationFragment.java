package com.tik.android.component.information;

import android.view.View;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.information.contract.CategoryContract;
import com.tik.android.component.information.bean.category.CategoryData;
import com.tik.android.component.information.presenter.CategoryPresenter;

public class InformationFragment extends BaseMVPFragment<CategoryPresenter> implements CategoryContract.View {

    public static final String TAG = "InformationFragment";

    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Override
    public int initLayout() {
        return R.layout.fragment_information;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mPresenter.loadInfoCategories();
    }

    @Override
    public void showInfoCategories(Result<CategoryData> result) {
        // todo 根据返回的结果加载 TabLayout
    }
}
