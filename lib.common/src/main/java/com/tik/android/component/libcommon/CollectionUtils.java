package com.tik.android.component.libcommon;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

/**
 * Created by baowei on 18-2-4.
 */

public class CollectionUtils {

    public static <T> int size(T... t) {
        return t == null ? 0 : t.length;
    }

    public static boolean isNotBlank(Collection<?> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isBlank(Collection<?> list) {
        return !isNotBlank(list);
    }

    public interface TraversalWeakRefListener<T> {
        boolean onNext(int index, T item);
    }

    public interface TraversalWeakRefListenerWithReduce<T, R> {
        boolean onNext(int index, T item, TraversalReduce<R> result);
    }

    public static class TraversalReduce <T> {
        public T data;

        public TraversalReduce(T data) {
            this.data = data;
        }
    }

    public static <T> void traversalWeakRefListAndRemoveEmpty(List<WeakReference<T>> list, TraversalWeakRefListener<T> listener) {
        if (isNotBlank(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    T item = list.get(i).get();
                    if (item != null) {
                        if (listener.onNext(i, item)) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }

                list.remove(i);
                i--;
            }
        }
    }

    public static <T, R> void traversalWeakRefListAndRemoveEmpty(List<WeakReference<T>> list, TraversalReduce<R> reduce, TraversalWeakRefListenerWithReduce<T, R> listener) {
        if (isNotBlank(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    T item = list.get(i).get();
                    if (item != null) {
                        if (listener.onNext(i, item, reduce)) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }

                list.remove(i);
                i--;
            }
        }
    }
}
