package com.tik.android.component.trade.module.order;

import android.support.v7.util.DiffUtil;

import com.tik.android.component.trade.module.order.bean.OrderInfo;

import java.util.List;

/**
 * @describe : DiffUtils for order-info
 * @usage :
 *          // in background,
 *          DiffCallBackUtil diffCallBack = new DiffCallBackUtil(orderInfos, data);
 *          return DiffUtil.calculateDiff(diffCallBack);
 *
 *          // post to main
 *          diffResult.dispatchUpdatesTo(mAdapter);
 *          mAdapter.setItems(newOrdersList);
 *          // or {@code
 *              mDatas.removeAll(start, end);
 *              mDatas.addAll(start, newOrdersList)
 *          }
 *
 *
 * </p>
 * Created by tanlin on 2018/12/1
 */
public class DiffCallBackUtil extends DiffUtil.Callback {
    private final List mInfosOld;
    private final List mInfosNew;

    public DiffCallBackUtil(List old, List data) {
        super();
        this.mInfosOld = old;
        this.mInfosNew = data;
    }

    @Override
    public int getOldListSize() {
        return mInfosOld.size();
    }

    @Override
    public int getNewListSize() {
        return mInfosNew.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return compare(i, i1);
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return compare(i, i1);
    }

    private boolean compare(int i, int i1) {
        Object o1 = mInfosOld.get(i);
        Object o2 = mInfosNew.get(i1);
        boolean result;
        if (o1 instanceof OrderInfo && o2 instanceof OrderInfo) {
            result = ((OrderInfo) o1).getTradeInfo().getId().equals(((OrderInfo) o2).getTradeInfo().getId());
        } else {
            result = o1 == o2;
        }

        return result;
    }
}
