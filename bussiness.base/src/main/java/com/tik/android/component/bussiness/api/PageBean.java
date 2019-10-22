package com.tik.android.component.bussiness.api;

import android.os.Parcelable;

import java.util.List;

/**
 * @describe :
 * the server response example:
 * {
 *   "code": 0,
 *   "message": "success",
 *   "data": {
 *       "total":10,
 *       "list":"[{bean},{bean},{bean}]",
 *       "page":2,
 *       "pageSize":20
 *
 *   }
 * }
 * @usage :
 *
 * </p>
 * Created by tanlin on 2018/11/24
 */
public class PageBean<T extends Parcelable> {
    private int total;
    private List<T> list;
    private int page;
    private int pageSize;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
