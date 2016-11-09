package com.ltc.helloandroid.databinding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by 李天成 on 2016/11/8.
 */

public class User implements Cloneable {
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
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.getUserName().equals(getUserName());
        }

        return super.equals(obj);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void onClick(View view){
        Toast.makeText(view.getContext(), content, Toast.LENGTH_SHORT).show();
    }
    @BindingAdapter("bind:imageUrl")
    public static void imaUrl(ImageView imageView ,String url){
        Glide.with(imageView.getContext()).load(url).into(imageView);

    }
}
