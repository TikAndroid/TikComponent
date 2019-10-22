package com.tik.android.component.information.presenter;

import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.information.InfoApi;
import com.tik.android.component.information.contract.CategoryContract;
import com.tik.android.component.information.bean.category.CategoryData;

public class CategoryPresenter extends RxPresenter<CategoryContract.View> implements CategoryContract.Presenter {

    @Override
    public void loadInfoCategories() {
        observe(ApiProxy.getInstance().getApi(InfoApi.class).loadInfoCategories())
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result<CategoryData>>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                    }

                    @Override
                    public void onNext(Result<CategoryData> result) {
                        if (mView != null) {
                            mView.showInfoCategories(result);
                        }
                    }
                });
    }
}
