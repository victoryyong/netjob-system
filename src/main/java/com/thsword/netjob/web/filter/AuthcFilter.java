package com.thsword.netjob.web.filter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thsword.netjob.pojo.Permission;
import com.thsword.netjob.pojo.Role;
import com.thsword.netjob.pojo.User;
import com.thsword.netjob.service.RedisService;
import com.thsword.netjob.service.TokenService;
import com.thsword.netjob.service.UserService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.AuthAnnotation.ISADMIN;
import com.thsword.netjob.web.annotation.AuthAnnotation.PERMREL;
import com.thsword.netjob.web.annotation.AuthAnnotation.REL;
import com.thsword.netjob.web.annotation.AuthAnnotation.ROLEREL;

/***
 * 前段后台权限认证
 */
public class AuthcFilter extends HttpServlet implements HandlerInterceptor {
	private static final long serialVersionUID = 5836290769748648967L;
	@Resource(name = "tokenService")
	private TokenService tokenService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "redisService")
	private RedisService redisService;

	public String[] allowUrls;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj, ModelAndView modelView)
			throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean flag = false;
		String url = request.getRequestURI();
		String userId = request.getAttribute("userId") + "";
		if (null != allowUrls && allowUrls.length >= 1) {
			for (String allowUrl : allowUrls) {
				if (url.indexOf(allowUrl) >= 0) {
					return true;
				}
			}
		}
		String subject = request.getAttribute("subject") + "";
		// 权限认证
		HandlerMethod handlerMethod = null;
		try {
			handlerMethod = (HandlerMethod) handler;
		} catch (Exception e) {
			JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_URL,
					response,request);
			return false;
		}
		// 获取注解
		AuthAnnotation authAtn = handlerMethod
				.getMethodAnnotation(AuthAnnotation.class);
		if (null != authAtn) {
			User user = null;
			user = (User) redisService.getObject(subject + ":user:"
					+ userId);
			if (user == null) {
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_WITHOUT,
						response,request);
				return false;
			}
			if (user.isAdmin())
				return true;
			ISADMIN isAdmin = authAtn.isAdmin();
			if (isAdmin.equals(ISADMIN.YES)) {
				JsonResponseUtil
						.codeResponse(
								ErrorUtil.REQUEST_NO_SUPERMANAGER_PERMISSION,
								response,request);
				return false;
			}
			String permission = authAtn.permissions();
			String role = authAtn.roles();
			if (!StringUtils.isEmpty(permission)
					|| !StringUtils.isEmpty(role)) {
				boolean permFlag = false;
				if (!StringUtils.isEmpty(permission)) {
					PERMREL permRel = authAtn.permRel();
					List<String> userPerms = new ArrayList<String>();
					userPerms = (List<String>) redisService
							.getObject(subject + ":userPermission:" + userId);
					String[] permissions = permission.split(":");
					for (String pms : permissions) {
						// 满足一个权限即可
						if (permRel == PERMREL.OR) {
							if (contains(userPerms, pms)) {
								permFlag = true;
								break;
							}
							// 满足全部权限
						} else {
							boolean tempFlag = false;
							tempFlag = contains(userPerms, pms);
							if (!tempFlag) {
								permFlag = false;
								break;
							} else {
								permFlag = true;
							}
						}
					}
				}
				boolean roleFlag = false;
				if (!StringUtils.isEmpty(role)) {
					ROLEREL roleRel = authAtn.roleRel();
					List<Role> userRoles = null;
					userRoles = (List<Role>) redisService
							.getObject(subject + ":userRole:" + userId);
					List<String> userRolesString = new ArrayList<String>();
					for (Role userRole : userRoles) {
						userRolesString.add(userRole.getCode());
					}
					String[] roles = role.split(":");
					for (String rls : roles) {
						// 满足一个角色即可
						if (roleRel == ROLEREL.OR) {
							if (contains(userRolesString, rls)) {
								roleFlag = true;
								break;
							}
							// 满足全部角色
						} else {
							boolean tempFlag = false;
							tempFlag = contains(userRolesString, rls);
							if (!tempFlag) {
								roleFlag = false;
								break;
							} else {
								roleFlag = true;
							}
						}
					}
				}
				REL rel = authAtn.rel();

				if (rel == REL.OR) {
					flag = permFlag || roleFlag;
				} else {
					flag = permFlag && roleFlag;
				}
			}
			//无注解
		}else{
			flag = true;
		}
		if (!flag) {
			JsonResponseUtil.codeResponse(
					ErrorUtil.REQUEST_NO_PERMISSION, response,request);
		}
		return flag;
	}

	private boolean contains(List<String> list, String param) {
		for (int i = 0; i < list.size(); i++) {
			if (param.equals(list.get(i)))
				return true;
		}
		return false;
	}

	public String[] getAllowUrls() {
		return allowUrls;
	}

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}
	/*
	 * public static void main(String[] args) { String str =
	 * "appId=lzk20171017GIEKDA13DFED&accessKey=b5044b72d4dd4e95b43e2bd3fb67ff6e&timestamp=2017-10-18 14:53&courseId=84C51C5DE7A843F7A4B49E005B7FF028&reviewStatus=2&releaseStatus=1"
	 * ; System.out.println(SecurityUtils.getMd5(str)); }
	 */
}
