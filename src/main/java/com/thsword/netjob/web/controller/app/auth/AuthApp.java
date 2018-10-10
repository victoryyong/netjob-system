package com.thsword.netjob.web.controller.app.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IAuthCompanyDao;
import com.thsword.netjob.dao.IAuthPersonDao;
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
			Map<String, Object> map = new HashMap<>();
			map.put("memberId", memberId);
			List<AuthPerson> auths = (List<AuthPerson>) authService.queryAllEntity(IAuthPersonDao.class, map);
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
			if(StringUtils.isEmpty(auth.getCode())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "code不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "type不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getIsPublic())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "isPublic不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "links不能为空", response, request);
				return;
			}
			AuthPerson temp = new AuthPerson();
			temp.setMemberId(memberId);
			temp.setType(auth.getType());
			temp = (AuthPerson) authService.queryEntity(IAuthPersonDao.class, auth);
			if(temp!=null){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "认证信息已存在", response, request);
				return;
			}
			auth.setMemberId(memberId);
			auth.setCreateBy(memberId);
			auth.setUpdateBy(memberId);
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
			String memberId = (String) request.getAttribute("memberId");
			if(StringUtils.isEmpty(auth.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "type不能为空", response, request);
				return;
			}
			AuthPerson temp = new AuthPerson();
			temp.setMemberId(memberId);
			temp.setType(auth.getType());
			temp = (AuthPerson) authService.queryEntity(IAuthPersonDao.class, auth);
			if(null!=temp){
				temp.setLinks(auth.getLinks());
				temp.setIsPublic(auth.getIsPublic());
				temp.setCode(auth.getCode());
				authService.updateEntity(IAuthPersonDao.class, temp);
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
			if(StringUtils.isEmpty(auth.getDeputes())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "授权委托书不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getTradeCode())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "营业执照号不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getTradeLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "营业证件不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getAllowCode())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "许可证书号不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getAllowLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "许可证书不能为空", response, request);
				return;
			}
			AuthCompany temp = new AuthCompany();
			temp.setMemberId(memberId);
			temp = (AuthCompany) authService.queryEntity(IAuthCompanyDao.class, temp);
			if(null!=temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "认证信息已存在", response, request);
				return;
			}
			auth.setMemberId(memberId);
			auth.setCreateBy(memberId);
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
			String memberId = (String) request.getAttribute("memberId");
			AuthCompany temp = new AuthCompany();
			temp.setMemberId(memberId);
			temp = (AuthCompany) authService.queryEntity(IAuthCompanyDao.class, auth);
			if(null!=temp){
				temp.setDeputes(auth.getDeputes());
				temp.setAllowCode(auth.getAllowCode());
				temp.setAllowLinks(auth.getAllowLinks());
				temp.setTradeCode(auth.getTradeCode());
				temp.setTradeLinks(auth.getTradeLinks());
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
			AuthCompany auth = (AuthCompany) authService.queryEntityById(IAuthCompanyDao.class, memberId);
			if(null==auth){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "认证信息不存在", response, request);
				return;
			}
			JsonResponseUtil.successBodyResponse(auth,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
