package com.tik.android.component.bussiness.account;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tik.android.component.bussiness.account.bean.User;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.libcommon.CollectionUtils;
import com.tik.android.component.libcommon.JsonUtil;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.UIHandler;
import com.tencent.mmkv.MMKV;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 登录用户的缓存信息管理类
 */
public class LocalAccountInfoManager {

    public static final String TAG = "LocalAccountInfoManager";
    private static final String MMKV_CRYPT_KEY = "dG9rZW4=";
    private static String ACCOUNT_INFO = "account_info";
    private static String TOKEN = "token";
    private static String UID = "uid";

    //登录用户信息
    private AtomicReference<User> user = new AtomicReference<>();
    private AtomicReference<String> token = new AtomicReference<>();
    private AtomicReference<String> uid = new AtomicReference<>();

    private List<WeakReference<AccountStateListener>> mAccountStateListener = new CopyOnWriteArrayList<>();

    @NonNull
    private MMKV kv;

    private LocalAccountInfoManager() {
        kv = MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, MMKV_CRYPT_KEY);
    }

    private static class SingletonHolder {
        private static final LocalAccountInfoManager INSTANCE = new LocalAccountInfoManager();
    }

    public static LocalAccountInfoManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void addAccountStateListener(AccountStateListener listener) {
        if (listener != null && !containListener(listener)) {
            mAccountStateListener.add(new WeakReference<>(listener));
        }
    }

    private synchronized boolean containListener(AccountStateListener listener) {
        CollectionUtils.TraversalReduce<Boolean> result = new CollectionUtils.TraversalReduce<>(false);
        CollectionUtils.traversalWeakRefListAndRemoveEmpty(
                mAccountStateListener,
                result,
                (index, item, reduce) -> {
                    reduce.data |= listener == item;
                    return !reduce.data; // 不存在则继续遍历
                });

        return result.data;
    }

    private void notifyUserInfoChanged() {
        User user = getUser();
        CollectionUtils.traversalWeakRefListAndRemoveEmpty(mAccountStateListener, (index, item) -> {
            LogUtil.i("notifyUserInfoChanged item:" + item
                    + ", index:" + index + ", size:" + mAccountStateListener.size());
            UIHandler.post(() -> item.onUserInfoChanged(user));
            return true; // 继续遍历
        });
    }

    private void notifyLoginStateChanged() {
        LoginState state = getLoginState();
        CollectionUtils.traversalWeakRefListAndRemoveEmpty(mAccountStateListener, (index, item) -> {
            LogUtil.i("notifyLoginStateChanged item:" + item
                    + ", index:" + index + ", size:" + mAccountStateListener.size());
            UIHandler.post(() -> item.onLoginStateChanged(state));
            return true; // 继续遍历
        });
    }

    public LoginState getLoginState() {
        User user = getUser();
        if (user == null) {
            return LoginState.NOT_LOGIN;
        } else if (TextUtils.isEmpty(user.getEmail()) && TextUtils.isEmpty(user.getMobile())) {
            return LoginState.NOT_BIND;
        } else if (user.getAuth() == null || !user.getAuth().getIssetAssetPassword()) {
            return LoginState.NO_ASSET_PWD;
        } else {
            return LoginState.FULL_LOGIN;
        }
    }

    public void saveUser(User user) {
        if (user == null) return;
        this.user.set(user);
        String info = JsonUtil.object2json(user);
        kv.encode(ACCOUNT_INFO, info);
        MarketUtils.resetDatabase();
        notifyUserInfoChanged();
    }

    /**
     * Used by http header
     *
     * @param uid   A hox user has UID
     * @param token the token of user
     */
    public void saveLoginAuth(final String uid, final String token) {
        this.uid.set(uid);
        this.token.set(token);
        kv.encode(UID, uid);
        kv.encode(TOKEN, token);
        notifyLoginStateChanged();
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public User getUser() {
        if (user.get() == null) {
            String info = kv.decodeString(ACCOUNT_INFO, "");
            // do not print user info, be careful
            LogUtil.v("refreshUI user getUser:" + info);
            if (!TextUtils.isEmpty(info)) {
                user.set(JsonUtil.json2object(info, User.class));
            }
        }
        return user.get();
    }

    public boolean isHoxUser() {
        return getUser() != null && getUser().isHoxUser();
    }

    public boolean isThirdUser() {
        return getUser() != null
                && getUser().getThirdPartUser() != null
                && !TextUtils.isEmpty(getUser().getThirdPartUser().getUuid());
    }


    public String getToken() {
        if (token.get() == null) {
            String token = kv.decodeString(TOKEN, "");
            this.token.set(token);
        }
        return token.get();
    }

    public String getUid() {
        if (uid.get() == null) {
            String uid = kv.decodeString(UID, "");
            this.uid.set(uid);
        }
        return uid.get();
    }

    public boolean isLogin() {
        return getUser() != null && !TextUtils.isEmpty(getAuthorization());
    }

    /**
     * 这个在线程中运行的， 直接read没有问题
     *
     * @return
     */
    public String getAuthorization() {
        return getUid() + " " + getToken();
    }


    public void clearLoginUser() {
        boolean reset = false;
        if (user.get() != null) {
            reset = true;
            user.set(null);
        }
        if (!TextUtils.isEmpty(uid.get())) {
            reset = true;
            uid.set(null);
        }
        if (!TextUtils.isEmpty(token.get())) {
            reset = true;
            token.set(null);
        }
        if (reset) {
            kv.removeValueForKey(ACCOUNT_INFO);
            kv.removeValueForKey(TOKEN);
            kv.removeValueForKey(UID);
            LogUtil.i("refreshUI user notifyLoginStateChanged.");
            MarketUtils.resetDatabase();
            notifyLoginStateChanged();
            notifyUserInfoChanged();
        }
    }
}
