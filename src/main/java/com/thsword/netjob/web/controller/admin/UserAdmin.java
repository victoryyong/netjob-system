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
import com.thsword.netjob.dao.ITokenDao;
import com.thsword.netjob.dao.IUserDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.pojo.Role;
import com.thsword.netjob.pojo.Token;
import com.thsword.netjob.pojo.User;
import com.thsword.netjob.service.RedisService;
import com.thsword.netjob.service.TokenService;
import com.thsword.netjob.service.UserService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.TokenUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.LogControllerAnnotation;
import com.thsword.utils.md5.Md5Util;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class UserAdmin {
	@Resource(name="userService")
	UserService userService;
	@Resource(name="tokenService")
	TokenService tokenService;
	@Resource(name="redisService")
	RedisService redisService;
	/**
	 * @throws Exception 
	 * 登录
	* @Title: login 
	* @Description: 用户登录
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/login")
	//@LogControllerAnnotation(description="用户登录")
	public void login(HttpServletRequest request,HttpServletResponse response,User user) throws Exception{
		try {
			String username = user.getName();
			String password = user.getPassword();
			if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			Global.getSetting("SECRET.APP.KEY");
			user.setPassword(Md5Util.getMd5Str(user.getPassword()));
			user = (User) userService.queryEntity(IUserDao.class, user);
			if(null==user){
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_NAME_PASSWORD_ERROR, response,request);
				return;
			}
			if(user.getStatus()==0){
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER_DISABLED, response,request);
				return;
			}
			request.setAttribute("userId", user.getId());
			request.setAttribute("username", user.getName());
			user.setLogins(user.getLogins()+1);
			userService.updateEntity(IUserDao.class, user);
			List<Permission> menus = userService.queryUserMenus(user.getId(),user.isAdmin());
			//用户角色
			List<Role> roles = userService.queryUserRole(user.getId());
			//缓存用户权限
			redisService.set("admin:userPermission:"+user.getId(),menus);
			redisService.expire("admin:userPermission:"+user.getId(),Global.USER_INFO_REDIS_TIMEOUT);
			//缓存用户角色
			redisService.set("admin:userRole:"+user.getId(),roles);
			redisService.expire("admin:userRole:"+user.getId(),Global.USER_INFO_REDIS_TIMEOUT);
			//缓存用户
			redisService.set("admin:user:"+user.getId(),user);
			redisService.expire("admin:user:"+user.getId(),Global.USER_INFO_REDIS_TIMEOUT);
			Token token = new Token();
			token.setUserId(user.getId());
			token.setUsername(username);
			token.setSubject(Global.JWT_SUBJECT_ADMIN);
			token = (Token) tokenService.queryEntity(ITokenDao.class, token);
			JSONObject obj = new JSONObject();
			if(null==token){
				token = TokenUtil.getToken(user.getId(),user.getName(),Global.JWT_SUBJECT_ADMIN,Global.getSetting(Global.JWT_SECRET_ADMIN_KEY));
				tokenService.addEntity(ITokenDao.class, token);
			}else{
				//后台改key后重新生成新的token
				if(!token.getSecretKey().equals(Global.getSetting(Global.JWT_SECRET_ADMIN_KEY))){
					tokenService.deleteEntityById(ITokenDao.class,token.getId());
					token = TokenUtil.getToken(user.getId(),user.getName(),Global.JWT_SUBJECT_ADMIN,Global.getSetting(Global.JWT_SECRET_ADMIN_KEY));
					tokenService.addEntity(ITokenDao.class, token);
				}else{
					//token已过期 生成新的token
					if(token.getSessionDate().before(new Date())){
						tokenService.deleteEntityById(ITokenDao.class,token.getId());
						token = TokenUtil.getToken(user.getId(),user.getName(),Global.JWT_SUBJECT_ADMIN,Global.getSetting(Global.JWT_SECRET_ADMIN_KEY));
						tokenService.addEntity(ITokenDao.class, token);
					}else{
						token.setSessionDate(new Date(new Date().getTime()+Global.JWT_ADMIN_SESSION_TIME_OUT));
						token.setExtendDate(new Date(new Date().getTime()+Global.JWT_EXTEND_TIME));
						tokenService.updateEntity(ITokenDao.class, token);
					}
				}
			}
			obj.put("token", token.getAccess_token());
			obj.put("user", user);
			obj.put("menus", menus);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * @throws Exception 
	 * 登录
	* @Title: login 
	* @Description: 用户登录
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/user/changePwd")
	@LogControllerAnnotation(description="修改密码")
	public void changePwd(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			String password = request.getParameter("password");
			String newPassword = request.getParameter("newPassword");
			String rePassword = request.getParameter("rePassword");
			if(StringUtils.isEmpty(password)||StringUtils.isEmpty(newPassword)||StringUtils.isEmpty(rePassword)||!newPassword.equals(rePassword)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response, request);
				return ;
			}
			User user = new User();
			user.setId(userId);
			user.setPassword(Md5Util.getMd5Str(password));
			user = (User) userService.queryEntity(IUserDao.class, user);
			if(null!=user){
				user.setPassword(Md5Util.getMd5Str(newPassword));
				userService.updateEntity(IUserDao.class, user);
				JsonResponseUtil.successCodeResponse(response, request);
			}else{
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_PWD,response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	/**
	 * @throws Exception 
	 * 登录
	* @Title: login 
	* @Description: 用户菜单
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/user/menus")
	public void menus(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			boolean isAdmin = (boolean) request.getAttribute("isAdmin");
			List<Permission> menus = userService.queryUserMenus(userId,isAdmin);
			JsonResponseUtil.successBodyResponse(menus, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 查询用户信息
	* @Title: login 
	* @Description: 查询用户信息
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/user/getUser")
	public void getUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			User user = (User) userService.queryEntityById(IUserDao.class, userId);
			JsonResponseUtil.successBodyResponse(user, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	/**
	 * 新增用户
	* @Title: add 
	* @Description: 新增用户
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@AuthAnnotation(permissions="admin.user.add")
	@RequestMapping("admin/user/add")
	@LogControllerAnnotation(description="新增用户")
	public void add(HttpServletRequest request,HttpServletResponse response,User user)throws Exception{
		try {
			String username = user.getName();
			String password = user.getPassword();
			String phone = user.getPhone();
			String email = user.getEmail();
			Integer status = user.getStatus();
			if(null==status||StringUtils.isEmpty(phone)||StringUtils.isEmpty(email)||StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			User tempUser = new User();
			tempUser.setName(username);
			List<User> users = (List<User>) userService.queryAllEntity(IUserDao.class, tempUser);
			if(!CollectionUtils.isEmpty(users)){
				JsonResponseUtil.codeResponse(ErrorUtil.REGISTER_NAME_EXSIST, response,request);
				return;
			}
			String id = UUIDUtil.get32UUID();
			user.setPassword(Md5Util.getMd5Str(password));
			user.setId(id);
			user.setCreateDate(new Date());
			user.setCreateBy(id);
			user.setLogins(0);
			user.setAdmin(false);
			userService.addEntity(IUserDao.class, user);
			user.setPassword("");
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(user));
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 编辑用户
	* @Title: add 
	* @Description: 新增用户
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.user.edit")
	@RequestMapping("admin/user/edit")
	@LogControllerAnnotation(description="编辑用户")
	public void edit(HttpServletRequest request,HttpServletResponse response,User user)throws Exception{
		try {
			String userId = user.getId();
			String username = user.getName();
			String phone = user.getPhone();
			String email = user.getEmail();
			Integer status = user.getStatus();
			if(StringUtils.isEmpty(userId)||null==status||StringUtils.isEmpty(phone)||StringUtils.isEmpty(email)||StringUtils.isEmpty(username)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			User tempUser = new User();
			tempUser.setName(user.getName());
			tempUser = (User) userService.queryEntity(IUserDao.class, tempUser);
			if(null!=tempUser&&!tempUser.getName().equals(username)){
				JsonResponseUtil.codeResponse(ErrorUtil.REGISTER_NAME_EXSIST, response,request);
				return;
			}
			user.setName(null);
			user.setPassword(null);
			userService.updateEntity(IUserDao.class, user);
			user.setName(username);
			JSONArray array = new JSONArray();
			array.add(JSONObject.toJSON(user));
			request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * @throws Exception 
	 * 列表
	* @Title: list
	* @Description: 用户列表 
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.user.list")
	@RequestMapping("admin/user/list")
	@SuppressWarnings("unchecked")
	public void list(HttpServletRequest request,HttpServletResponse response,Page pageUtil,User user) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) JSONObject.toJSON(user);
			map.put("page", pageUtil);
			List<User> users = (List<User>) userService.queryPageEntity(IUserDao.class,map);
			result.put("page", pageUtil);
			result.put("param", user);
			result.put("list", users);
			JsonResponseUtil.successBodyResponse(result, response, request);
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
	@AuthAnnotation(permissions="admin.user.authc")
	@RequestMapping("admin/user/authc")
	@LogControllerAnnotation(description="用户授权")
	public void authc(HttpServletRequest request,HttpServletResponse response){
		try {
			String userId = request.getParameter("userId");
			String roleIds = request.getParameter("roleIds");
			if(StringUtils.isEmpty(userId)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			userService.deleteUserRole(userId);
			if (!StringUtils.isEmpty(roleIds)) {
				String[] roleArray = roleIds.split(",");
				if(roleArray.length>0){
					for (String roleId : roleArray) {
						userService.addUserRole(UUIDUtil.get32UUID(),userId,roleId);
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
	 * 删除用户
	* @Title: list
	* @Description: 删除用户
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.user.delete")
	@RequestMapping("admin/user/delete")
	@LogControllerAnnotation(description="删除用户")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String userId = (String) request.getAttribute("userId");
			boolean isAdmin = (boolean) request.getAttribute("isAdmin");
			String userIds = request.getParameter("userIds");
			JSONArray array = new JSONArray();
			if(StringUtils.isEmpty(userId)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			if (!StringUtils.isEmpty(userIds)) {
				String[] userArray = userIds.split(",");
				User tempUser = null;
				if(userArray.length>1){
					for (String deleteId : userArray) {
						if(userId.equals(deleteId)||isAdmin){
							tempUser = (User) userService.queryEntityById(IUserDao.class, deleteId);
							if(StringUtils.isEmpty(tempUser.isAdmin())||!tempUser.isAdmin()){
								array.add(JSONObject.toJSON(tempUser));
								userService.deleteEntityById(IUserDao.class, deleteId);
								userService.deleteUserRole(deleteId);
							}
						}
					}
				}else if(userArray.length==1){
					tempUser = (User) userService.queryEntityById(IUserDao.class, userArray[0]);
					if(!StringUtils.isEmpty(tempUser.isAdmin())&&tempUser.isAdmin()){
						JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_NO_PERMISSION, response,request);
						return;
					}
				}
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
	@RequestMapping("admin/user/userRole")
	public void userRole(HttpServletRequest request,HttpServletResponse response){
		try {
			String userId = request.getParameter("userId");
			if(StringUtils.isEmpty(userId)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			List<Role> roles = userService.queryUserRole(userId);
			JsonResponseUtil.successBodyResponse(roles,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
