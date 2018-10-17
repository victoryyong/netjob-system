package com.thsword.netjob.service;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.pojo.Role;

public interface UserService extends IBaseService{
List<Permission> queryUserMenus(String userId, boolean isAdmin);
	
	List<String> queryUserPermissions(String userId);
	
	List<Role> queryUserRoles(String userId);

	void addUserRole(String id,String userId, String roleId);

	void deleteUserRole(String userId);
	
	List<Role> queryUserRole(String userId);
	
	
	void addRolePermission(String id,String roleId, String permId);
	
	void deleteRolePermission(String roleId);

	List<Permission> queryRolePermission(String roleId);
	

	List<Permission> queryPageRoots(Map<String, Object> map);

	List<Permission> queryRoots(String level);
}
