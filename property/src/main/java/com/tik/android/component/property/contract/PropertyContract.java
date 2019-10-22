package com.tik.android.component.property.contract;

import com.tik.android.component.basemvp.BasePresenter;
import com.tik.android.component.basemvp.BaseView;
import com.tik.android.component.property.bean.PropertyData;

public interface PropertyContract {
    interface View extends BaseView {
        void showTotalData(PropertyData propertyData);

        void showError();
    }

    interface Presenter extends BasePresenter<View> {
        void getTotalData();
    }

}
