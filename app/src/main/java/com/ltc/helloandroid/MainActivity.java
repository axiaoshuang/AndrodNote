package com.ltc.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ltc.helloandroid.circleimaview.RoundImaActivity;
import com.ltc.helloandroid.databinding.DataBindingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.data_binding)
    Button mDataBinding;
    @Bind(R.id.circle_ima)
    Button mCircleIma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }




    @OnClick({R.id.data_binding, R.id.circle_ima, R.id.mobile_vision})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.data_binding:
                intent = new Intent(this, DataBindingActivity.class);
                break;
            case R.id.circle_ima:
                intent = new Intent(this, RoundImaActivity.class);
                break;

        }
        startActivity(intent);

    }
}
