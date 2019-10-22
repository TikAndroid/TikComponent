package com.tik.android.component.property;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tik.android.component.basemvp.BaseMVPFragment;
import com.tik.android.component.bussiness.property.bean.StockAssets;
import com.tik.android.component.property.bean.PropertyData;
import com.tik.android.component.property.contract.PropertyContract;
import com.tik.android.component.property.multitype.HeaderViewBinder;
import com.tik.android.component.property.multitype.PropertyItemViewBinder;
import com.tik.android.component.property.presenter.PropertyPresenter;
import com.tik.android.component.property.util.PropertyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.drakeet.multitype.MultiTypeAdapter;

public class PropertyFragment extends BaseMVPFragment<PropertyPresenter> implements PropertyContract.View,HeaderViewBinder.OnVisibleClickListener {

    public static final String TAG = "PropertyFragment";

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    MultiTypeAdapter mAdapter;
    List<Object> mItems = new ArrayList<>();


    public static PropertyFragment newInstance() {
        return new PropertyFragment();
    }

    @Override
    protected void init(View view) {
        super.init(view);
        initRefreshLayout();
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(PropertyData.class, new HeaderViewBinder(this));
        mAdapter.register(StockAssets.class, new PropertyItemViewBinder());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.getTotalData();
    }

    private void initRefreshLayout() {
        mSmartRefreshLayout.setRefreshHeader(new MaterialHeader(getContext()).setShowBezierWave(false));
        mSmartRefreshLayout.setEnableOverScrollBounce(false);
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setEnableOverScrollDrag(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getTotalData();
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.property_fragment_property;
    }

    @Override
    public void showTotalData(PropertyData propertyData) {
        mSmartRefreshLayout.finishRefresh();
        if (propertyData == null) {
            return;
        }
        mItems.clear();
        mItems.add(0, propertyData);
        mItems.addAll(propertyData.getStock());
        checkHideItem();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        mSmartRefreshLayout.finishRefresh();
    }

    @Override
    public void onVisibleClick() {
        checkHideItem();
        mAdapter.notifyDataSetChanged();
    }

    public void checkHideItem() {
        if (PropertyUtil.getShouldHideValue()) {
            List<Object> headerItem = new ArrayList<>();
            headerItem.add(mItems.get(0));
            mAdapter.setItems(headerItem);
        } else {
            mAdapter.setItems(mItems);
        }
    }
}
