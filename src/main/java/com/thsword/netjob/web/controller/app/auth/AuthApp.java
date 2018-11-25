package com.thsword.netjob.web.controller.app.auth;

import java.io.InputStream;
import java.util.Calendar;
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
import com.thsword.netjob.dao.IAuthCompanyDao;
import com.thsword.netjob.dao.IAuthDao;
import com.thsword.netjob.dao.IAuthPersonDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Auth;
import com.thsword.netjob.pojo.app.AuthCompany;
import com.thsword.netjob.pojo.app.AuthPerson;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.AuthService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.httpclient.HttpClientUtils;
import com.thsword.netjob.util.juhe.AuthType;
import com.thsword.utils.date.DateUtil;
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
			String hostId =  request.getAttribute("memberId")+"";
			AuthPerson person = new AuthPerson();
			person.setMemberId(memberId);
			if(hostId.equals(memberId)){
				person.setIsPublic("");
			}else{
				person.setIsPublic("1");
			}
			if(StringUtils.isEmpty(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "memberId不能为空", response, request);
				return;
			}
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
			String memberName = (String) request.getAttribute("memberName");
			String citycode = (String) request.getAttribute("citycode");
			auth.setId(UUIDUtil.get32UUID());
			if(StringUtils.isEmpty(auth.getName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "name不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getRealName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "realName不能为空", response, request);
				return;
			}
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
				temp = (AuthPerson) authService.queryEntity(IAuthPersonDao.class, temp);
				if(temp!=null){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "身份证认证信息已存在", response, request);
					return;
				}
				
				//聚合认证
				JSONArray links = JSONArray.parseArray(auth.getLinks());
				InputStream inputStream = null;
				try {
					inputStream =  HttpClientUtils.getInputStream(links.get(0).toString());
				} catch (Exception e) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证件识别失败", response, request);
					return;
				}
				
				String appKey = Global.getSetting(Global.JUHE_ACCESS_PERSONCARD_KEY);
				Map<String, String> param = new HashMap<String, String>();
				param.put("key", appKey);
				param.put("cardType", AuthType.PersonTwoFace.getCode());
				String jsonString = "";
				try {
					jsonString = HttpClientUtils.post(Global.JUHE_PERSONCARD_URL, param, null, "pic", inputStream);
				} catch (Exception e) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证件识别失败", response, request);
					return;
				}
				JSONObject cardInfo = JSONObject.parseObject(jsonString);
				//审核记录原因
				String content = "";
				int status = 0;
				if(!cardInfo.getString("error_code").equals("0")){
					content = cardInfo.getString("reason");
					status = Global.SYS_AUTH_STATUS_3;
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, cardInfo.getString("reason"), response, request);
					return;
				}else{
					if(!cardInfo.getJSONObject("result").getString("姓名").equals(auth.getRealName())){
						content = "身份证名称与输入姓名不匹配";
						status = Global.SYS_AUTH_STATUS_3;
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"身份证名称与输入姓名不匹配", response, request);
						return;
					}else if(!cardInfo.getJSONObject("result").getString("公民身份号码").equals(auth.getCode())){
						content = "身份证号码与输入号码不匹配";
						status = Global.SYS_AUTH_STATUS_3;
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"身份证号码与输入号码不匹配", response, request);
						return;
					}else{
						status=Global.SYS_AUTH_STATUS_2;
						content="后台自动认证通过";
						//更新个人信息
						Date birth = DateUtil.getDate(cardInfo.getJSONObject("result").getString("出生"),DateUtil.FORMAT_STYLE_2);
						Calendar cal = Calendar.getInstance();
						cal.setTime(birth);
				        int year = cal.get(Calendar.YEAR);//获取年份
				        int month=cal.get(Calendar.MONTH)+1;//获取月份
				        int day=cal.get(Calendar.DATE);//获取日
						int age = DateUtil.getAge(year,month,day);
						String gender = cardInfo.getJSONObject("result").getString("性别");
						gender=gender.equals("男")?"1":"0";
						Member member = (Member) authService.queryEntityById(IMemberDao.class, memberId);
						Member memberDb = new Member();
						memberDb.setId(member.getId());
						memberDb.setGender(Integer.parseInt(gender));
						memberDb.setAge(age);
						memberDb.setPersonAuth(true);
						memberDb.setRealName(auth.getRealName());
						authService.updateEntity(IMemberDao.class, memberDb);
						
						auth.setStatus(Global.SYS_AUTH_STATUS_2);
						
					}
				}
				//审核记录
				Auth authRecord = new Auth();
				authRecord.setId(UUIDUtil.get32UUID());
				authRecord.setCreateBy(memberId);
				authRecord.setName(memberName);
				authRecord.setUpdateBy(memberId);
				authRecord.setContent(content);
				authRecord.setBizId(auth.getId());
				authRecord.setCitycode(citycode);
				authRecord.setUserId(memberId);
				authRecord.setUserName(auth.getRealName());
				authRecord.setType(Global.SYS_AUTH_TYPE_4);
				authRecord.setStatus(status);
				authService.addEntity(IAuthDao.class, authRecord);
				
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
					temp.setIsPublic(auth.getIsPublic());
					if(auth.getType()==Global.SYS_MEMBER_PERSON_AUTH_1){
						temp.setIsPublic("0");
					}
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
			String memberName = (String) request.getAttribute("memberName");
			if(StringUtils.isEmpty(auth.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证书不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证书名不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(auth.getRealName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "真实姓名不能为空", response, request);
				return;
			}
			if(auth.getType()!=Global.SYS_MEMBER_COMPANY_AUTH_2){
				if(StringUtils.isEmpty(auth.getCode())){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证件号码不能为空", response, request);
					return;
				}
			}
			 AuthPerson person = new AuthPerson();
			 person.setType(Global.SYS_MEMBER_PERSON_AUTH_1);
			 person.setMemberId(memberId);
			 person = (AuthPerson) authService.queryEntity(IAuthPersonDao.class,person);
			 if(null==person||person.getStatus()!=Global.SYS_AUTH_STATUS_2){
				 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请先完成个人身份证审核", response, request);
					return;
			 }
			 if(Global.SYS_MEMBER_COMPANY_AUTH_1==auth.getType()||Global.SYS_MEMBER_COMPANY_AUTH_2==auth.getType()){
				 if(!person.getRealName().equals(auth.getRealName())){
					 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "企业审核名与个人真实姓名不匹配", response, request);
					 return;
				 }
			 }
			 if(Global.SYS_MEMBER_COMPANY_AUTH_1==auth.getType()){
				 AuthCompany temp = new AuthCompany();
				 temp.setMemberId(memberId);
				 temp.setType(auth.getType());
				 temp = (AuthCompany) authService.queryEntity(IAuthCompanyDao.class, temp);
				 if(null!=temp){
					 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "营业执照认证信息已存在", response, request);
					 return;
				 }
			 }
			 if(Global.SYS_MEMBER_COMPANY_AUTH_2==auth.getType()){
				 AuthCompany temp = new AuthCompany();
				 temp.setMemberId(memberId);
				 temp.setType(auth.getType());
				 temp = (AuthCompany) authService.queryEntity(IAuthCompanyDao.class, temp);
				 if(null!=temp){
					 JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "授权书认证信息已存在", response, request);
					 return;
				 }
			 }
			 
			//聚合认证
			JSONArray links = JSONArray.parseArray(auth.getLinks());
			InputStream inputStream = null;
			try {
				inputStream =  HttpClientUtils.getInputStream(links.get(0).toString());
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证件识别失败", response, request);
				return;
			}
			
			String appKey = Global.getSetting(Global.JUHE_ACCESS_PERSONCARD_KEY);
			Map<String, String> param = new HashMap<String, String>();
			param.put("key", appKey);
			param.put("cardType", AuthType.BusinessLicence.getCode());
			String jsonString = "";
			try {
				jsonString = HttpClientUtils.post(Global.JUHE_PERSONCARD_URL, param, null, "pic", inputStream);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "证件识别失败", response, request);
				return;
			}
			JSONObject cardInfo = JSONObject.parseObject(jsonString);
			String content = "";
			int status = 0;
			if(!cardInfo.getString("error_code").equals("0")){
				content = cardInfo.getString("reason");
				status = Global.SYS_AUTH_STATUS_3;
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, cardInfo.getString("reason"), response, request);
				return;
			}else{
				if(!cardInfo.getJSONObject("result").getString("法定代表人").equals(auth.getRealName())){
					content = "法定代表人与输入姓名不匹配";
					status = Global.SYS_AUTH_STATUS_3;
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"法定代表人与输入姓名不匹配", response, request);
					return;
				}else if(!cardInfo.getJSONObject("result").getString("统一社会信用代码").equals(auth.getCode())){
					content = "统一社会信用代码与输入号码不匹配";
					status = Global.SYS_AUTH_STATUS_3;
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"统一社会信用代码与输入号码不匹配", response, request);
					return;
				}else{
					auth.setMemberId(memberId);
					auth.setCreateBy(memberId);
					auth.setCitycode(citycode);
					auth.setUpdateBy(memberId);
					auth.setId(UUIDUtil.get32UUID());
					authService.addEntity(IAuthCompanyDao.class, auth);
					
					status=Global.SYS_AUTH_STATUS_2;
					content="后台自动认证通过";
					
					//审核记录
					Auth authRecord = new Auth();
					authRecord.setId(UUIDUtil.get32UUID());
					authRecord.setCreateBy(memberId);
					authRecord.setName(memberName);
					authRecord.setUpdateBy(memberId);
					authRecord.setContent(content);
					authRecord.setBizId(auth.getId());
					authRecord.setCitycode(citycode);
					authRecord.setUserId(memberId);
					authRecord.setUserName(auth.getRealName());
					authRecord.setType(Global.SYS_AUTH_TYPE_5);
					authRecord.setStatus(status);
					authService.addEntity(IAuthDao.class, authRecord);
				}
			}
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
			if(StringUtils.isEmpty(auth.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空", response, request);
				return;
			}
			AuthCompany temp = (AuthCompany) authService.queryEntityById(IAuthCompanyDao.class, auth.getId());
			if(null!=temp){
				if(temp.getStatus()==Global.SYS_AUTH_STATUS_2){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "已审核通过，禁止修改", response, request);
					return;
				}else{
					temp.setLinks(auth.getLinks());
					temp.setStatus(Global.SYS_AUTH_STATUS_1);
					temp.setName(auth.getName());
					temp.setCode(auth.getCode());
					authService.updateEntity(IAuthCompanyDao.class, temp);
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
	 * 查询企业认证信息
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/auth/company/list")
	public void info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId =  request.getParameter("memberId");
			if(StringUtils.isEmpty(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "memberId不能为空", response, request);
				return;
			}
			AuthCompany auth = new AuthCompany();
			auth.setMemberId(memberId);
			@SuppressWarnings("unchecked")
			List<AuthCompany> auths = (List<AuthCompany>) authService.queryAllEntity(IAuthCompanyDao.class, auth);
			JSONObject obj = new JSONObject();
			obj.put("list", auths);
			JsonResponseUtil.successBodyResponse(auths,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分           
        int second=cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        System.out.println("现在的时间是：公元"+year+"年"+month+"月"+day+"日      "+hour+"时"+minute+"分"+second+"秒       星期"+WeekOfYear);
	}
}
