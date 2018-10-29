package com.thsword.netjob.dao;

import java.util.List;

/**
 * 基础查询
 * 
 * @author yong 2014-4-11 22:23:07
 * 
 */
public interface IBaseDao {

	/**
	 * 添加
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int addEntity(Object obj) throws Exception;

	/**
	 * 删除
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int deleteEntity(Object obj) throws Exception;

	/**
	 * 删除通过ID
	 * 
	 * @param entityId
	 * @return
	 * @throws Exception
	 */
	public int deleteEntityById(final String entityId) throws Exception;

	/**
	 * 更新
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int updateEntity(Object obj) throws Exception;

	/**
	 * 查询对象
	 * 
	 * @param entityId
	 * @return
	 * @throws Exception
	 */
	public Object queryEntityById(final String entityId) throws Exception;

	
	/**
	 * 查询对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> queryAllEntity(Object object) throws Exception;
	
	/**
	 * 查询对象
	 * 
	 * @param entityId
	 * @return
	 * @throws Exception
	 */
	public Object queryEntity(final Object object) throws Exception;
	
	/**
	 * 查询对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer queryCountEntity(Object object) throws Exception;
	
	/**
	 * 查询对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> queryPageEntity(Object object) throws Exception;
	
}
