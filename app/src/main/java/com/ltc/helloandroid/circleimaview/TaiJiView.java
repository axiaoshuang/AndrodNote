package com.ltc.helloandroid.circleimaview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ltc on 2016/11/17.
 */

public class TaiJiView extends View {ViewGroup

    private Paint mWhitePaint;
    private Paint mBlackPaint;
    private int width;
    private int height;
    private boolean isVisible=true;
    private int mMin;
    private int mRadius;
    private RectF mRectF;

    public TaiJiView(Context context) {
        this(context,null);
    }

    public TaiJiView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaiJiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //画笔初始化
        mWhitePaint = new Paint();
        mBlackPaint = new Paint();
        mWhitePaint.setColor(Color.WHITE);
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setAntiAlias(true);
        mBlackPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画布移到圆心
        canvas.translate(mMin/2,mMin/2);
        canvas.rotate(rotate);


        canvas.drawArc(mRectF,90,180,true,mWhitePaint);
        canvas.drawArc(mRectF,-90,180,true,mBlackPaint);
        //绘制两个圆
        int smallRadius=mRadius/2;
        canvas.drawCircle(0,-smallRadius,smallRadius,mWhitePaint);
        canvas.drawCircle(0,smallRadius,smallRadius,mBlackPaint);
        //绘制太极两个点
        canvas.drawCircle(0,-smallRadius,mRadius/8,mBlackPaint);
        canvas.drawCircle(0,smallRadius,mRadius/8,mWhitePaint);

    }
    float rotate;
    private void startRotate() {
        postDelayed(() -> {
            if (isVisible) {
                rotate += 5;
                invalidate();
                startRotate();
            }
        },80);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        mMin = Math.min(width, height);
        mRadius = mMin / 2;
        //绘制区域
        mRectF = new RectF(-mRadius, -mRadius, mRadius, mRadius);

    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility==VISIBLE) {
            isVisible = true;
            startRotate();
        }
        else
            isVisible=false;
    }
}
