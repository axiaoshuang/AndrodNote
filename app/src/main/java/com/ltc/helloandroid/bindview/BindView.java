package com.ltc.helloandroid.bindview;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by litiancheng on 2016/12/29.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    int value() default 0;
}
