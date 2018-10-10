package com.thsword.netjob.dao;

import java.util.List;

import com.thsword.netjob.pojo.Permission;


/**
 * 
* @ClassName: IRoleDao 
* @Description: TODO(角色dao) 
* @author yong
* @date 2017年5月14日 下午1:52:13 
*
 */
public interface IRoleDao extends IBaseDao{
	/**
	 * 删除角色权限
	* @Title: deleteRolePermission 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param roleId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void deleteRolePermission(String roleId);
	/**
	 * 新增角色权限
	* @Title: addRolePermission 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param id
	* @param @param roleId
	* @param @param permId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void addRolePermission(String id,String roleId, String permissionId);
	/**
	 * 
	* @Title: queryRolePermission 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param roleId
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Permission> queryRolePermission(String roleId);
}
