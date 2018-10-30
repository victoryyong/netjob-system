package com.thsword.netjob.dao;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.Dict;

public interface IDictDao extends IBaseDao{
	/**
	 * 查询子字典
	* @Title: queryChilds 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param id
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Dict> queryChilds(String dictId);

	List<Dict> queryPageRoots(Map<String, Object> map);
}
