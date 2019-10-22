package com.tik.android.component.market.widget.chart;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by jianglixuan on 2018/12/7
 */
public class HoxCandleChartRenderer extends CandleStickChartRenderer {
    private CrossIconBitmapCallback mCallback;

    public HoxCandleChartRenderer(CandleDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        super.drawHighlighted(c, indices);
        Bitmap bitmap;
        if (mCallback == null || (bitmap = mCallback.getCrossBitmap()) == null) {
            return;
        }
        for (Highlight high : indices) {
            float drawX = high.getDrawX();
            float drawY = high.getDrawY();
            if (bitmap.isRecycled()) {
                return;
            }
            c.drawBitmap(bitmap, drawX - bitmap.getWidth() / 2, drawY - bitmap.getHeight() / 2, mDrawPaint);
        }
    }

    public void setCrossIconBitmapCallback(CrossIconBitmapCallback callback) {
        mCallback = callback;
    }

    public interface CrossIconBitmapCallback {
        Bitmap getCrossBitmap();
    }
}
