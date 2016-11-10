package com.ltc.helloandroid.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;

/**
 * Created by ltc on 2016/11/8.
 */

public class User extends BaseObservable implements Cloneable {
    private String userName;
    private String psw;
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
                ", psw='" + psw + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public User(String userName,String content,String url) {
        this.userName = userName;
        this.content = content;
        this.imaUrl=url;
    }



    @Override
    public User clone(){
        User sc = null;
        try
        {
            sc = (User) super.clone();
        } catch (CloneNotSupportedException e){
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

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void onClick(View view){
        setContent("张三被点击了");
        //刷新局部 通过@Bindable注释绑定字段
       notifyPropertyChanged(BR.content);
        //全部刷新 notifyChange();
    }
    @BindingAdapter("imageUrl")
    public  static void imaUrl(ImageView imageView ,String url){
        Glide.with(imageView.getContext()).load(url).into(imageView);

    }
}
