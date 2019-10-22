package com.tik.android.component.bussiness.service.mine;

public interface IMineService {

    /**
     * @param delay
     * @param toast    是否显示toast
     * @param progress 是否显示loading
     */
    void doAppCheck(long delay, boolean toast, boolean progress);
}
