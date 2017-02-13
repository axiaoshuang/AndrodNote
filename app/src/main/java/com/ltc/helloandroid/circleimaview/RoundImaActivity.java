package com.ltc.helloandroid.circleimaview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ltc.helloandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RoundImaActivity extends AppCompatActivity {

    @Bind(R.id.pie_chart)
    PieChartView mPieChart;
    @Bind(R.id.activity_round_ima)
    ImageView mActivityRoundIma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_ima);
        ButterKnife.bind(this);
        List<PieChartView.PieChartData> pieChartDatas = new ArrayList<>();
        pieChartDatas.add(new PieChartView.PieChartData(0, "淘宝", 40));
        pieChartDatas.add(new PieChartView.PieChartData(0, "京东", 20));
        pieChartDatas.add(new PieChartView.PieChartData(0, "其他", 30));
        mPieChart.addPieChartDatas(pieChartDatas);
        mPieChart.setPieChartItemListener(data -> Toast.makeText(RoundImaActivity.this, data.getTypeName(), Toast.LENGTH_SHORT).show());


    }

}
