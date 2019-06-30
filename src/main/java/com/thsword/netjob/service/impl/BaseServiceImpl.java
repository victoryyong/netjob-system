package com.thsword.netjob.service.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import com.thsword.netjob.service.IBaseService;
import com.thsword.netjob.web.exception.ServiceException;

/**
 * 基本接口
 *
 * @author yong
 */
@Service(value = "baseService")
public class BaseServiceImpl extends WebApplicationObjectSupport implements IBaseService {

    private static ApplicationContext context = null;

    /**
     * 添加
     *
     * @param clazz
     * @param obj
     * @return
     */
    public Object addEntity(Class<?> clazz, Object obj) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("addEntity",
                            Object.class);
                    return method.invoke(dao, obj);
                }
            }
        } catch (Exception e) {
            
        	throw new ServiceException("addEntity exception");
        }
        return null;
    }

    /**
     * 删除
     *
     * @param clazz
     * @param obj
     * @return
     */
    public boolean deleteEntity(Class<?> clazz, Object obj) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("deleteEntity",
                            Object.class);
                    method.invoke(dao, obj);
                    return true;
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("deleteEntity exception");
        }
        return false;
    }

    /**
     * 删除通过ID
     *
     * @param clazz
     * @param entityId
     * @return
     */
    public boolean deleteEntityById(Class<?> clazz, final String entityId) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod(
                            "deleteEntityById", String.class);
                    method.invoke(dao, entityId);
                    return true;
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("deleteEntityById exception");
        }
        return false;
    }

    /**
     * 更新
     *
     * @param clazz
     * @param obj
     * @return
     */
    public boolean updateEntity(Class<?> clazz, Object obj) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("updateEntity",
                            Object.class);
                    method.invoke(dao, obj);
                    return true;
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("updateEntity exception");
        }
        return false;
    }

    /**
     * 查询对象
     *
     * @param clazz
     * @param entityId
     * @return
     */
    public Object queryEntityById(Class<?> clazz, final String entityId) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("queryEntityById",
                    		String.class);
                    return method.invoke(dao, entityId);
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("queryEntityById exception");
        }
        return null;
    }

    /**
     * 查询对象
     *
     * @param clazz
     * @return
     */
    public List<?> queryAllEntity(Class<?> clazz,Object object) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("queryAllEntity",Object.class);
                    return (List<?>) method.invoke(dao,object);
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("queryAllEntity exception");
        }
        return null;
    }
    
    /**
     * 查询对象
     *
     * @param clazz
     * @return
     */
    public Object queryEntity(Class<?> clazz,Object object) throws RuntimeException{
    	try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("queryEntity",
                    		Object.class);
                    return method.invoke(dao, object);
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("queryEntity exception");
        }
        return null;
    }
    
    /**
     * 查询对象
     *
     * @param clazz
     * @return
     */
    public Integer queryCountEntity(Class<?> clazz,Object object) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("queryCountEntity",Object.class);
                    return (Integer) method.invoke(dao,object);
                }
            }
        } catch (Exception e) {
        	 
        	throw new ServiceException("queryCountEntity exception");
        }
        return null;
    }
    
    /**
     * 查询分页列表
     *
     * @param clazz
     * @return
     */
    public List<?> queryPageEntity(Class<?> clazz,Object object) throws RuntimeException{
        try {
            context = this.getApplicationContext();
            if (null != context) {
                Object dao = context.getBean(clazz);
                if (null != dao) {
                    Method method = dao.getClass().getMethod("queryPageEntity",Object.class);
                    return (List<?>) method.invoke(dao,object);
                }
            }
        } catch (Exception e) {
        	 
            throw new ServiceException("queryPageEntity exception");
        }
        return null;
    }
}
