package com.thsword.netjob.web.swagger.model;

/**
 * Created by yong on 2018/9/7.
 */



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thsword.netjob.web.swagger.CommonData;


@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonResult {

    String[] value();

    String name() default "";

    String type() default CommonData.RESULT_TYPE_NORMAL_FINAL;


}