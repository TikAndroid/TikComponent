package com.tik.android.component.mine.service;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;

import com.google.gson.JsonObject;
import com.tik.android.component.basemvp.Result;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.ApiProxy;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.service.mine.IMineService;
import com.tik.android.component.bussiness.service.mine.MineConstants;
import com.tik.android.component.libcommon.AppUtil;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.CollectionUtils;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.ToastUtil;
import com.tik.android.component.libcommon.sharedpreferences.RxSharedPrefer;
import com.tik.android.component.mine.R;
import com.tik.android.component.mine.VersionApi;
import com.tik.android.component.mine.bean.VersionInfo;
import com.tik.android.component.mine.utils.AppVersionCheckUtil;
import com.tik.android.component.mine.utils.DialogCreator;
import com.tik.android.component.mine.utils.RxTimer;

import static com.tik.android.component.mine.utils.AppVersionCheckUtil.NO_VERSION_NEW;
import static com.tik.android.component.mine.utils.AppVersionCheckUtil.VERSION_LATEST;

/**
 * @describe :
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/14
 */
public class MineServiceImpl implements IMineService {

    private final Activity mActivity;
    private static final String CURRENT_VERSION = AppUtil.getProjectVersionName(BaseApplication.getAPPContext());
    private static final String DEVICE_TYPE = "android";

    /**
     * usually init on MainActivity of Application to show a dialog
     *
     * @param activity main activity,
     */
    public MineServiceImpl(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void doAppCheck(long delay, final boolean toast, final boolean progress) {
        RxTimer.shotOnce(delay, mActivity, number -> handle(toast, progress));
    }

    private void handle(final boolean toast, final boolean progress) {
        ApiProxy.getInstance().getApi(VersionApi.class).getAppVersion(CURRENT_VERSION, DEVICE_TYPE)
                .compose(RxUtils.rxSchedulerHelperForFlowable())
                .safeSubscribe(new NormalSubscriber<Result<VersionInfo>>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                    }

                    @Override
                    public void onNext(Result<VersionInfo> versionInfoResult) {
                        VersionInfo newVersionInfo = versionInfoResult.getData();
                        int updateState = AppVersionCheckUtil.needUpdate(newVersionInfo);
                        handleVersionUpdated(updateState, newVersionInfo, toast);
                        if (newVersionInfo != null) {
                            RxSharedPrefer
                                    .builder()
                                    .context(BaseApplication.getAPPContext())
                                    .build()
                                    .edit()
                                    .putString(MineConstants.KEY_VERSION_CODE_SP, newVersionInfo.getCurrentVersion())
                                    .apply(); //todo optimise
                        }
                    }
                });
    }

    private void handleVersionUpdated(int updateState, VersionInfo newVersionInfo
            , final boolean toast) {
        Resources res = mActivity.getResources();
        if (updateState < NO_VERSION_NEW || newVersionInfo == null) {
            LogUtil.e("version check failed: updateState=" + updateState);
        } else if (updateState == VERSION_LATEST) {
            if (!toast) {
                return;
            }
            ToastUtil.showToastShort(res.getString(R.string.mine_app_version_latest_tips));
        } else {
            String okText = res.getString(R.string.mine_app_version_dlg_now);
            String cancelText = res.getString(R.string.mine_app_version_dlg_later);
            String dialogTitle = res.getString(R.string.mine_app_version_update, newVersionInfo.getCurrentVersion());
            String message = null;
            JsonObject msgObject = newVersionInfo.getMessage();
            if (msgObject != null && CollectionUtils.isNotBlank(msgObject.entrySet())) {
                // todo show the user locale-string
                if (msgObject.get("cn") != null) {
                    message = msgObject.get("cn").getAsString();
                }
            }
            RxSharedPrefer
                    .builder()
                    .context(BaseApplication.getAPPContext())
                    .build()
                    .edit()
                    .putString(MineConstants.KEY_VERSION_CODE_SP, newVersionInfo.getCurrentVersion())
                    .apply();

            boolean cancelable = !newVersionInfo.isForceUpdate();
            DialogCreator.create(new DialogCreator.Builder(mActivity)
                    .dialogListener(new AppCheckListener(newVersionInfo))
                    .title(dialogTitle)
                    .message(message)
                    .positiveText(okText)
                    .negativeText(cancelText)
                    .cancelable(cancelable)
                    .cancelBtnVisible(cancelable))
                    .show();
        }
    }

    /**
     * dialog to go browser
     * use static to avoid memory leak
     */
    public static class AppCheckListener extends DialogCreator.DialogListener {
        private VersionInfo mNewVersionInfo;

        AppCheckListener(VersionInfo newVersion) {
            this.mNewVersionInfo = newVersion;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if(mNewVersionInfo == null) {
                        return;
                    }
                    LogUtil.d("download apk at:" + mNewVersionInfo.getDownload());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
