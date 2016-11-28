package com.ltc.helloandroid.circleimaview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ltc.helloandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RoundImaActivity extends AppCompatActivity {

    @Bind(R.id.pie_chart)
    PieChartView mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_ima);
        ButterKnife.bind(this);
        List<PieChartView.PieChartData> pieChartDatas = new ArrayList<>();
        pieChartDatas.add(new PieChartView.PieChartData(0, "淘宝", 40));
        pieChartDatas.add(new PieChartView.PieChartData(0, "京东", 20));
        pieChartDatas.add(new PieChartView.PieChartData(0, "其他", 40));
        mPieChart.addPieChartDatas(pieChartDatas);

    }
}
