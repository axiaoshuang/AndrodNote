package com.ltc.helloandroid.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;

import java.util.Locale;

/**
 * Created by ltc on 2016/11/8.
 */

public class User2 extends BaseObservable implements Cloneable {
    private String userName;
    private String content;

    public String getImaUrl() {
        return imaUrl;
    }

    public void setImaUrl(String imaUrl) {
        this.imaUrl = imaUrl;
    }

    private String imaUrl;


    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public User2(String userName, String content, String url) {
        this.userName = userName;
        this.content = content;
        this.imaUrl = url;
    }


    @Override
    public User2 clone() {
        User2 sc = null;
        try {
            sc = (User2) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void onClick(View view) {
        setContent(String.format(Locale.getDefault(),getContent()+"%s","被点击了"));
        //刷新局部 通过@Bindable注释绑定字段
        notifyPropertyChanged(BR.content);
        //全部刷新 notifyChange();
    }

    @BindingAdapter({"imageUrl"})
    public static void imaUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);

    }
}
