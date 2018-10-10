package com.thsword.netjob.dao;

import java.util.List;

import com.thsword.netjob.pojo.Role;


/**
 * 
* @ClassName: IUserDao 
* @Description: TODO(用户dao) 
* @author yong
* @date 2017年5月14日 下午1:52:13 
*
 */
public interface IUserDao extends IBaseDao{
	/**
	 * 删除用户角色
	* @Title: deleteUserRole 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void deleteUserRole(String userId);
	/**
	 * 新增用户角色
	* @Title: addUserRole 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId
	* @param @param roleId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void addUserRole(String id,String userId, String roleId);
	/**
	 * 查询用户角色
	* @Title: queryUserRole 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId
	* @param @return    设定文件 
	* @return List<Role>    返回类型 
	* @throws
	 */
	List<Role> queryUserRole(String userId);
}
