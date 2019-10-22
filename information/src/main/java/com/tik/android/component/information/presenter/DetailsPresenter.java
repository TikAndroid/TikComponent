package com.tik.android.component.information.presenter;

import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.information.InfoApi;
import com.tik.android.component.information.contract.DetailsContract;
import com.tik.android.component.information.bean.details.DetailsData;

public class DetailsPresenter extends RxPresenter<DetailsContract.View> implements DetailsContract.Presenter {
    @Override
    public void loadInfoDetails(int articleId) {
        observe(ApiProxy.getInstance().getApi(InfoApi.class).loadInfoDetails(articleId))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result<DetailsData>>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }

                    @Override
                    public void onNext(Result<DetailsData> detailsDataResult) {
                        if (mView != null) {
                            mView.showInfoDetails(detailsDataResult);
                        }
                    }
                });

    }
}
