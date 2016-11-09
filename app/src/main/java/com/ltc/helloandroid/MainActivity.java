package com.ltc.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ltc.helloandroid.databinding.DataBindingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    @Bind(R.id.data_binding)
    Button dataBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.data_binding)
    public void onClick() {
        Intent intent = new Intent(this, DataBindingActivity.class);
        startActivity(intent);
    }
}
