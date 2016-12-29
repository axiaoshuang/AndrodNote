package com.ltc.helloandroid.bindview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ltc.helloandroid.R;

/**
 * 依赖注入findviewbyid和onlcik事件
 * butterknife基于编译时注入代码 这个比较简单属于运行时注入
 */

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
    @Onclick({R.id.bind_text1,R.id.bind_text2})
    private  void  textClick(View view){
        Toast.makeText(this, "view="+view.toString(), Toast.LENGTH_SHORT).show();

    }
}
