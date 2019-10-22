package com.tik.android.component.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.tik.android.component.R;
import com.tik.android.component.basemvp.BasicFragment;
import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.bussiness.service.IAppService;
import com.tik.android.component.bussiness.service.account.AccountConstants;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.libcommon.ActivityStackManager;
import com.tik.android.component.market.ui.fragment.MarketFragment;
import com.tik.android.component.mine.MineFragment;
import com.tik.android.component.property.PropertyFragment;
import com.tik.android.component.trade.module.trade.ui.TradeFragment;

import org.qiyi.video.svg.Andromeda;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/15.
 */
public class MainFragment extends BasicFragment implements IAppService {
    private SupportFragment[] mFragments = new SupportFragment[4];
    private List<MenuItem> mSubMenus = new LinkedList<>();
    private static List<Integer> needLoginTabs = new ArrayList();
    private int mCurrentPos;
    private BottomNavigationViewEx bnv;

    static {
        needLoginTabs.add(IAppService.TAB_INDEX_PROPERTY);//暂时根据tab item来判断是否需要登录，后续可以优化到menu配置上
    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayout() {
        return R.layout.app_fragment_main;
    }

    @Override
    public void switchToTab(int tabIndex, Bundle args) {
        if (getActivity() == null) {
            return;
        }
        // 找出栈顶activity
        Activity fromActivity = ActivityStackManager.getInstance().getCurrentActivity();
        BasicFragment from = null;
        // 找出栈顶fragment
        ISupportFragment topFragment = getTopFragment();
        if (topFragment instanceof BasicFragment) {
            from = (BasicFragment) topFragment;
        }

        boolean fromOtherActivity = fromActivity != getActivity();

        if (from != null) {
            if (from.getRootFragment() != this) {
                if (fromOtherActivity) { // 从其他activity跳转回来禁用fragment的动画
                    from.popTo(getClass(), false, null, 0);
                } else {
                    from.popTo(getClass(), false);
                }
            }

            if (tabIndex >= 0 && tabIndex < mFragments.length) {
                SupportFragment target = mFragments[tabIndex];
                bnv.setCurrentItem(tabIndex);
                target.onNewBundle(args);
            }
        }

        if (fromOtherActivity) { // single task的方式启动MainActivity并出栈其上所有activity
            Intent intent = new Intent(fromActivity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            fromActivity.startActivity(intent);
        }
    }

    private boolean isNeedLogin(int tabNum) {
        return needLoginTabs.contains(tabNum);
    }

    private boolean preLoginDecide(int tabNum) {
        if (isNeedLogin(tabNum) && LocalAccountInfoManager.getInstance().getUser() == null) {
            IAccountService accountService = Andromeda.getLocalService(IAccountService.class);
            accountService.startRegisterOrLogin(MainFragment.this, true, AccountConstants.REQ_REGISTER_LOGIN);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void init(View view) {
        bnv = view.findViewById(R.id.navigation);
        bnv.setItemIconTintList(null);
        bnv.enableShiftingMode(false);
        bnv.enableItemShiftingMode(false);
        bnv.enableAnimation(false);
        bnv.setOnNavigationItemSelectedListener(item -> {
            int pos = mSubMenus.indexOf(item);
            if (preLoginDecide(pos)) {
                return false;
            }
            if (mCurrentPos != pos) {
                showHideFragment(mFragments[pos], mFragments[mCurrentPos]);
                mCurrentPos = pos;
            } else {
                reselectSubMenu();
            }
            return true;
        });
        bnv.setOnNavigationItemReselectedListener(item -> reselectSubMenu());
        initChildrenFragments();
        registerService();
    }

    private void registerService() {
        Andromeda.registerLocalService(IAppService.class, this);
    }

    private void initChildrenFragments() {
        Menu menu = bnv.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            mSubMenus.add(menu.getItem(i));
        }
        SupportFragment firstFragment = findFragment(MarketFragment.class);
        if (firstFragment == null) {
            mFragments[0] = MarketFragment.newInstance();
            mFragments[1] = TradeFragment.newInstance();
            mFragments[2] = PropertyFragment.newInstance();
            mFragments[3] = MineFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_content, 0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2],
                    mFragments[3]);
        } else {
            mFragments[0] = firstFragment;
            mFragments[1] = findFragment(TradeFragment.class);
            mFragments[2] = findFragment(PropertyFragment.class);
            mFragments[3] = findFragment(MineFragment.class);
        }
    }

    private void reselectSubMenu() {
        // do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            bnv.setCurrentItem(IAppService.TAB_INDEX_PROPERTY);
        }
    }
}
