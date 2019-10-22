package com.tik.android.component.market.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.basemvp.RxUtils;
import com.tik.android.component.bussiness.api.NormalSubscriber;
import com.tik.android.component.bussiness.service.market.utils.MarketUtils;
import com.tik.android.component.libcommon.Constants;
import com.tik.android.component.libcommon.HoxItemDecoration;
import com.tik.android.component.market.R;
import com.tik.android.component.market.R2;
import com.tik.android.component.market.bussiness.database.MarketStockEntity;
import com.tik.android.component.market.contract.ISearchContract;
import com.tik.android.component.market.presenter.SearchPresenter;
import com.tik.android.component.market.ui.adapter.NoSearchResultViewBinder;
import com.tik.android.component.market.ui.adapter.SearchResultHeadBinder;
import com.tik.android.component.market.ui.adapter.SearchResultViewBinder;
import com.tik.android.component.market.util.Constant;
import com.tik.android.component.market.util.MarketModelUtils;
import com.tik.android.component.widget.EditTextField;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Linker;
import me.drakeet.multitype.MultiTypeAdapter;

public class SearchStockFragment extends BaseMVPFragment<SearchPresenter> implements ISearchContract.View {
    public static final String TAG = "SearchStockFragment";
    private View mView;
    private MultiTypeAdapter mSearchResultAdapter;

    @BindView(R2.id.rv_result)
    RecyclerView mResultRecyclerView;

    @BindView(R2.id.etf_input_search)
    EditTextField mInputEditField;

    @Override
    protected void init(View view) {
        super.init(view);
        mView = view;
        /*PlaceHolderManager placeHolderMgr = new PlaceHolderView.Config()
                .addPlaceHolder(LoadingPlaceHolder.class, SearchEmptyPlaceHolder.class, SearchNoDataPlaceHolder.class)
                .build()
                .bind(mResultRecyclerView);
        mPresenter.setPlaceHolderMgr(placeHolderMgr);*/
        initInputEditField();
        initRecyclerView();
    }

    public static SearchStockFragment newInstance() {
        Bundle args = new Bundle();
        SearchStockFragment fragment = new SearchStockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        refreshRecycleView();
    }

    private void initInputEditField() {
        RxTextView.textChanges(mInputEditField)
                .compose(RxUtils.bindToLifecycle(this))
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.computation(), false, Constant.SEARCH_BUFFER_SIZE)
                .safeSubscribe(new NormalSubscriber<CharSequence>() {
                    @Override
                    public void onNext(CharSequence s) {
                        if (TextUtils.isEmpty(s)) {
                            mPresenter.loadSearchHistory();
                        } else {
                            mPresenter.getSearchResult(s.toString());
                        }
                    }
                });
    }

    private void refreshRecycleView() {
        Flowable.create(new FlowableOnSubscribe<CharSequence>() {
            @Override
            public void subscribe(FlowableEmitter<CharSequence> emitter) throws Exception {
                emitter.onNext((CharSequence)mInputEditField.getText());
            }
        }, BackpressureStrategy.LATEST)
                .compose(RxUtils.bindToLifecycle(this))
                .subscribeOn(Schedulers.computation())
                .safeSubscribe(new NormalSubscriber<CharSequence>() {
                    @Override
                    public void onNext(CharSequence s) {
                        if (TextUtils.isEmpty(s)) {
                            mPresenter.loadSearchHistory();
                        } else {
                            mPresenter.getSearchResult(s.toString());
                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    private void initRecyclerView() {
        mSearchResultAdapter = new MultiTypeAdapter();
        SearchResultHeadBinder resultHeadBinder = new SearchResultHeadBinder();

        resultHeadBinder.setButtonClickListener(new SearchResultHeadBinder.OnButtonClickListener() {
            @Override
            public void onButtonClick() {
                mPresenter.clearSearchHistory();
            }
        });

        SearchResultViewBinder resultViewBinder = new SearchResultViewBinder();
        resultViewBinder.setResultItemClick(new SearchResultViewBinder.OnResultItemClick() {
            @Override
            public void onItemClick(MarketStockEntity entity) {
                Bundle bundle = new Bundle();
                entity.setHistory(true);
                bundle.putParcelable(Constant.HISTORY_INSERT_DATA, entity);
                hideKeyboard();
                //分为跳转到个股详情和直接返回结果给交易页面两种情况
                Bundle arguments = getArguments();
                if (arguments != null && arguments.containsKey(Constants.FRAGMENTATION_ARG_RESULT_RECORD)
                        && arguments.getParcelable(Constants.FRAGMENTATION_ARG_RESULT_RECORD) != null) {
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(Constants.EXTRA_STRING_STOCK_SYMBOL, entity.getSymbol());
                    setFragmentResult(Constants.Market.SEARCH_FRAGMENT_RESULT_CODE, resultBundle);
                    MarketUtils.insertSearchHistory(bundle, SearchStockFragment.this);
                    pop();
                } else {
                    MarketModelUtils.goToChartFragment(SearchStockFragment.this, entity.getSymbol());
                    MarketUtils.insertSearchHistory(bundle, SearchStockFragment.this);
                }
            }

            @Override
            public void onFavorClick(boolean isFavor, MarketStockEntity item) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.USER_FAVOR_DATA, item);
                mPresenter.updateUserFavor(isFavor, bundle, SearchStockFragment.this);
            }
        });

        mSearchResultAdapter.register(MarketStockEntity.class)
                .to(resultViewBinder, resultHeadBinder, new NoSearchResultViewBinder())
                .withLinker(new Linker<MarketStockEntity>() {
                    @Override
                    public int index(@NonNull MarketStockEntity searchEntity) {
                        switch (searchEntity.getType()) {
                            case Constant.LIST_TYPE:
                                return 0;
                            case Constant.HEAD_TYPE:
                                return 1;
                            case Constant.NO_SEARCH_RESULT:
                                return 2;
                        }

                        return 0;
                    }
                });

        mResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        mResultRecyclerView.addItemDecoration(new HoxItemDecoration(LinearLayout.VERTICAL,
                        getContext().getResources().getDrawable(R.drawable.horizontal_divider)));
        mResultRecyclerView.setAdapter(mSearchResultAdapter);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick(R2.id.tv_cancel)
    public void cancel() {
        hideKeyboard();
        _mActivity.onBackPressed();
    }

    @Override
    public int initLayout() {
        return R.layout.market_activity_search_stock;
    }

    public void updateData(DiffUtil.DiffResult result) {
        result.dispatchUpdatesTo(mSearchResultAdapter);
    }

    public void firstFillUpData(List<MarketStockEntity> data) {
        mSearchResultAdapter.setItems(data);
        mSearchResultAdapter.notifyDataSetChanged();
    }
}
