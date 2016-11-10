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

public class DBAdapter extends RecyclerView.Adapter <DBAdapter.DBHolder>{
    private final List list;
    private final Context context;
    private final HashMap<Type,BindingTool> map;

    public DBAdapter(Context context, HashMap<Type,BindingTool> map, List list) {
        this.context=context;
        this.map=map;
        this.list=list;
    }

    @Override
    public DBAdapter.DBHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), viewType, parent, false);
        return new DBHolder(binding);
    }

    @Override
    public void onBindViewHolder(DBAdapter.DBHolder holder, int position) {
        Object object = list.get(position);
        int variableId = map.get(object.getClass()).variableId;
        holder.itemView.setVariable(variableId,object);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {
        return map.get(list.get(position).getClass()).layoutId;
    }


    public   class  DBHolder extends RecyclerView.ViewHolder{
        private  ViewDataBinding itemView;
        public DBHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
        }
    }


    public static  class  BindingTool{
        private  int  layoutId;
        private int variableId;

        public BindingTool(int layoutId, int variableId) {
            this.layoutId = layoutId;
            this.variableId = variableId;
        }
    }
}
