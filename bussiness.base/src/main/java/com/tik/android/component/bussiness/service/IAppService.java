package com.tik.android.component.bussiness.service;

import android.os.Bundle;

public interface IAppService {
    int TAB_INDEX_MARKET = 0;
    int TAB_INDEX_TRADE = 1;
    int TAB_INDEX_PROPERTY = 2;
    int TAB_INDEX_MINE = 3;

    /**
     * 切换到对应的tab
     * @param tabIndex tab的索引，需是以下值：<br/>
     * {@link #TAB_INDEX_MARKET}<br/>
     * {@link #TAB_INDEX_TRADE}<br/>
     * {@link #TAB_INDEX_PROPERTY}<br/>
     * {@link #TAB_INDEX_MINE}
     *
     * @param args 传递给fragment的参数
     */
    void switchToTab(int tabIndex, Bundle args);
}
