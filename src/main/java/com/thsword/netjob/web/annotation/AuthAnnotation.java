package com.thsword.netjob.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//注解会在class中存在，运行时可通过反射获取    
@Target({ElementType.METHOD,ElementType.FIELD})//目标是方法    
public @interface AuthAnnotation {
    public enum PERMREL{ OR,AND};
    public enum ROLEREL{ OR,AND};
    public enum REL{ OR,AND};
    public enum ISADMIN{ YES,NO};
    /**
     * 权限集合(格式admin.course.delete:admin.course.add)
    * @Title: AuthAnnotation 
    * @Description: 
    * @param @return
    * @return 
    * @throws
     */
    public String permissions() default "";
    /**
     * 角色集合(格式superManager:courseManager)
    * @Title: AuthAnnotation 
    * @Description: 
    * @param @return
    * @return 
    * @throws
     */
    public String roles() default "";
    /**
     * 权限关联关系&& || 
    * @ClassName: PERMREL 
    * @Description: 
    * @author LIXIAOYONG371
    * @date 2017年3月17日 下午2:42:00 
    *
     */
    PERMREL permRel() default PERMREL.OR;
    /**
     * 角色关联关系&& || 
    * @ClassName: PERMREL 
    * @Description: 
    * @author LIXIAOYONG371
    * @date 2017年3月17日 下午2:42:00 
    *
     */
    ROLEREL roleRel() default ROLEREL.OR;
    /**
     * 权限角色关联关系 && ||
    * @ClassName: PERMREL 
    * @Description: 
    * @author LIXIAOYONG371
    * @date 2017年3月17日 下午2:42:00 
    *
     */
    REL rel() default REL.OR;
    /**
     * 是否超管
    * @Title: AuthAnnotation 
    * @Description: 
    * @param @return
    * @return 
    * @throws
     */
    ISADMIN isAdmin() default ISADMIN.NO;
} 
