package com.ltc.helloandroid.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;

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
    private String TAG = "DataBindingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        ButterKnife.bind(this);
        ArrayList users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new User("ltc", "我是内容", "http://cdn.duitang.com/uploads/item/201608/22/20160822233035_HfCuJ.jpeg"));
            users.add(new User2("item2", "我是内容", "http://cdn.duitang.com/uploads/item/201608/22/20160822233035_HfCuJ.jpeg"));

        }

        User user = new User("ltc", "我是ltc", "http://cdn.duitang.com/uploads/item/201608/22/20160822233035_HfCuJ.jpeg");
        binding.setUser(user);

        HashMap<Type, DBAdapter.BindingData> map = new HashMap<>();
        map.put(User.class, new DBAdapter.BindingData(R.layout.db_recycler_item, BR.user));
        map.put(User2.class, new DBAdapter.BindingData(R.layout.db_recycler_item2, BR.user2));
        Log.i(TAG, "onCreate: " + users.size());
        mDbRv.setAdapter(new DBAdapter(this, map, users));


    }
}
