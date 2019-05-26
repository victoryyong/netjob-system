package com.thsword.netjob.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//注解会在class中存在，运行时可通过反射获取    
@Target({ElementType.METHOD,ElementType.FIELD})//目标是方法    
public @interface AccountCheckAnnotation {
	 public enum CHECKTYPE{ DEPOSIT,ACTIVE,BOTH};
	 
	 /**
     * 账户检查类型(DEPOSIT-保证金，ACTIVE-激活,BOTH-保证金和账户激活)
    * @Title: AuthAnnotation 
    * @Description: 
    * @param @return
    * @return 
    * @throws
     */
    public CHECKTYPE type() default CHECKTYPE.ACTIVE;
} 
