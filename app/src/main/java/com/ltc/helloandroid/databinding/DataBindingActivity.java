package com.ltc.helloandroid.databinding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.ltc.helloandroid.databinding.DBTestBinding;

import com.ltc.helloandroid.R;

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding);
        DBTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        User user = new User("张三", "我是张三","https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png");
        binding.setUser(user);
    }
}
