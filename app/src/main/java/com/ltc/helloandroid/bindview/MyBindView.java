package com.ltc.helloandroid.bindview;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.tool.expr.StaticIdentifierExpr;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.ltc.helloandroid.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by litiancheng on 2016/12/29.
 */

public class MyBindView {


    public static void bind(Activity activity) {
        bindView(activity);
        clickView(activity);

    }

    private static void clickView(Activity activity) {
        //拿到所有方法
        Method[] methods = activity.getClass().getDeclaredMethods();
        for (Method method : methods) {
            //拿到注解
            Onclick onclick = method.getAnnotation(Onclick.class);

            if (onclick != null) {
                int[] id = onclick.value();
                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes != null && parameterTypes.length == 1) {
                    Class<?> aClass = parameterTypes[0];

                    if (View.class.isAssignableFrom(aClass)) {
                        for (int i : id) {
                            View view = activity.findViewById(i);
                            method.setAccessible(true);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        method.invoke(activity, v);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    } else
                        throw new RuntimeException("param type must be view");

                } else
                    throw new RuntimeException("param length must be 1");


            }

        }

    }

    private static void bindView(Activity activity) {

        //拿到所有的成员
        Field[] declaredFields = activity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            //判断是否为view的子类
            if (View.class.isAssignableFrom(type)) {
                //如果是拿到注解的id set给成员
                BindView annotation = field.getAnnotation(BindView.class);
                int value = annotation.value();
                field.setAccessible(true);
                if (value != 0) {

                    try {
                        field.set(activity, activity.findViewById(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static void bind(Fragment fragment, View view) {

        //拿到所有的成员
        Field[] declaredFields = fragment.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type = field.getType();
            //判断是否为view的子类
            if (View.class.isAssignableFrom(type)) {
                //如果是拿到注解的id set给成员
                BindView annotation = field.getAnnotation(BindView.class);
                int value = annotation.value();
                field.setAccessible(true);
                if (value != 0) {

                    try {
                        field.set(fragment, view.findViewById(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
    }
}
