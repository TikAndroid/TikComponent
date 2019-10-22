package com.tik.android.component.information;

import com.tik.android.component.basemvp.Result;
import com.tik.android.component.information.bean.category.CategoryData;
import com.tik.android.component.information.bean.details.DetailsData;
import com.tik.android.component.information.bean.list.ListData;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InfoApi {
    @GET("cms/categories")
    Flowable<Result<CategoryData>> loadInfoCategories();

    @GET("cms/list")
    Flowable<Result<List<ListData>>> loadInfoList(
            @Query("categoryId") int categoryId,
            @Query("limit") int limit,
            @Query("page") int page
    );

    @GET("cms/article")
    Flowable<Result<DetailsData>> loadInfoDetails(
            @Query("articleId") int articleId
    );

}
