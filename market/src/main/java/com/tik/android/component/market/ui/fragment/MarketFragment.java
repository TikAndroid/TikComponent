package com.tik.android.component.market.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.libcommon.HoxItemDecoration;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.libcommon.StringUtils;
import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.contract.IMarketContract;
import com.tik.android.component.market.presenter.MarketPresenter;
import com.tik.android.component.market.ui.adapter.MarketViewBinder;
import com.tik.android.component.market.ui.adapter.NoFavorViewBinder;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.MarketModelUtils;
import com.tik.android.component.market.util.ResourceUtils;
import com.tik.android.component.widget.HoxTabLayout;
import com.tik.android.component.widget.RegularTextView;
import com.tik.android.component.widget.placeholderview.PlaceHolderView;
import com.tik.android.component.widget.placeholderview.core.PlaceHolderManager;
import com.tik.android.component.widget.placeholderview.placeholder.LoadingPlaceHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.drakeet.multitype.Linker;
import me.drakeet.multitype.MultiTypeAdapter;

public class MarketFragment extends BaseMVPFragment<MarketPresenter> implements IMarketContract.View {
    public static final String TAG = "MarketFragment";
    private MultiTypeAdapter mMarketTabAdapter;
    private int mCurrentIndex = 0;
    private PlaceHolderManager mPlaceHolderMgr;
    private String[] mTabTitles = new String[]{
            ResourceUtils.getStrUserFavor(), ResourceUtils.getStrCurrencyUSD(), ResourceUtils.getStrCurrencyHKD()};
    private boolean mPriceSelected;
    private boolean mGainsSelected;
    //排序类型：初始、升序、降序
    private int[] mOrder = {Constant.ORIGIN_ORDER, Constant.UP_ORDER, Constant.DOWN_ORDER};
    private int mOrderType = 0;
    private int mOrderSize = mOrder.length;

    public static MarketFragment newInstance() {
        Bundle bundle = new Bundle();
        MarketFragment fragment = new MarketFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R2.id.market_tab_rv)
    RecyclerView mRecycleView;

    @BindView(R2.id.type_tab)
    HoxTabLayout mHoxTabLayout;

    @BindView(R2.id.market_tab_refresh_layout)
    SmartRefreshLayout mTabRefreshLayout;

    @BindView(R2.id.market_tab_price)
    RegularTextView mPriceText;

    @BindView(R2.id.market_tab_gains)
    RegularTextView mGainsText;

    @BindView(R2.id.market_search)
    ImageView mSearchView;

    @OnClick(R2.id.market_tab_gains)
    public void onGainSort() {
        refreshOrder();
        changeSortDrawable(mGainsText, mOrderType, mPriceText);
        mPresenter.sortStock(Constant.GAINS_ORDER, mCurrentIndex, mOrderType);
        mGainsSelected = !mGainsSelected;
        mPriceSelected = false;
    }

    @OnClick(R2.id.market_tab_price)
    public void onPriceSort() {
        refreshOrder();
        changeSortDrawable(mPriceText, mOrderType, mGainsText);
        mPresenter.sortStock(Constant.PRICE_ORDER, mCurrentIndex, mOrderType);
        mPriceSelected = !mPriceSelected;
        mGainsSelected = false;
    }

    @Override
    public int initLayout() {
        return R.layout.market_fragment_market;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mPlaceHolderMgr = new PlaceHolderView.Config()
                .addPlaceHolder(LoadingPlaceHolder.class)
                .build()
                .bind(mRecycleView);
        MarketUtils.initData();
        mPresenter.initOriginData();
//        showPlaceHolder();
        initTab();
        initRecycleView();
        initRefreshLayout();

        RxView.clicks(mSearchView).throttleFirst(300, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> MarketModelUtils.goToSearchFragment(this));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        refreshMarketData(mCurrentIndex);
    }

    private int refreshOrder() {
        if (mOrderType >= (mOrderSize - 1)) {
            mOrderType = 0;
        } else {
            mOrderType ++;
        }

        return mOrder[mOrderType];
    }

    private void changeSortDrawable(TextView currentView, int order, TextView defaultView) {
        Drawable sortDrawable = null;
        Drawable defaultDrawable = null;

        if (order == Constant.UP_ORDER) {
            sortDrawable = getContext().getResources().getDrawable(R.drawable.sort_up);
        } else if (order == Constant.DOWN_ORDER) {
            sortDrawable = getContext().getResources().getDrawable(R.drawable.sort_down);
        } else if (order == Constant.ORIGIN_ORDER) {
            sortDrawable = getContext().getResources().getDrawable(R.drawable.sort);
        }

        defaultDrawable = getContext().getResources().getDrawable(R.drawable.sort);

        currentView.setCompoundDrawablesWithIntrinsicBounds(null, null, sortDrawable, null);
        defaultView.setCompoundDrawablesWithIntrinsicBounds(null, null, defaultDrawable, null);
    }

