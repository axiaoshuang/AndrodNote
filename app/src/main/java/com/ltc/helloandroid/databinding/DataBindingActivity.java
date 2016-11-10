package com.ltc.helloandroid.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.ltc.helloandroid.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DataBindingActivity extends AppCompatActivity {

    @Bind(R.id.db_rv)
    RecyclerView mDbRv;
    private String TAG="DataBindingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        ButterKnife.bind(this);
        User user = new User("ltc", "我是ltc", "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png");
        binding.setUser(user);
        ArrayList users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(user);
        }
        HashMap<Type, DBAdapter.BindingTool> map = new HashMap<>();
        map.put(User.class,new DBAdapter.BindingTool(R.layout.db_recycler_item, BR.user));
        Log.i(TAG, "onCreate: "+ users.size());
        mDbRv.setAdapter(new DBAdapter(this,map,users));


    }
}
