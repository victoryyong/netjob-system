package com.thsword.netjob.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.thsword.netjob.dao.IPermissionDao;
import com.thsword.netjob.dao.IRoleDao;
import com.thsword.netjob.dao.IUserDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.pojo.Role;
import com.thsword.netjob.service.UserService;

/**
 * 
* @ClassName: UserServiceImpl 
* @Description: TODO(用户service) 
* @author yong
* @date 2017年5月14日 下午2:26:08 
*
 */
@Service(value = "userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService{
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IPermissionDao permissionDao;
	@Override
	public List<Role> queryUserRoles(String userId) {
		 return userDao.queryUserRole(userId);
	}
	/**
	 * 查询用户菜单
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> queryUserMenus(String userId,boolean isAdmin) {
		//用户角色
		List<Permission> userMenu1 = new ArrayList<Permission>();
		List<Permission> userMenu2 = new ArrayList<Permission>();
		List<Permission> userOprt = new ArrayList<Permission>();
		try {
			List<Permission> allPermissions = new ArrayList<Permission>();
			if(isAdmin){
				allPermissions = (List<Permission>) permissionDao.queryAllEntity(new Permission());
			}else{
				List<Role> roles = userDao.queryUserRole(userId);
				if(!CollectionUtils.isEmpty(roles)){
					for (Role role : roles) {
						List<Permission> rolePermissions = permissionDao.queryRolePermissions(role.getId());
						if(!StringUtils.isEmpty(rolePermissions)){
							for (Permission rolePerm : rolePermissions) {
								allPermissions.add(rolePerm);
							}
						}
					}
				}
			}
			//该角色下的所有权限
				
			if(!CollectionUtils.isEmpty(allPermissions)){
				List<Permission> menu1 = new ArrayList<Permission>();
				List<Permission> menu2 = new ArrayList<Permission>();
				List<Permission> oprt = new ArrayList<Permission>();
				Permission temp = null;
				for (Permission permission : allPermissions) {
				 if(permission.getType().equals(Global.SYS_PERMISSION_TYPE_MENU1)){
					 if(!menu1.contains(permission))
					  menu1.add(permission);
				  }	
					if (permission.getType().equals(
							Global.SYS_PERMISSION_TYPE_MENU2)) {
						if(!menu2.contains(permission))
						menu2.add(permission);
					}
					if(permission.getType().equals(Global.SYS_PERMISSION_TYPE_OPRT)){
						oprt.add(permission);
						if(!StringUtils.isEmpty(permission.getParentId())){
							temp = (Permission) permissionDao.queryEntityById(permission.getParentId());
							if(null!=temp){
								if (!menu2.contains(temp)&&temp.getType().equals(Global.SYS_PERMISSION_TYPE_MENU2)) {
									menu2.add(temp);
									temp = (Permission) permissionDao.queryEntityById(temp.getParentId());
									if(null!=temp){
										if(!menu1.contains(temp)){
											menu1.add(temp);
										}
									}
								}else if(!menu1.contains(temp)&&temp.getType().equals(Global.SYS_PERMISSION_TYPE_MENU1)){
									menu1.add(temp);
								};
							}
						}
						
					}
				}
				//二级菜单下的 操作权限
				if(!CollectionUtils.isEmpty(menu2)){
					for (Permission parent : menu2) {
						userOprt = new ArrayList<Permission>();
						for (Permission child : oprt) {
							if (child.getParentId().equals(parent.getId())) {
								userOprt.add(child);
							}
						}
						parent.setChildren(userOprt);
						userMenu2.add(parent);
					}
				}
				//一级菜单下的 操作权限
				if(!CollectionUtils.isEmpty(menu1)){
					userOprt = null;
					for (Permission parent : menu1) {
						userOprt = new ArrayList<Permission>();
						for (Permission child : oprt) {
							if (child.getParentId().equals(parent.getId())) {
								userOprt.add(child);
							}
						}
						parent.setChildren(userOprt);
						userMenu1.add(parent);
					}
				}
				
				//一级菜单下的二级菜单
				if(!CollectionUtils.isEmpty(userMenu1)){
					for (Permission parent : userMenu1) {
						for (Permission child : userMenu2) {
							if(!StringUtils.isEmpty(child.getParentId())&&child.getParentId().equals(parent.getId())){
								List<Permission> childs = parent.getChildren();
								if(CollectionUtils.isEmpty(childs)){
									childs = new ArrayList<Permission>();
								}
								childs.add(child);
								parent.setChildren(childs);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!CollectionUtils.isEmpty(userMenu1)){
			for (Permission permission : userMenu1) {
				if(!CollectionUtils.isEmpty(permission.getChildren())){
					Collections.sort(permission.getChildren(),new Comparator<Permission>() {
						@Override
						public int compare(Permission o1, Permission o2) {
							return o1.getSort()-o2.getSort();
						}
					});
				}
			}
			Collections.sort(userMenu1,new Comparator<Permission>() {
				@Override
				public int compare(Permission o1, Permission o2) {
					return o1.getSort()-o2.getSort();
				}
			});
		}
		return userMenu1;
	}
	
	
	/**
	 * 查询用户菜单
	 */
	@Override
	public List<String> queryUserPermissions(String userId) {
		//用户角色
		List<Role> roles = userDao.queryUserRole(userId);
		//该角色下的所有权限
		List<String> allPermissions = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(roles)){
			for (Role role : roles) {
				List<Permission> rolePermissions = permissionDao.queryRolePermissions(role.getId());
				if(!StringUtils.isEmpty(rolePermissions)){
					for (Permission rolePerm : rolePermissions) {
						allPermissions.add(rolePerm.getCode());
					}
				}
			}
		}
		return allPermissions;
	}
	
	/**
	 * 新增用户角色
	 */
	@Override
	public void addUserRole(String id,String userId, String roleId) {
		userDao.addUserRole(id,userId,roleId);
	}
	
	/**
	 * 删除用户角色
	 */
	@Override
	public void deleteUserRole(String userId) {
		userDao.deleteUserRole(userId);
	}


	@Override
	public List<Role> queryUserRole(String userId) {
		return userDao.queryUserRole(userId);
	}
	
	/**
	 * 新增角色权限
	 */
	@Override
	public void addRolePermission(String id,String roleId, String permId) {
		roleDao.addRolePermission(id,roleId,permId);
	}
	/**
	 * 删除角色权限
	 */
	@Override
	public void deleteRolePermission(String roleId) {
		roleDao.deleteRolePermission(roleId);
	}
	@Override
	public List<Permission> queryRolePermission(String roleId) {
		return roleDao.queryRolePermission(roleId);
	}
	

	/**
	 * 新增分页一级菜单权限
	 */
	@Override
	public List<Permission> queryPageRoots(Map<String, Object> map) {
		return permissionDao.queryPageRoots(map);
	}
	@Override
	public List<Permission> queryRoots(String level) {
		return permissionDao.queryRoots(level);
	}
}
