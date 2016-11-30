package com.ltc.helloandroid.databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ltc on 2016/11/10.
 */

public class DBAdapter extends RecyclerView.Adapter<DBAdapter.DBViewHolder> {
    private final List list;//数据源
    private final Context context;
    private final HashMap<Type, BindingData> map;//存放viewmodel类型 和对应的id

    public DBAdapter(Context context, HashMap<Type, BindingData> map, List list) {
        this.context = context;
        this.map = map;
        this.list = list;
    }

    @Override
    public DBAdapter.DBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), viewType, parent, false);
        return new DBViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DBAdapter.DBViewHolder holder, int position) {
        Object object = list.get(position);
        int variableId = map.get(object.getClass()).variableId;
        holder.itemView.setVariable(variableId, object);//数据绑定
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        //把layoutId当做viewType
        return map.get(list.get(position).getClass()).layoutId;
    }


    public class DBViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding itemView;

        public DBViewHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }


    public static class BindingData {
        private int layoutId;
        private int variableId;

        public BindingData(int layoutId, int variableId) {
            this.layoutId = layoutId;
            this.variableId = variableId;
        }
    }
}
