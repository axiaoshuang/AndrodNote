package com.ltc.helloandroid.bindview;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by litiancheng on 2016/12/29.
 */

public class MyBindView {


    public static void bind(Activity activity){
        //拿到所有的成员
        Field[] declaredFields = activity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            //判断是否为view的子类
            if (type.isAssignableFrom(View.class)){
                //如果是拿到注解的id set给成员
                BindView annotation = type.getAnnotation(BindView.class);
                int value = annotation.value();
                field.setAccessible(true);
                if (value!=0){

                    try {
                        field.set(activity,activity.findViewById(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }



            }
        }
    }
}
