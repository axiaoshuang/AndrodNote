package com.ltc.helloandroid.circleimaview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ltc on 2016/11/22.
 */

public class PieChartView extends View {

    private Paint mPaint;
    private List<PieChartData> mPieChartDatas;
    private Random mRandom;
    private RectF mRectF;
    private int mMin;
    private Paint mLinePaint;
    private Paint mTextPaint;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(2.0f);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(18);

        mRandom = new Random();
        //存放数据类
        mPieChartDatas = new ArrayList<>();
        mPieChartDatas.add(new PieChartData(0, "淘宝", 20));
        mPieChartDatas.add(new PieChartData(0, "京东", 10));
        mPieChartDatas.add(new PieChartData(0, "其他", 60));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius=mMin/2*0.8f;
        //将画笔移到中心
        canvas.translate(getWidth()/2, getHeight()/2);
        //讲数据遍历出来绘制扇形
        int currentAngle = 0;
        if (mPieChartDatas != null && mPieChartDatas.size() > 0) {
            for (PieChartData pieChartData : mPieChartDatas) {
                if(currentAngle>360)
                  throw  new RuntimeException("do not exceed 100 percent");

                int color = pieChartData.getColor();
                //如果没有颜色的话 随机生成颜色
                if (color == 0) {
                    color = Color.rgb(mRandom.nextInt(257), mRandom.nextInt(257), mRandom.nextInt(257));
                }
                mPaint.setColor(color);
                //绘制扇形
                canvas.drawArc(mRectF, currentAngle, pieChartData.angle, true, mPaint);
                float angle = currentAngle + pieChartData.angle / 2;
                float pxs = (float) (radius*Math.cos(Math.toRadians(angle)));
                float pys = (float) (radius*Math.sin(Math.toRadians(angle)));
                float pxt = (float) ((radius+radius/10f)*Math.cos(Math.toRadians(angle)));
                float pyt = (float) ((radius+radius/10f)*Math.sin(Math.toRadians(angle)));
                float textX= (float) (radius/2*Math.cos(Math.toRadians(angle)));
                float textY = (float) (radius/2*Math.sin(Math.toRadians(angle)));

                canvas.drawLine(pxs,pys,pxt,pyt,mLinePaint);

                canvas.drawText(pieChartData.getTypeName(),textX-mTextPaint.measureText(pieChartData.getTypeName())/2,textY+mTextPaint.getTextSize(),mTextPaint);
                //绘制文字canvas.drawText();
                String format = String.format(Locale.getDefault(), "%.2f%%", pieChartData.getPrecent());
                if (!(currentAngle>=90&&currentAngle<=270))
                canvas.drawText(format,pxt-mTextPaint.measureText(format)/2,pyt+mTextPaint.getTextSize(),mTextPaint);
                else
                canvas.drawText(format,pxt-mTextPaint.measureText(format)/2,pyt,mTextPaint);

                currentAngle += pieChartData.angle;


            }
        }
        canvas.drawCircle(0,0,radius,mLinePaint);


    }

    public void addPieChartData(PieChartData pieChartData) {
        mPieChartDatas.add(pieChartData);
        invalidate();
    }

    public void addPieChartDatas(@NonNull  List<PieChartData> pieChartDatas) {
        mPieChartDatas.addAll(pieChartDatas);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMin = Math.min(getWidth(), getHeight());

        mRectF = new RectF(-mMin / 2*0.8f,-mMin / 2*0.8f,mMin / 2*0.8f,mMin / 2*0.8f);
    }

    //数据类
    public class PieChartData {
        private int color;//颜色
        private String typeName;//名字
        private float percent;//百分比


        private float angle;//角度根据百分比计算得出  无需关心


        public PieChartData(int color, String typeName, float percent) {
            this.color = color;
            this.typeName = typeName;
            this.percent = percent;
            angle = 360.0f * percent / 100.0f;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public float getPrecent() {
            return percent;
        }

        public void setPrecent(float percent) {
            this.percent = percent;
            angle = 360.0f * percent / 100.0f;

        }
    }


}
