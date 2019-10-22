package com.tik.android.component.information.contract;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.information.bean.list.ListData;

import java.util.List;

public interface ListContract {
    interface View extends BaseView {
        void showInfoList(Result<List<ListData>> result);
    }

    interface Presenter extends BasePresenter<View> {
        void loadInfoList(int categoryId, int limit, int page);
    }

}
