package com.tik.android.component.information.presenter;

import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.information.InfoApi;
import com.tik.android.component.information.contract.ListContract;
import com.tik.android.component.information.bean.list.ListData;

import java.util.List;

public class ListPresenter extends RxPresenter<ListContract.View> implements ListContract.Presenter {
    @Override
    public void loadInfoList(int categoryId, int limit, int page) {
        observe(ApiProxy.getInstance().getApi(InfoApi.class).loadInfoList(categoryId, limit, page))
                .compose(RxUtils.bindToLifecycle(mView))
                .safeSubscribe(new NormalSubscriber<Result<List<ListData>>>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }

                    @Override
                    public void onNext(Result<List<ListData>> listResult) {
                        if (mView != null) {
                            mView.showInfoList(listResult);
                        }
                    }
                });
    }
}
