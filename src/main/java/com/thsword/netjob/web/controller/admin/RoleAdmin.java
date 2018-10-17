package com.thsword.netjob.web.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IRoleDao;
import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.pojo.Role;
import com.thsword.netjob.service.UserService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.LogControllerAnnotation;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class RoleAdmin {
	@Resource(name="userService")
	UserService userService;
	/**
	 * @throws Exception 
	 * 角色
	* @Title:  
	* @Description: 角色列表
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.role.list")
	@RequestMapping("admin/role/list")
	@SuppressWarnings("unchecked")
	public void list(HttpServletRequest request,HttpServletResponse response,Page pageUtil,Role role) throws Exception{
		try {
			String level = request.getParameter("level");
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) JSONObject.toJSON(role);
			map.put("page", pageUtil);
			map.put("level", level);
			List<Role> roles = (List<Role>) userService.queryPageEntity(IRoleDao.class,map);
			result.put("page", pageUtil);
			result.put("param", role);
			result.put("list", roles);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 角色
	* @Title:  
	* @Description: 角色列表
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.role.list")
	@RequestMapping("admin/role/listAll")
	@SuppressWarnings("unchecked")
	public void listAll(HttpServletRequest request,HttpServletResponse response,Role role) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) JSONObject.toJSON(role);
			List<Role> roles = (List<Role>) userService.queryAllEntity(IRoleDao.class,map);
			result.put("param", role);
			result.put("list", roles);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 新增角色
	* @Title: addRole 
	* @Description: 新增角色
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.role.add")
	@RequestMapping("admin/role/add")
	@LogControllerAnnotation(description="新增角色")
	public void add(HttpServletRequest request,HttpServletResponse response,Role role)throws Exception{
		try {
			String userId = (String) request.getAttribute("userId");
			if(StringUtils.isEmpty(role.getName())||StringUtils.isEmpty(role.getCode())||StringUtils.isEmpty(role.getStatus())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			Role tempRole =new Role();
			tempRole.setName(role.getName());
			tempRole = (Role) userService.queryEntity(IRoleDao.class, tempRole);
			if(null!=tempRole&&!StringUtils.isEmpty(tempRole.getName())){
				JsonResponseUtil.codeResponse(ErrorUtil.ROLE_NAME_EXSIST, response,request);
				return;
			}
			if(!StringUtils.isEmpty(role.getCode())){
				tempRole =new Role();
				tempRole.setCode(role.getCode());
				tempRole = (Role) userService.queryEntity(IRoleDao.class, tempRole);
				if(null!=tempRole&&!StringUtils.isEmpty(tempRole.getCode())){
					JsonResponseUtil.codeResponse(ErrorUtil.ROLE_CODE_EXSIST, response,request);
					return;
				}
			}
			role.setCreateBy(userId);
			role.setCreateDate(new Date());
			role.setId(UUIDUtil.get32UUID());
			userService.addEntity(IRoleDao.class, role);
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(role));
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 编辑角色
	* @Title: addRole 
	* @Description: 编辑角色
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.role.edit")
	@RequestMapping("admin/role/edit")
	@LogControllerAnnotation(description="编辑角色")
	public void edit(HttpServletRequest request,HttpServletResponse response,Role role)throws Exception{
		try {
			if(StringUtils.isEmpty(role.getId())||StringUtils.isEmpty(role.getName())||StringUtils.isEmpty(role.getCode())||StringUtils.isEmpty(role.getStatus())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			String roleName = role.getName();
			Role tempRole = new Role();
			tempRole.setName(roleName);
			tempRole = (Role) userService.queryEntity(IRoleDao.class, tempRole);
			if (null!=tempRole&&!tempRole.getId().equals(role.getId())) {
				JsonResponseUtil.codeResponse(ErrorUtil.ROLE_NAME_EXSIST, response,request);
				return;
			}
			tempRole = new Role();
			tempRole.setCode(role.getCode());
			tempRole = (Role) userService.queryEntity(IRoleDao.class, tempRole);
			if (null!=tempRole&&!tempRole.getId().equals(role.getId())) {
				JsonResponseUtil.codeResponse(ErrorUtil.ROLE_CODE_EXSIST, response,request);
				return;
			}
			userService.updateEntity(IRoleDao.class, role);
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(role));
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 角色授权
	* @Title: list
	* @Description: 用户列表 
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.role.authc")
	@RequestMapping("admin/role/authc")
	@LogControllerAnnotation(description="角色授权")
	public void authc(HttpServletRequest request,HttpServletResponse response){
		try {
			String roleId = request.getParameter("roleId");
			String permissionIds = request.getParameter("permissionIds");
			if(StringUtils.isEmpty(roleId)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			userService.deleteRolePermission(roleId);
			if (!StringUtils.isEmpty(permissionIds)) {
				String[] permArray = permissionIds.split(",");
				if(permArray.length>0){
					for (String permId : permArray) {
						userService.addRolePermission(UUIDUtil.get32UUID(),roleId,permId);
					}
				}
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 删除角色
	* @Title: list
	* @Description: 删除角色
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.role.delete")
	@RequestMapping("admin/role/delete")
	@LogControllerAnnotation(description="删除角色")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			JSONArray array = new JSONArray();
			String roleIds = request.getParameter("roleIds");
			if(StringUtils.isEmpty(roleIds)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			String[] roleArray = roleIds.split(",");
			Role tempRole = null;
			for (String roleId : roleArray) {
				tempRole = (Role) userService.queryEntityById(IRoleDao.class, roleId);
				userService.deleteEntityById(IRoleDao.class, roleId);
				userService.deleteRolePermission(roleId);
				array.add(JSONObject.toJSON(tempRole));
			}
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 获取用户角色
	* @Title: list
	* @Description: 获取用户角色
	* @param @param request
	* @param @param response
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/role/rolePermission")
	public void rolePermission(HttpServletRequest request,HttpServletResponse response){
		try {
			String roleId = request.getParameter("roleId");
			if(StringUtils.isEmpty(roleId)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			List<Permission> permissions = userService.queryRolePermission(roleId);
			JsonResponseUtil.successBodyResponse(permissions,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
