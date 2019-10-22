package com.tik.android.component.libcommon.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.tik.android.component.libcommon.AppUtil;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @describe :
 * 1. 支持RxJava流式调用,方便线程切换 {@link RxSharedPrefer#readAsFlowable()} && {@link RxSharedPrefer#editAsFlowable()} ()}
 * 2. 支持直接同步getString/int/boolean ， {@link RxSharedPrefer#read()}
 * @usage :
 * <p>
 * --read some key-value
 * RxSharedPrefer.builder()
 * .context(getContext())
 * .file("filename")  // optional
 * .build()
 * .read()
 * .getXxx(KYE, DEFAULT_VALUE);
 * <p>
 * --edit some key-value
 * RxSharedPrefer.builder()
 * .context(getContext())
 * .file("filename")  // optional
 * .build()
 * .edit()
 * .putAaa(aKey, aValue)
 * .putBbb(bKey, bValue)
 * .apply(); // or .commit();
 * </p>
 * Created by caixi on 2018/11/15.
 */
public class RxSharedPrefer implements IRxSharedPrefer {

    private Context context;
    private String file;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static volatile RxSharedPrefer INSTANCE;

    public static Builder builder() {
        return new Builder();
    }

    public RxSharedPrefer(@NonNull Builder builder) {
        this.context = builder.context;
        if (TextUtils.isEmpty(builder.file)) {
            this.file = getDefaultSharedPreferencesName(context);
        } else {
            this.file = builder.file;
        }
    }

    @NonNull
    @Override
    public SharedPreferences read() {
        if (sp == null) {
            sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        }
        return sp;
    }

    @NonNull
    @Override
    public SharedPreferences.Editor edit() {
        if (editor == null) {
            editor = read().edit();
        }
        return editor;
    }

    @NonNull
    @Override
    public Flowable<SharedPreferences> readAsFlowable() {
        if (sp == null) {
            sp = read();
        }
        return Flowable.just(sp);
    }


    @NonNull
    @Override
    public Flowable<SharedPreferences.Editor> editAsFlowable() {
        if (editor == null) {
            editor = edit();
        }
        return Flowable.just(editor);
    }

    @Override
    public void readAsAsyncFlowable(@NonNull Consumer<SharedPreferences> consumer) {
        readAsFlowable()
                .flatMap(new Function<SharedPreferences, Publisher<SharedPreferences>>() {
                    @Override
                    public Publisher<SharedPreferences> apply(SharedPreferences sharedPreferences) {
                        return AppUtil.isInBackgroundThread() ?
                                Flowable.just(sharedPreferences) :
                                Flowable.just(sharedPreferences).observeOn(Schedulers.computation());
                    }
                })
                .subscribe(new DisposableSubscriber<SharedPreferences>() {
                    @Override
                    public void onNext(SharedPreferences sharedPreferences) {
                        consumer.accept(sharedPreferences);
                    }

                    @Override
                    public void onError(Throwable t) {
                        consumer.error(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void editAsAsyncFlowable(@NonNull Consumer<SharedPreferences.Editor> consumer) {
        editAsFlowable()
                .flatMap(new Function<SharedPreferences.Editor, Publisher<SharedPreferences.Editor>>() {
                    @Override
                    public Publisher<SharedPreferences.Editor> apply(SharedPreferences.Editor editor) {
                        return AppUtil.isInBackgroundThread() ?
                                Flowable.just(editor) :
                                Flowable.just(editor).observeOn(Schedulers.computation());
                    }
                })
                .subscribe(new DisposableSubscriber<SharedPreferences.Editor>() {
                    @Override
                    public void onNext(SharedPreferences.Editor editor) {
                        consumer.accept(editor);
                    }

                    @Override
                    public void onError(Throwable t) {
                        consumer.error(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * @param context
     * @return
     * @see PreferenceManager#getDefaultSharedPreferencesName(Context)
     */
    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    public static class Builder {
        private Context context;
        private String file;

        public Context getContext() {
            return context;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public String getFile() {
            return file;
        }

        public Builder file(String file) {
            this.file = file;
            return this;
        }

        public RxSharedPrefer build() {
            return new RxSharedPrefer(this);
        }

        public RxSharedPrefer buildAccountInstance() {
            if (RxSharedPrefer.INSTANCE == null) {
                synchronized (RxSharedPrefer.class) {
                    if (RxSharedPrefer.INSTANCE == null) {
                        RxSharedPrefer.INSTANCE = build();
                    }
                }
            }
            return RxSharedPrefer.INSTANCE;
        }
    }
}
