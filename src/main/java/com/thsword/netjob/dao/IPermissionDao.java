package com.thsword.netjob.dao;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.pojo.Role;


/**
 * 
* @ClassName: IPermissionDao 
* @Description: TODO(权限dao) 
* @author yong
* @date 2017年5月14日 下午1:52:13 
*
 */
public interface IPermissionDao extends IBaseDao{
	/**
	 * 根据userId查询角色
	* @Title: queryUserRoles 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Role> queryUserRoles(String userId);
	/**
	 * 根据角色查询权限
	* @Title: queryRolePermissions 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param id
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Permission> queryRolePermissions(String roleId);
	
	/**
	 * 查询子权限
	* @Title: queryChilds 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param id
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Permission> queryChilds(String permissionId);
	
	/**
	 * 分页查询根权限
	* @Title: queryRoots 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Permission> queryPageRoots(Map<String, Object> map);
	/**
	 * 查询跟权限
	* @Title: queryRoots 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return List<Permission>    返回类型 
	* @throws
	 */
	List<Permission> queryRoots();
}
