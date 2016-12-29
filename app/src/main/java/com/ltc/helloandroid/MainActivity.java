package com.ltc.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ltc.helloandroid.bindview.BindViewActivity;
import com.ltc.helloandroid.circleimaview.RoundImaActivity;
import com.ltc.helloandroid.databinding.DataBindingActivity;
import com.ltc.helloandroid.rxjava2.Rxjava2Activity;
import com.ltc.helloandroid.tinker.app.TinkerActivity;

import org.xmlpull.v1.XmlPullParser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TinkerActivity";

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @OnClick({R.id.data_binding, R.id.circle_ima, R.id.rxjava_2, R.id.tinker,R.id.bindView})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.data_binding:
                intent = new Intent(this, DataBindingActivity.class);
                break;
            case R.id.circle_ima:
                intent = new Intent(this, RoundImaActivity.class);
                break;
            case R.id.rxjava_2:
                intent = new Intent(this, Rxjava2Activity.class);
                break;
            case R.id.tinker:
                intent = new Intent(this, TinkerActivity.class);
                break;
            case R.id.bindView:
                intent = new Intent(this, BindViewActivity.class);
                break;


        }
        startActivity(intent);

    }
}
