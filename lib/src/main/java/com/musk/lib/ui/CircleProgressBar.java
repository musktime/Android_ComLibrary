package com.musk.lib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {

    private int progress;
    private int max = 100;
    private int strokeWidth = 10;
    private Paint paint;
    private RectF oval;
    private float centerX = 122, centerY = 122;
    private int circleColor = Color.rgb(69, 204, 70);
    private int circlebg = Color.rgb(238, 238, 238);


    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        oval = new RectF();
    }

    public int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public synchronized void setColor(int circleColor) {
        this.circleColor = circleColor;
        invalidate();
    }

    public synchronized void setCenter(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        width = width > height ? width : height;
        height = width > height ? width : height;
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAlpha(0);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX, centerY, 120, paint);
        oval.set(strokeWidth, strokeWidth + 2, width - strokeWidth, height
                - strokeWidth);
        paint.setColor(circlebg);
        canvas.drawArc(oval, -90, 360, false, paint);
        paint.setColor(circleColor);
        canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, paint);
    }
}
