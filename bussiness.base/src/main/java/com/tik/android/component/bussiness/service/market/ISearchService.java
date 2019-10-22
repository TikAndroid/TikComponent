package com.tik.android.component.bussiness.service.market;

import android.os.Bundle;

/**
 * Created by wangqiqi on 2018/11/21.
 */
public interface ISearchService {
    /**
     *
     * @param bundle 插入数据库的item
     * @param view 绑定生命周期的对象，activity/fragment
     */
    void insertSearchDB(Bundle bundle, Object view);
}
