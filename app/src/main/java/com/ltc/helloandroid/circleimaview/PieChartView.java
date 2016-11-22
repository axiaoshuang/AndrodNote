package com.ltc.helloandroid.circleimaview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ltc on 2016/11/22.
 */

public class PieChartView extends View {

    private Paint mPaint;
    private ArrayList<PieChartData> mPieChartDatas;
    private Random mRandom;

    public PieChartView(Context context) {
        this(context,null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mRandom = new Random(257);
        //存放数据类
        mPieChartDatas = new ArrayList<>();
        mPieChartDatas.add(new PieChartData(Color.YELLOW,"淘宝",20));
        mPieChartDatas.add(new PieChartData(Color.BLACK,"京东",10));
        mPieChartDatas.add(new PieChartData(Color.RED,"其他",60));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int min = Math.min(getWidth(), getHeight());
        //将画笔移到中心
        canvas.translate(min/2,min/2);
        RectF rectF = new RectF(-min / 2, -min / 2, min / 2, min / 2);
        //讲数据遍历出来绘制扇形
        int currentAngle=0;
        if (mPieChartDatas != null&&mPieChartDatas.size()>0) {
            for (PieChartData pieChartData : mPieChartDatas) {
                int color = pieChartData.getColor();
                //如果没有颜色的话 随机生成颜色
                if (color==0)color= Color.rgb(mRandom.nextInt(),mRandom.nextInt(),mRandom.nextInt());

                mPaint.setColor(color);
                //绘制扇形
                canvas.drawArc(rectF,currentAngle,pieChartData.angle,true,mPaint);

                //绘制文字canvas.drawText();
                currentAngle+=pieChartData.angle;


            }
        }

    }

    public  void  addPieChartData(PieChartData pieChartData){
        mPieChartDatas.add(pieChartData);
        invalidate();
    }
    public  void  addPieChartDatas( ArrayList<PieChartData> pieChartDatas){
            mPieChartDatas.addAll(pieChartDatas);
            invalidate();
    }
    //数据类
    public class PieChartData{
        private  int color;//颜色
        private  String typeName;//名字
        private  float precent;//百分比


        private float angle;//角度根据百分比计算得出  用户无需关心




        public PieChartData(int color, String typeName, float precent) {
            this.color = color;
            this.typeName = typeName;
            this.precent = precent;
            angle=  360.0f*precent/100.0f;
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
            return precent;
        }

        public void setPrecent(float precent) {
            this.precent = precent;
            angle=  360.0f*precent/100.0f;

        }
    }
}
