package com.thsword.netjob.web.controller.admin;

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
import com.thsword.netjob.dao.IAuthDao;
import com.thsword.netjob.dao.IAuthPersonDao;
import com.thsword.netjob.dao.IBrandDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Auth;
import com.thsword.netjob.pojo.app.AuthCompany;
import com.thsword.netjob.pojo.app.AuthPerson;
import com.thsword.netjob.pojo.app.Brand;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.IBaseService;
import com.thsword.netjob.service.ServeService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.LogControllerAnnotation;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;
/**
 * 审核管理

 * @Description审核

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class AuthAdmin {
	@Resource(name="baseService")
	IBaseService baseService;
	@Resource(name="serveService")
	ServeService serveService;
	@AuthAnnotation(permissions="admin.auth.list")
	@RequestMapping("admin/auth/list")
	public void authList(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			String type = request.getParameter("type");
			String name = request.getParameter("name");
			String provinceName = request.getParameter("provinceName");
			String cityName = request.getParameter("cityName");
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			map.put("type", type);
			map.put("citycode", citycode);
			map.put("name", name);
			map.put("provinceName", provinceName);
			map.put("cityName", cityName);
			map.put("status", Global.SYS_AUTH_STATUS_1);
			@SuppressWarnings("unchecked")
			List<Auth> auths = (List<Auth>) serveService.queryPageEntity(IAuthDao.class, map);
			result.put("page", page);
			result.put("list", auths);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.serve.auth.list")
	@RequestMapping("admin/serve/authList")
	public void serveAuthList(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			String type = request.getParameter("type");
			String name = request.getParameter("name");
			String provinceName = request.getParameter("provinceName");
			String cityName = request.getParameter("cityName");
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			map.put("type", type);
			map.put("name", name);
			map.put("citycode", citycode);
			map.put("provinceName", provinceName);
			map.put("cityName", cityName);
			map.put("status", Global.SYS_AUTH_STATUS_1);
			List<Serve> serves = serveService.queryPageServe(map);
			result.put("page", page);
			result.put("list", serves);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.serve.auth")
	@RequestMapping("admin/serve/auth")
	@LogControllerAnnotation(description="服务审核")
	public void add(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}else{
				citycode="1";
			}
			String userId = request.getAttribute("userId")+"";
			String username = request.getAttribute("username")+"";
			String authIds = request.getParameter("authIds");
			String type = request.getParameter("type");
			String content = request.getParameter("content");
			String status = request.getParameter("status");
			if(StringUtils.isEmpty(type)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(authIds)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核ID不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(content)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核意见不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(status)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核结果不能为空",response,request);
				return;
			}
			String[] idArray = authIds.split(",");
			for (String id : idArray) {
				Serve serve = (Serve) serveService.queryEntityById(IServeDao.class, id);
				if(null==serve){
					continue;
				}else{
					Serve temServe = new Serve();
					temServe.setId(id);
					temServe.setStatus(Integer.parseInt(status));
					serveService.updateEntity(IServeDao.class, temServe);
					Auth auth = new Auth();
					auth.setId(UUIDUtil.get32UUID());
					auth.setCreateBy(userId);
					auth.setName(serve.getTitle());
					auth.setUpdateBy(userId);
					auth.setContent(content);
					auth.setBizId(id);
					auth.setCitycode(citycode);
					auth.setUserId(userId);
					auth.setUserName(username);
					auth.setType(Integer.parseInt(type));
					auth.setStatus(Integer.parseInt(status));
					baseService.addEntity(IAuthDao.class, auth);
				}
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.brand.auth.list")
	@RequestMapping("admin/brand/authList")
	public void brandAuthList(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("t.c_createDate");
			map.put("page", page);
			map.put("citycode", citycode);
			map.put("status", Global.SYS_AUTH_STATUS_1);
			@SuppressWarnings("unchecked")
			List<Brand> serves = (List<Brand>) serveService.queryPageEntity(IBrandDao.class,map);
			result.put("page", page);
			result.put("list", serves);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.brand.auth")
	@RequestMapping("admin/brand/auth")
	@LogControllerAnnotation(description="品牌修审核")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}else{
				citycode = "1";
			}
			String userId = request.getAttribute("userId")+"";
			String username = request.getAttribute("userName")+"";
			String authIds = request.getParameter("authIds");
			String name = request.getParameter("name");
			String content = request.getParameter("content");
			String status = request.getParameter("status");
			if(StringUtils.isEmpty(name)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核名称不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(authIds)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核ID不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(content)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核意见不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(status)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核结果不能为空",response,request);
				return;
			}
			String[] idArray = authIds.split(",");
			for (String id : idArray) {
				Brand brand = (Brand) baseService.queryEntityById(IBrandDao.class, id);
				if(null==brand){
					continue;
				}else{
					Brand temBrand = new Brand();
					temBrand.setId(id);
					temBrand.setStatus(Integer.parseInt(status));
					baseService.updateEntity(IBrandDao.class, temBrand);
					Auth auth = new Auth();
					auth.setId(UUIDUtil.get32UUID());
					auth.setCreateBy(userId);
					auth.setName(brand.getTitle());
					auth.setUpdateBy(userId);
					auth.setContent(content);
					auth.setBizId(id);
					auth.setCitycode(citycode);
					auth.setUserId(userId);
					auth.setUserName(username);
					auth.setType(Global.SYS_AUTH_TYPE_3);
					auth.setStatus(Integer.parseInt(status));
					baseService.addEntity(IAuthDao.class, auth);
				}
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@AuthAnnotation(permissions="admin.person.auth.list")
	@RequestMapping("admin/person/authList")
	public void personAuthList(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			map.put("citycode", citycode);
			map.put("status", Global.SYS_AUTH_STATUS_1);
			@SuppressWarnings("unchecked")
			List<AuthPerson> persons = (List<AuthPerson>) serveService.queryPageEntity(IAuthPersonDao.class,map);
			result.put("page", page);
			result.put("list", persons);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.person.auth")
	@RequestMapping("admin/person/auth")
	@LogControllerAnnotation(description="身份认证审核")
	public void authPerson(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}else{
				citycode = "1";
			}
			String userId = request.getAttribute("userId")+"";
			String username = request.getAttribute("username")+"";
			String authIds = request.getParameter("authIds");
			String content = request.getParameter("content");
			String status = request.getParameter("status");
			if(StringUtils.isEmpty(authIds)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核ID不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(content)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核意见不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(status)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核结果不能为空",response,request);
				return;
			}
			String[] idArray = authIds.split(",");
			for (String id : idArray) {
				AuthPerson person = (AuthPerson) baseService.queryEntityById(IAuthPersonDao.class, id);
				if(null==person){
					continue;
				}else{
					if(null!=person&&Integer.parseInt(status)==Global.SYS_AUTH_STATUS_2){
						Member member = (Member) baseService.queryEntityById(IMemberDao.class, person.getMemberId());
						if(null!=member){
							Member temp = new Member();
							temp.setId(member.getId());
							temp.setRealName(person.getName());
							temp.setPersonAuth(true);
							baseService.updateEntity(IMemberDao.class, temp);
						}
					}
					AuthPerson temPerson = new AuthPerson();
					temPerson.setId(UUIDUtil.get32UUID());
					temPerson.setStatus(Integer.parseInt(status));
					baseService.updateEntity(IAuthPersonDao.class, temPerson);
					Auth auth = new Auth();
					auth.setId(UUIDUtil.get32UUID());
					auth.setCreateBy(userId);
					auth.setName(person.getName());
					auth.setUpdateBy(userId);
					auth.setContent(content);
					auth.setBizId(id);
					auth.setCitycode(citycode);
					auth.setUserId(userId);
					auth.setUserName(username);
					auth.setType(Global.SYS_AUTH_TYPE_4);
					auth.setStatus(Integer.parseInt(status));
					baseService.addEntity(IAuthDao.class, auth);
				}
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@AuthAnnotation(permissions="admin.com.auth.list")
	@RequestMapping("admin/company/authList")
	public void companyAuthList(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			map.put("citycode", citycode);
			map.put("status", Global.SYS_AUTH_STATUS_1);
			@SuppressWarnings("unchecked")
			List<AuthCompany> persons = (List<AuthCompany>) serveService.queryPageEntity(IAuthCompanyDao.class,map);
			result.put("page", page);
			result.put("list", persons);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.com.auth")
	@RequestMapping("admin/company/auth")
	@LogControllerAnnotation(description="企业认证审核")
	public void authCompany(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}else{
				citycode = "1";
			}
			String userId = request.getAttribute("userId")+"";
			String username = request.getAttribute("username")+"";
			String authIds = request.getParameter("authIds");
			String name = request.getParameter("name");
			String content = request.getParameter("content");
			String status = request.getParameter("status");
			if(StringUtils.isEmpty(name)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核名称不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(authIds)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核ID不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(content)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核意见不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(status)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "审核结果不能为空",response,request);
				return;
			}
			String[] idArray = authIds.split(",");
			for (String id : idArray) {
				AuthPerson person = (AuthPerson) baseService.queryEntityById(IAuthCompanyDao.class, id);
				if(null==person){
					continue;
				}else{
					if(null!=person&&person.getType()==Global.SYS_AUTH_STATUS_2){
						Member member = (Member) baseService.queryEntityById(IMemberDao.class, person.getMemberId());
						if(null!=member){
							Member temp = new Member();
							temp.setId(member.getId());
							temp.setRealName(person.getName());
							temp.setPersonAuth(true);
							baseService.updateEntity(IMemberDao.class, temp);
						}
					}
					AuthPerson temPerson = new AuthPerson();
					temPerson.setId(id);
					temPerson.setStatus(Integer.parseInt(status));
					baseService.updateEntity(IAuthCompanyDao.class, temPerson);
					Auth auth = new Auth();
					auth.setId(UUIDUtil.get32UUID());
					auth.setCreateBy(userId);
					auth.setName(person.getName());
					auth.setUpdateBy(userId);
					auth.setContent(content);
					auth.setBizId(id);
					auth.setCitycode(citycode);
					auth.setUserId(userId);
					auth.setUserName(username);
					auth.setType(Global.SYS_AUTH_TYPE_4);
					auth.setStatus(Integer.parseInt(status));
					baseService.addEntity(IAuthDao.class, auth);
				}
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
