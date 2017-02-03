package com.ltc.helloandroid.bindview;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.tool.expr.StaticIdentifierExpr;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.ltc.helloandroid.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by litiancheng on 2016/12/29.
 *
 *
 */
public class MyBindView {


    public static void bind(@NonNull Activity activity) {
        bindView(activity, null);
        clickView(activity, null);

    }


    private static void clickView(Object object, @Nullable View parentView) {
        //拿到所有方法
        Method[] methods = object.getClass().getDeclaredMethods();
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
                            View view;
                            if (parentView == null) {
                                view = ((Activity) object).findViewById(i);
                            } else
                                view = parentView.findViewById(i);
                            if (view != null) {
                                method.setAccessible(true);
                                view.setOnClickListener(v -> {
                                    try {
                                        method.invoke(object, v);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    } else
                        throw new RuntimeException("param type must be view");

                } else
                    throw new RuntimeException("param length must be 1");


            }

        }

    }

    private static void bindView(Object object, View view) {

        //拿到所有的成员
        Field[] declaredFields = object.getClass().getDeclaredFields();
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
                        if (view == null)
                            field.set(object, ((Activity) object).findViewById(value));
                        else
                            field.set(object, view.findViewById(value));

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static void bind(@NonNull Fragment fragment, @NonNull View view) {
        bindView(fragment, view);
        clickView(fragment, view);


    }


}
