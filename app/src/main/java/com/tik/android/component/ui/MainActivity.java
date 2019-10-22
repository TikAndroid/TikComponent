package com.tik.android.component.ui;

import com.tik.android.component.R;
import com.tik.android.component.account.login.service.AccountServiceImpl;
import com.tik.android.component.account.verify.service.VerifyServiceImpl;
import com.tik.android.component.basemvp.BasicActivity;
import com.tik.android.component.bussiness.service.IAppService;
import com.tik.android.component.bussiness.service.account.IAccountService;
import com.tik.android.component.bussiness.service.account.IVerifyService;
import com.tik.android.component.bussiness.service.market.IMarketService;
import com.tik.android.component.bussiness.service.market.ISearchService;
import com.tik.android.component.bussiness.service.market.utils.IDatabaseService;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.bussiness.service.mine.IMineService;
import com.tik.android.component.bussiness.service.property.IPropertyService;
import com.tik.android.component.bussiness.service.trade.ITradeService;
import com.tik.android.component.bussiness.service.webview.IWebService;
import com.tik.android.component.bussiness.webview.WebServiceImpl;
import com.tik.android.component.market.service.DatabaseServiceImpl;
import com.tik.android.component.market.service.MarketServiceImpl;
import com.tik.android.component.market.service.SearchServiceImpl;
import com.tik.android.component.mine.service.MineServiceImpl;
import com.tik.android.component.property.PropertyServiceImpl;
import com.tik.android.component.trade.module.TradeServiceImpl;

import org.qiyi.video.svg.Andromeda;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends BasicActivity {

    private MainFragment mainFragment;

    @Override
    public int initLayout() {
        return R.layout.app_main_container;
    }

    @Override
    protected void init() {
        super.init();
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, mainFragment = MainFragment.newInstance());
        }
        registerLocalServices();
        doAppVersionCheck();
    }

    private void registerLocalServices() {
        Andromeda.registerLocalService(IAppService.class, mainFragment);
        Andromeda.registerLocalService(IAccountService.class, new AccountServiceImpl());
        Andromeda.registerLocalService(IMarketService.class, new MarketServiceImpl());
        Andromeda.registerLocalService(IMineService.class, new MineServiceImpl(this));
        Andromeda.registerLocalService(IWebService.class, new WebServiceImpl());
        Andromeda.registerLocalService(ISearchService.class, new SearchServiceImpl());
        Andromeda.registerLocalService(ITradeService.class, new TradeServiceImpl());
        Andromeda.registerLocalService(IVerifyService.class, new VerifyServiceImpl());
        Andromeda.registerLocalService(IPropertyService.class, new PropertyServiceImpl());
        Andromeda.registerLocalService(IDatabaseService.class, new DatabaseServiceImpl());
    }

    private void unRegisterLocalServices() {
        // avoid main activity leak
        Andromeda.unregisterLocalService(IAppService.class);
        Andromeda.unregisterLocalService(IMineService.class);
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    private void doAppVersionCheck() {
        IMineService mineService = Andromeda.getLocalService(IMineService.class);
        if (mineService != null) {
            mineService.doAppCheck(1000, false, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MarketUtils.releaseDatabase();
        unRegisterLocalServices();
    }
}
