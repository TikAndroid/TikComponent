package com.tik.android.component.information.contract;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.information.bean.details.DetailsData;

public interface DetailsContract {
    interface View extends BaseView {
        void showInfoDetails(Result<DetailsData> result);
    }

    interface Presenter extends BasePresenter<View> {
        void loadInfoDetails(int articleId);
    }
}
