package com.tik.android.component.information.contract;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.information.bean.category.CategoryData;

public interface CategoryContract {
    interface View extends BaseView {
        void showInfoCategories(Result<CategoryData> result);
    }

    interface Presenter extends BasePresenter<View> {
        void loadInfoCategories();
    }

}
