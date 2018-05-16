package com.musk.lib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.musk.lib.R;

public class CircleProgressBar1 extends View {
    private Paint mCirclePaint;
    private Paint mCircleInnerPaint;
    private TextPaint mTextPaint;

    private float mProgress;
    private float CIRCLE_RADIUS=90;
    private float CIRCLE_LINE_WIDTH=2;
    private String mProgressText="加载中...";
    private boolean mStartProgress;

    public CircleProgressBar1(Context context) {
        this(context, null);
    }

    public CircleProgressBar1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CircleProgressBar1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(2);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(ContextCompat.getColor(context, R.color.circle_color));

        mCircleInnerPaint = new Paint();
        mCircleInnerPaint.setAntiAlias(true);
        mCircleInnerPaint.setStyle(Paint.Style.FILL);
        mCircleInnerPaint.setColor(ContextCompat.getColor(context, R.color.circle_inner_color));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setColor(ContextCompat.getColor(context, R.color.circle_text_color));
        mTextPaint.setTextSize(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float halfWidth = getMeasuredWidth() / 2;
        float halfHeight = getMeasuredHeight() / 2;

        canvas.drawCircle(halfWidth, halfHeight,CIRCLE_RADIUS - CIRCLE_LINE_WIDTH / 2,mCircleInnerPaint);
        canvas.drawCircle(halfWidth, halfHeight,CIRCLE_RADIUS - CIRCLE_LINE_WIDTH / 2,mCircleInnerPaint);
        canvas.drawText(mProgressText,halfWidth - mTextPaint.measureText(mProgressText) / 2,halfHeight - (mTextPaint.ascent() + mTextPaint.descent()) / 2,mTextPaint);
    }

    public void setProgress(float progress) {
        if (progress > 100) {
            progress = 100;
        }
        if (progress < 0) {
            progress = 0;
        }
        mProgress = progress;
        mProgressText = "Pause";
        mStartProgress = true;
        postInvalidate();
    }
}
