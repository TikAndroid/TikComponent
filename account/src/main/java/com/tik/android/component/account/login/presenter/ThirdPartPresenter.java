package com.tik.android.component.account.login.presenter;

import android.text.TextUtils;

import com.tik.android.component.account.login.Constants;
import com.tik.android.component.account.login.ThirdPartApi;
import com.tik.android.component.account.login.UserApi;
import com.tik.android.component.account.login.bean.LoginInfo;
import com.tik.android.component.account.login.contract.ThirdPartContract;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.account.bean.ThirdPartUser;
import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.api.RxPresenter;
import com.tik.android.component.libcommon.JsonUtil;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.tik.android.component.account.login.Constants.THIRD_PART_WECHAT;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/16
 */
public class ThirdPartPresenter extends RxPresenter<ThirdPartContract.View> implements ThirdPartContract.Presenter {

    @Override
    public void thirdPartLoginProcess(String type, String thirdToken) {
        if (THIRD_PART_WECHAT.equals(type)) {
            handleWechatProcess(thirdToken);
        }
    }

    private void handleWechatProcess(final String wxToken) {
        observe(ApiProxy.getInstance().getApi(ThirdPartApi.class).getThirdPartToken(THIRD_PART_WECHAT, wxToken))
                .compose(RxUtils.bindToLifecycle(mView))
                .observeOn(Schedulers.io())
                .flatMap(mapResult -> {
                    if (mapResult == null || mapResult.getData() == null) {
                        return Flowable.just(new Result<User>());
                    }
                    LoginInfo loginInfo = null;
                    ThirdPartUser thirdPartUser = null;
                    if (mapResult.getData() instanceof Map) {
                        loginInfo = JsonUtil.map2object(mapResult.getData(), LoginInfo.class);
                        if (loginInfo == null || emptyStr(loginInfo.getUid())) {
                            thirdPartUser = JsonUtil.map2object(mapResult.getData(), ThirdPartUser.class);
                        }
                    }
                    // login info non null after gson covert
                    if (loginInfo != null && notEmptyStr(loginInfo.getUid())) {
                        LocalAccountInfoManager.getInstance().clearLoginUser();
                        LocalAccountInfoManager.getInstance().saveLoginAuth(loginInfo.getUid(), loginInfo.getToken());
                        return ApiProxy.getInstance().getApi(UserApi.class).getUser();
                    } else if (thirdPartUser != null) {
                        final User user = new User();
                        thirdPartUser.setUserType(Constants.THIRD_PART_WECHAT);
                        user.setThirdPartUser(thirdPartUser);
                        LocalAccountInfoManager.getInstance().saveLoginAuth(thirdPartUser.getUuid(), thirdPartUser.getAccessToken());
                        return just(user);
                    }
                    return Flowable.just(new Result<User>());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new NormalSubscriber<Result<User>>() {
                    @Override
                    public void onNext(Result<User> userResult) {
                        User user = null;
                        if(userResult != null && userResult.getData() != null) {
                            user = userResult.getData();
                            LocalAccountInfoManager.getInstance().saveUser(user);
                        }
                        if (mView != null) {
                            mView.onLogin(user);
                        }
                    }
                });
    }

    private <T> Flowable<Result<T>> just(final T type) {
        Result<T> result = new Result<T>();
        result.setData(type);
        return Flowable.just(result);
    }

    private boolean notEmptyStr(String str) {
        return !TextUtils.isEmpty(str);
    }

    private boolean emptyStr(String str) {
        return TextUtils.isEmpty(str);
    }

}
