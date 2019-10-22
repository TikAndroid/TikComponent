package com.tik.android.component.libcommon;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;


/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/12/7.
 */
public class SpannableBuilder {
    private SpannableString mSpannableString;

    int mStart;
    int mEnd;

    private SpannableBuilder(String string) {
        if (string == null) {
            string = "";
        }
        mSpannableString = new SpannableString(string);
    }

    public void move(int start, int end) {
        int len = mSpannableString.length();
        start = Math.min(len, Math.max(0, start)); // 0 <= start <= len
        end = Math.min(len, Math.max(start, end)); // start <= end <= len

        mStart = start;
        mEnd = end;
    }

    public SpannableBuilder span(int start, int end, @NonNull Object span) {
        mSpannableString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableBuilder span(@NonNull Object span) {
        mSpannableString.setSpan(span, mStart, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpannableString build() {
        return mSpannableString;
    }

    public static SpannableBuilder create(String string) {
        return create(string, 0, 0);
    }

    public static SpannableBuilder create(String string, int start, int end) {
        SpannableBuilder builder = new SpannableBuilder(string);
        builder.move(start, end);
        return builder;
    }

}
