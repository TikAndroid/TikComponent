package com.tik.android.component.market.widget.chart;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 每次绘制操作判断绘制范围，从而根据时间跨度来决定横轴时间显示格式
 * <p>
 * Created by jianglixuan on 2018/11/15
 */
public class BarXAxisRenderer extends XAxisRenderer {

    private Paint mMarkLabelPaint;
    private IXAxisRendererCallback mCallback;
    private MPPointF mPointF;

    public BarXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    public BarXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans, IXAxisRendererCallback callback) {
        super(viewPortHandler, xAxis, trans);
        mCallback = callback;
    }

    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        IAxisValueFormatter valueFormatter = mXAxis.getValueFormatter();
        if (valueFormatter instanceof BarAXisValueFormatter) {
            ((BarAXisValueFormatter) valueFormatter).needUpdateValueRange();
        }
        super.drawLabels(c, pos, anchor);
    }

    public void drawMarkLabels(Canvas c) {
        if (mCallback != null) {
            Highlight[] highlighted = mCallback.getHighlighted();
            if (highlighted == null || highlighted.length == 0) {
                return;
            }
            for (Highlight high : highlighted) {
                float drawX = high.getDrawX();
                float labelY = mViewPortHandler.contentBottom();
                String text = mCallback.getDateForHighlight(high);
                float width = getMarkLabelPaint().measureText(text);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                c.drawRect(drawX - width / 2, labelY + 1, drawX + width / 2, mCallback.getHeight(), paint);
                MPPointF pointF = getMPPointF();
                Utils.drawXAxisValue(c, text, drawX, labelY + mAxis.getYOffset(), getMarkLabelPaint(), pointF, mXAxis.getLabelRotationAngle());
            }
        }
    }

    private MPPointF getMPPointF() {
        if (mPointF == null) {
            mPointF = MPPointF.getInstance(0, 0);
            mPointF.x = 0.5f;
            mPointF.y = 0.0f;
        }
        return mPointF;
    }

    private Paint getMarkLabelPaint() {
        if (mMarkLabelPaint == null) {
            mMarkLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mMarkLabelPaint.setTextSize(mAxisLabelPaint.getTextSize());
            mMarkLabelPaint.setTextAlign(Paint.Align.CENTER);
        }
        return mMarkLabelPaint;
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        super.renderAxisLabels(c);
        drawMarkLabels(c);
    }

    public interface IXAxisRendererCallback {
        Highlight[] getHighlighted();

        int getHeight();

        String getDateForHighlight(Highlight highlight);
    }
}
