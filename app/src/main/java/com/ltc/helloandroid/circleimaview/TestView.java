package com.ltc.helloandroid.circleimaview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by litiancheng on 2016/11/21.
 */

public class TestView extends View {

    private Paint mPaint;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(12.0f);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth()/2,getHeight()/2);
        RectF rectF = new RectF(-getWidth()/2, -getHeight()/2, getWidth()/2, getHeight()/2);
        for (int i = 0; i < 20; i++) {
//            canvas.rotate(20);
            canvas.scale(0.9f,0.9f);
            canvas.drawRect(rectF,mPaint);
        }

    }
}