    private void initRefreshLayout() {
        mTabRefreshLayout.setRefreshHeader(new MaterialHeader(getContext())
                .setShowBezierWave(false))
                .setEnableOverScrollBounce(false)
                .setEnableLoadMore(false)
                .setEnableOverScrollDrag(false)
                .setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        refreshMarketData(mCurrentIndex);
                    }
                });
    }

    private void refreshMarketData(int tabIndex) {
        switch (tabIndex) {
            case Constant.MARKET_TAB_FAVOR:
                mPresenter.getUserFavors();
                break;
            case Constant.MARKET_TAB_USD:
                if (UserDataManager.getInstance().getUsdSymbols().size() == 0) {
                    mPresenter.getMarketPriceFromDB(Constant.USD_STOCK_CURRENCY);
                } else {
                    mPresenter.getMarketStocksPrice(StringUtils.listToString(
                            UserDataManager.getInstance().getUsdSymbols(), ","),
                                    Constant.USD_STOCK_CURRENCY);
                }

                break;
            case Constant.MARKET_TAB_HKD:
                if (UserDataManager.getInstance().getHkdSymbols().size() == 0) {
                    mPresenter.getMarketPriceFromDB(Constant.HKD_STOCK_CURRENCY);
                } else {
                    mPresenter.getMarketStocksPrice(StringUtils.listToString(
                            UserDataManager.getInstance().getHkdSymbols(), ","),
                                    Constant.HKD_STOCK_CURRENCY);
                }

                break;
            default:
                break;
        }
    }

    private void initTab() {
        mHoxTabLayout.setItems(mTabTitles, 0);
        mHoxTabLayout.setOnItemClickListener(new HoxTabLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView view, int index) {
                mCurrentIndex = index;
//                showPlaceHolder();
            }

            @Override
            public void OnItemClickAnimEnd(TextView view, int index, boolean cancel) {
                refreshMarketData(index);
            }
        });
    }

    private void initRecycleView() {
        mMarketTabAdapter = new MultiTypeAdapter();
        MarketViewBinder priceBinder = new MarketViewBinder();
        priceBinder.setResultItemClick(new MarketViewBinder.OnResultItemClick() {
            @Override
            public void onItemClick(MarketStockEntity entity) {
                MarketModelUtils.goToChartFragment(MarketFragment.this, entity.getSymbol());
            }
        });

        NoFavorViewBinder noFavorBinder = new NoFavorViewBinder();
        noFavorBinder.setOnButtonClickListener(new NoFavorViewBinder.OnButtonClick() {
            @Override
            public void onItemClick() {
                MarketModelUtils.goToSearchFragment(MarketFragment.this);
            }
        });

        mMarketTabAdapter.register(MarketStockEntity.class)
                .to(priceBinder, noFavorBinder)
                .withLinker(new Linker<MarketStockEntity>() {
                    @Override
                    public int index(@NonNull MarketStockEntity marketStockEntity) {
                        switch (marketStockEntity.getType()) {
                            case Constant.LIST_TYPE:
                                return 0;
                            case Constant.NO_FAVOR:
                                return 1;
                        }

                        return 0;
                    }
                });

        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        mRecycleView.addItemDecoration(new HoxItemDecoration(LinearLayout.VERTICAL,
                        getContext().getResources().getDrawable(R.drawable.horizontal_divider)));
        mRecycleView.setAdapter(mMarketTabAdapter);
    }

    @Override
    public void showMarketStocksPrice(List<MarketStockEntity> entityList) {
        //Todo 优化diffUtil
        mMarketTabAdapter.setItems(entityList);
        mMarketTabAdapter.notifyDataSetChanged();
//        hidePlaceHolder();

        if (null != mTabRefreshLayout) {
            mTabRefreshLayout.finishRefresh();
        }
    }

    /*private void showPlaceHolder() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> emitter) throws Exception {
                if (null != mPlaceHolderMgr) {
                    mPlaceHolderMgr.showPlaceHolder(LoadingPlaceHolder.class);
                }
            }
        }, BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(this))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void hidePlaceHolder() {
        if (null != mPlaceHolderMgr) {
            mPlaceHolderMgr.hidePlaceHolder();
        }
    }*/
}
