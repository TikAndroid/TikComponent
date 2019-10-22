package com.tik.android.component.libcommon.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * @describe : 封装的分页加载， load more
 * @usage :
 *         LoadMoreDelegate.attach(recyclerView, ApiRequest,
 *                 new LoadMoreDelegate.ILoadMore<T>() {
 *
 *             @Override
 *             public void onLoadMore(T data) {
 *
 *             }
 *         });
 * <p>
 * </p>
 * Created by caixi on 2018/11/19.
 */
public class LoadMoreDelegate {

    public interface ILoadMore<T> {
        void onLoadMore(T data);
    }

    public static <T> void attach(@NonNull RecyclerView recyclerView, @NonNull Flowable<T> api, @NonNull ILoadMore loadMore) {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager, loadMore, api));
    }

    private static class EndlessScrollListener<T> extends RecyclerView.OnScrollListener {

        private static final int VISIBLE_THRESHOLD = 6;
        @NonNull
        private final LinearLayoutManager layoutManager;
        @NonNull
        private final ILoadMore loadMore;
        @NonNull
        private final Flowable<T> api;
        @NonNull
        private final AtomicInteger loadingCount;


        private EndlessScrollListener(@NonNull LinearLayoutManager layoutManager, @NonNull ILoadMore loadMore, @NonNull Flowable<T> api) {
            this.layoutManager = layoutManager;
            this.loadMore = loadMore;
            this.api = api;
            loadingCount = new AtomicInteger(0);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy < 0 || isLoadingOrNoMoreData()) return;

            final int itemCount = layoutManager.getItemCount();
            final int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
            final boolean isBottom = (lastVisiblePosition >= itemCount - VISIBLE_THRESHOLD);
            if (isBottom) {
                api.map(new Function<T, T>() {
                    @Override
                    public T apply(T t) throws Exception {
                        notifyLoadingStarted();
                        return t;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .safeSubscribe(new FlowableSubscriber<T>() {
                            @Override
                            public void onSubscribe(Subscription s) {

                            }

                            @Override
                            public void onNext(T t) {
                                if (t != null) {
                                    notifyLoadingFinished();
                                    loadMore.onLoadMore(t);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }

        private boolean isLoadingOrNoMoreData() {
            return loadingCount.get() > 0;
        }

        private void notifyLoadingStarted() {
            loadingCount.getAndIncrement();
        }

        private void notifyLoadingFinished() {
            loadingCount.decrementAndGet();
        }
    }
}
