package com.ltc.helloandroid.bindview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ltc.helloandroid.R;


public class BindViewActivity extends AppCompatActivity {
    @BindView(R.id.bind_text1)
    TextView mTextView1;
    @BindView(R.id.bind_text2)
    TextView mTextView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_view);
        MyBindView.bind(this);
        mTextView1.setText("bind success");
    }
}
