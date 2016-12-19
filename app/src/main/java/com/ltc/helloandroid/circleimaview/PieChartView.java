package com.ltc.helloandroid.circleimaview;

import android.content.Context;
import android.databinding.generated.callback.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
    private float radius;


    private PieChartItemListener mPieChartItemListener;

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
        mTextPaint.setTextSize(16);

        mRandom = new Random();
        //存放数据类
        mPieChartDatas = new ArrayList<>();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将画笔移到中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        //讲数据遍历出来绘制扇形
        int currentAngle = 0;
        if (mPieChartDatas != null && mPieChartDatas.size() > 0) {
            for (PieChartData pieChartData : mPieChartDatas) {
                if (currentAngle + pieChartData.angle > 360)
                    throw new RuntimeException("do not exceed 100 percent");

                int color = pieChartData.getColor();
                //如果没有颜色的话 随机生成颜色
                if (color == 0) {
                    color = Color.rgb(mRandom.nextInt(257), mRandom.nextInt(257), mRandom.nextInt(257));
                }
                mPaint.setColor(color);
                //绘制扇形
                canvas.drawArc(mRectF, currentAngle, pieChartData.angle, true, mPaint);
                float angle = currentAngle + pieChartData.angle / 2;
                float pxs = (float) (radius * Math.cos(Math.toRadians(angle)));
                float pys = (float) (radius * Math.sin(Math.toRadians(angle)));
                float pxt = (float) ((radius + radius / 10f) * Math.cos(Math.toRadians(angle)));
                float pyt = (float) ((radius + radius / 10f) * Math.sin(Math.toRadians(angle)));
                float textX = (float) (radius / 2 * Math.cos(Math.toRadians(angle)));
                float textY = (float) (radius / 2 * Math.sin(Math.toRadians(angle)));

                canvas.drawLine(pxs, pys, pxt, pyt, mLinePaint);

                canvas.drawText(pieChartData.getTypeName(), textX - mTextPaint.measureText(pieChartData.getTypeName()) / 2, textY + mTextPaint.getTextSize(), mTextPaint);
                //绘制文字canvas.drawText();
                String format = String.format(Locale.getDefault(), "%.2f%%", pieChartData.getPrecent());


                currentAngle += pieChartData.angle;
                if (currentAngle >= 0 && currentAngle <= 180)
                    canvas.drawText(format, pxt - mTextPaint.measureText(format) / 2, pyt + mTextPaint.getTextSize(), mTextPaint);
                else if (currentAngle > 180 && currentAngle - pieChartData.angle / 2 == 180)
                    canvas.drawText(format, pxt - mTextPaint.measureText(format), pyt + mTextPaint.getTextSize() / 2, mTextPaint);
                else if (currentAngle > 270 && currentAngle <= 360 && currentAngle - pieChartData.angle > 270)
                    canvas.drawText(format, pxt, pyt, mTextPaint);
                else
                    canvas.drawText(format, pxt - mTextPaint.measureText(format) / 2, pyt, mTextPaint);


            }
        }
        canvas.drawCircle(0, 0, radius, mLinePaint);


    }

    public void addPieChartData(PieChartData pieChartData) {
        mPieChartDatas.add(pieChartData);
        invalidate();
    }

    public void addPieChartDatas(@NonNull List<PieChartData> pieChartDatas) {
        mPieChartDatas.addAll(pieChartDatas);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMin = Math.min(getWidth(), getHeight());

        radius = mMin / 2 * 0.6f;
        mRectF = new RectF(-radius, -radius, radius, radius);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX() - mMin / 2;
                float y = event.getY() - mMin / 2;
                double touchAngle = Math.toDegrees(Math.atan(y / x));

                if (x < 0 && y < 0) {  //2 象限
                    touchAngle += 180.0;
                } else if (y < 0 && x > 0) {  //1象限
                    touchAngle += 360.0;
                } else if (y > 0 && x < 0) {  //3象限
                    touchAngle += 180;
                }

                int currentAngle = 0;

                for (PieChartData pieChartData : mPieChartDatas) {
                    if (mPieChartItemListener == null)
                        break;
                    float angle = pieChartData.angle;
                    currentAngle += angle;
                    if ((touchAngle > angle && touchAngle < currentAngle) || (currentAngle == angle && touchAngle < angle)) {
                        mPieChartItemListener.onPieChartItemClick(pieChartData);
                        break;
                    }
                }

                break;


        }
        return super.onTouchEvent(event);
    }


    public void setPieChartItemListener(PieChartItemListener pieChartItemListener) {
        mPieChartItemListener = pieChartItemListener;
    }

    //数据类
    public static class PieChartData {
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

    public interface PieChartItemListener {
        void onPieChartItemClick(PieChartData data);
    }
}
