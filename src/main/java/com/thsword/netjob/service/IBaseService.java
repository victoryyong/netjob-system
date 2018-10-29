package com.thsword.netjob.service;

import java.util.List;

/**
 * 
* @ClassName: IBaseService 
* @Description: TODO(基本接口) 
* @author yong
* @date 2017年5月13日 下午3:59:50 
*
 */
public interface IBaseService {

	/**
	 * 添加
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 */
	public Object addEntity(Class<?> clazz, Object obj) throws Exception;

	/**
	 * 删除
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 */
	public boolean deleteEntity(Class<?> clazz, Object obj) throws Exception;

	/**
	 * 删除通过ID
	 * 
	 * @param clazz
	 * @param entityId
	 * @return
	 */
	public boolean deleteEntityById(Class<?> clazz, final String entityId) throws Exception;

	/**
	 * 更新
	 * 
	 * @param clazz
	 * @param obj
	 * @return
	 */
	public boolean updateEntity(Class<?> clazz, Object obj) throws Exception;

	/**
	 * 查询对象
	 * 
	 * @param clazz
	 * @param string
	 * @return
	 */
	public Object queryEntityById(Class<?> clazz, final String string) throws Exception;

	
	/**
	 * 查询对象
	 * 
	 * @param clazz
	 * @return
	 */
	public List<?> queryAllEntity(Class<?> clazz,Object object) throws Exception;
	
	/**
	 * 查询对象
	 * 
	 * @param clazz
	 * @return
	 */
	public Object queryEntity(Class<?> clazz,Object object) throws Exception;
	
	/**
	 * 查询分页列表
	 * 
	 * @param clazz
	 * @return
	 */
	public List<?> queryPageEntity(Class<?> clazz, Object object) throws Exception;
	
	/**
	 * 查询计数
	 * 
	 * @param clazz
	 * @return
	 */
	public Integer queryCountEntity(Class<?> clazz, Object object) throws Exception;

}
