package com.thsword.netjob.web.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IPermissionDao;
import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.service.UserService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.LogControllerAnnotation;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;


@Controller
public class PermissionAdmin {
	@Resource(name="userService")
	UserService userService;
	/**
	 * @throws Exception 
	 * 权限列表
	* @Title:  
	* @Description: 权限列表
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.permission.list")
	@RequestMapping("admin/permission/list")
	public void list(HttpServletRequest request,HttpServletResponse response,Page pageUtil) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			pageUtil.setSort("c_sort");
			pageUtil.setDir(Page.DIR_TYPE_ASC);
			map.put("page", pageUtil);
			List<Permission> permissions = userService.queryPageRoots(map);
			result.put("page", pageUtil);
			result.put("param", "");
			result.put("list", permissions);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 权限列表
	* @Title:  
	* @Description: 权限列表
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.permission.list")
	@RequestMapping("admin/permission/listAll")
	public void listAll(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String level = request.getParameter("level");
			JSONObject result  = new JSONObject();
			List<Permission> permissions = userService.queryRoots(level);
			result.put("list", permissions);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 新增权限
	* @Title: addRole 
	* @Description: 新增权限
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.permission.add")
	@RequestMapping("admin/permission/add")
	@LogControllerAnnotation(description="新增权限")
	public void add(HttpServletRequest request,HttpServletResponse response,Permission permission)throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			if(StringUtils.isEmpty(permission.getName())
					||StringUtils.isEmpty(permission.getStatus())
					||StringUtils.isEmpty(permission.getSort())
					||StringUtils.isEmpty(permission.getType())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			if(permission.getType()==2){
				if(StringUtils.isEmpty(permission.getUrl())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
				}
			}else if(permission.getType()==3){
				if(StringUtils.isEmpty(permission.getCode())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
				}
			}
			String name = permission.getName();
			Permission temp = new Permission();
			temp.setName(name);
			temp = (Permission) userService.queryEntity(IPermissionDao.class, temp);
			if (null!=temp) {
				JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_NAME_EXSIST, response,request);
				return;
			}
			/*if(!StringUtils.isEmpty(permission.getCode())){
				temp = new Permission();
				temp.setCode(permission.getCode());
				temp = (Permission) userService.queryEntity(IPermissionDao.class, temp);
				if (null!=temp) {
					JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_CODE_EXSIST, response,request);
					return;
				}
			}*/
			permission.setId(UUIDUtil.get32UUID());
			permission.setCreateBy(userId);
			permission.setCreateDate(new Date());
			userService.addEntity(IPermissionDao.class, permission);
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(permission));
			request.setAttribute("params", JSONObject.toJSONString(array));
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
	@AuthAnnotation(permissions="admin.permission.delete")
	@RequestMapping("admin/permission/delete")
	@LogControllerAnnotation(description="删除权限")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String permissionId = request.getParameter("permissionId");
			if(StringUtils.isEmpty(permissionId)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			Permission permission = (Permission) userService.queryEntityById(IPermissionDao.class, permissionId);
			if(null!=permission&&!CollectionUtils.isEmpty(permission.getChildren())){
				JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_HAS_CHILD, response,request);
				return;
			}
			userService.deleteEntityById(IPermissionDao.class, permissionId);
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(permission));
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 编辑权限
	* @Title: addRole 
	* @Description: 编辑权限
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.permission.edit")
	@RequestMapping("admin/permission/edit")
	@LogControllerAnnotation(description="编辑权限")
	public void edit(HttpServletRequest request,HttpServletResponse response,Permission permission)throws Exception{
		try {
			if(StringUtils.isEmpty(permission.getId())
					||StringUtils.isEmpty(permission.getName())
					||StringUtils.isEmpty(permission.getType())
					||StringUtils.isEmpty(permission.getStatus())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			if(permission.getType()==2){
				if(StringUtils.isEmpty(permission.getUrl())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
				}
			}else if(permission.getType()==3){
				if(StringUtils.isEmpty(permission.getCode())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
				}
			}
			String name = permission.getName();
			Permission temp = new Permission();
			temp.setName(name);
			temp = (Permission) userService.queryEntity(IPermissionDao.class, temp);
			if (null!=temp&&!temp.getId().equals(permission.getId())) {
				JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_NAME_EXSIST, response,request);
				return;
			}
			/*if(!StringUtils.isEmpty(permission.getCode())){
				temp = new Permission();
				temp.setCode(permission.getCode());
				temp = (Permission) userService.queryEntity(IPermissionDao.class, temp);
				if (null!=temp&&!temp.getId().equals(permission.getId())) {
					JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_CODE_EXSIST, response,request);
					return;
				}
			}*/
			userService.updateEntity(IPermissionDao.class, permission);
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(permission));
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
