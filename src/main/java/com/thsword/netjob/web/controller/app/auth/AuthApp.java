package com.thsword.netjob.web.controller.app.auth;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IAuthCompanyDao;
import com.thsword.netjob.dao.IAuthPersonDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.AuthCompany;
import com.thsword.netjob.pojo.app.AuthPerson;
import com.thsword.netjob.service.AuthService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class AuthApp {
	@Resource(name = "authService")
	AuthService authService;

	/**
	 * 查询个人认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/visitor/auth/person/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId =  request.getParameter("memberId");
			AuthPerson person = new AuthPerson();
			person.setMemberId(memberId);
			List<AuthPerson> auths = (List<AuthPerson>) authService.queryAllEntity(IAuthPersonDao.class, person);
			JSONObject obj = new JSONObject();
			obj.put("list", auths);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 添加个人认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/auth/person/add")
	public void add(HttpServletRequest request, HttpServletResponse response,AuthPerson auth) throws Exception {
		try {
			String memberId = (String) request.getAttribute("memberId");
			String citycode = (String) request.getAttribute("citycode");
			if(StringUtils.isEmpty(auth.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "type不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "links不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getCode())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "code不能为空", response, request);
				return;
			}
			if(auth.getType()==Global.SYS_MEMBER_PERSON_AUTH_1){
				AuthPerson temp = new AuthPerson();
				temp.setMemberId(memberId);
				temp.setType(auth.getType());
				temp = (AuthPerson) authService.queryEntity(IAuthPersonDao.class, auth);
				if(temp!=null){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "身份证认证信息已存在", response, request);
					return;
				}
				auth.setIsPublic("0");
			}else{
				if(StringUtils.isEmpty(auth.getIsPublic())){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "isPublic不能为空", response, request);
					return;
				}
			}
			auth.setMemberId(memberId);
			auth.setCreateBy(memberId);
			auth.setUpdateBy(memberId);
			auth.setCitycode(citycode);
			auth.setId(UUIDUtil.get32UUID());
			authService.addEntity(IAuthPersonDao.class, auth);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 修改个人认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/auth/person/edit")
	public void edit(HttpServletRequest request, HttpServletResponse response,AuthPerson auth) throws Exception {
		try {
			if(StringUtils.isEmpty(auth.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "Id不能为空", response, request);
				return;
			}
			AuthPerson temp = (AuthPerson) authService.queryEntityById(IAuthPersonDao.class, auth.getId());
			if(null!=temp){
				if(temp.getStatus()==2){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "已审核通过，禁止修改", response, request);
					return;
				}else{
					temp.setLinks(auth.getLinks());
					temp.setStatus(Global.SYS_AUTH_STATUS_1);
					temp.setName(auth.getName());
					temp.setCode(auth.getCode());
					authService.updateEntity(IAuthPersonDao.class, temp);
				}
			}else{
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "认证信息不存在", response, request);
				return;
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 添加企业认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/auth/company/add")
	public void addCompany(HttpServletRequest request, HttpServletResponse response,AuthCompany auth) throws Exception {
		try {
			String memberId = (String) request.getAttribute("memberId");
			String citycode = (String) request.getAttribute("citycode");
			if(StringUtils.isEmpty(auth.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "许可证书不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "名称不能为空", response, request);
				return;
			}
			if(auth.getType()==Global.SYS_MEMBER_COMPANY_AUTH_1){
				 AuthPerson person = new AuthPerson();
				 person.setType(Global.SYS_MEMBER_PERSON_AUTH_1);
				 person.setMemberId(memberId);
				 person = (AuthPerson) authService.queryEntity(IAuthPersonDao.class,person);
				 if(null==person||person.getStatus()!=Global.SYS_AUTH_STATUS_2){
					 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请选完成个人身份证审核", response, request);
						return;
				 }
				 if(!person.getName().equals(auth.getName())){
					 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "企业审核与个人真实姓名不匹配", response, request);
					 return;
				 }
				 AuthCompany temp = new AuthCompany();
				 temp.setMemberId(memberId);
				 temp.setType(auth.getType());
				 temp = (AuthCompany) authService.queryEntity(IAuthCompanyDao.class, temp);
				 if(null!=temp){
					 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "授权书认证信息已存在", response, request);
					 return;
				 }
			}else{
				if(StringUtils.isEmpty(auth.getCode())){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "许件号码不能为空", response, request);
					return;
				}
			}
			auth.setMemberId(memberId);
			auth.setCreateBy(memberId);
			auth.setCitycode(citycode);
			auth.setUpdateBy(memberId);
			auth.setId(UUIDUtil.get32UUID());
			authService.addEntity(IAuthCompanyDao.class, auth);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 修改企业认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/auth/company/edit")
	public void editCompany(HttpServletRequest request, HttpServletResponse response,AuthCompany auth) throws Exception {
		try {
			String authId = request.getParameter("authId");
			AuthCompany temp = new AuthCompany();
			temp.setMemberId(authId);
			temp = (AuthCompany) authService.queryEntityById(IAuthCompanyDao.class, authId);
			if(null!=temp){
				authService.updateEntity(IAuthCompanyDao.class, temp);
			}else{
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "认证信息不存在", response, request);
				return;
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查询企业认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/auth/company/info")
	public void info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getParameter("memberId");
			AuthCompany auth = new AuthCompany();
			auth.setMemberId(memberId);
			@SuppressWarnings("unchecked")
			List<AuthCompany> auths = (List<AuthCompany>) authService.queryAllEntity(IAuthCompanyDao.class, auth);
			JSONObject obj = new JSONObject();
			obj.put("list", auths);
			JsonResponseUtil.successBodyResponse(auth,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
