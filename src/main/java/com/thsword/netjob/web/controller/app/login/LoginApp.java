package com.thsword.netjob.web.controller.app.login;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.dao.ITokenDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Token;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.service.TokenService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JmessageUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.TokenUtil;
import com.thsword.utils.md5.Md5Util;
import com.thsword.utils.object.UUIDUtil;

@Controller
public class LoginApp {
	@Resource(name = "memberService")
	MemberService memberService;
	@Resource(name = "tokenService")
	TokenService tokenService;

	/**
	 * 
	
	 * @Description:登陆
	
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/login")
	public void list(HttpServletRequest request, HttpServletResponse response, Member member) throws Exception {
		try {
			String password = member.getPassword();
			String type = request.getParameter("type");
			if (StringUtils.isEmpty(type)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "登陆类型不能为空", response, request);
				return;
			}
			Member temp = new Member();
			if (type.equals(Global.SYS_MEMBER_TYPE_PHONE)) {
				if (StringUtils.isEmpty(member.getPhone())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账号不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getPassword())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
					return;
				}
				temp.setPassword(member.getPassword());
				temp.setPhone(member.getPhone());
				member = (Member) memberService.queryEntity(IMemberDao.class, temp);
				if(null==member){
					JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_NAME_PASSWORD_ERROR, response, request);
					return;
				}
				if(member.getStatus()==Global.SYS_MEMBER_STATUS_2){
					JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER_DISABLED, response, request);
					return;
				}
			} else if (type.equals(Global.SYS_MEMBER_TYPE_QQ)) {
				/*if (StringUtils.isEmpty(member.getCitycode())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "citycode不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getCityName())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "cityName不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getProvinceName())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "provinceName不能为空", response, request);
					return;
				}*/
				/*if (StringUtils.isEmpty(member.getProvince())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "province不能为空", response, request);
					return;
				}*/
				if (StringUtils.isEmpty(member.getQqId())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "QQId不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getName())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "昵称不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getGender())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "性别不能为空", response, request);
					return;
				}
				temp.setQqId(member.getQqId());
				temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
				if(null==temp){
					member.setId(UUIDUtil.get32UUID());
					member.setCreateBy(member.getId());
					member.setUpdateBy(member.getId());
					member.setPhoneAuth(false);
					memberService.addEntity(IMemberDao.class, member);
					temp = new Member();
					temp.setQqId(member.getQqId());
					member = (Member) memberService.queryEntity(IMemberDao.class, temp);
				}else{
					if(member.getStatus()==Global.SYS_MEMBER_STATUS_2){
						JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER_DISABLED, response, request);
						return;
					}
					member = temp;
				}
			} else if (type.equals(Global.SYS_MEMBER_TYPE_WX)) {
				/*if (StringUtils.isEmpty(member.getCitycode())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "citycode不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getCityName())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "cityName不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getProvinceName())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "provinceName不能为空", response, request);
					return;
				}*/
				/*if (StringUtils.isEmpty(member.getProvince())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "province不能为空", response, request);
					return;
				}*/
				if (StringUtils.isEmpty(member.getWxId())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "WXId不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getName())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "昵称不能为空", response, request);
					return;
				}
				if (StringUtils.isEmpty(member.getGender())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "性别不能为空", response, request);
					return;
				}
				temp.setWxId(member.getWxId());
				temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
				if(null==temp){
					member.setId(UUIDUtil.get32UUID());
					member.setCreateBy(member.getId());
					member.setUpdateBy(member.getId());
					member.setPhoneAuth(false);
					memberService.addEntity(IMemberDao.class, member);
					temp = new Member();
					temp.setWxId(member.getWxId());
					member = (Member) memberService.queryEntity(IMemberDao.class, temp);
				}else{
					if(member.getStatus()==Global.SYS_MEMBER_STATUS_2){
						JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER_DISABLED, response, request);
						return;
					}
					member = temp;
				}
			} else {
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response, request);
				return;
			}
			Token token = new Token();
			token.setUserId(member.getId());
			token.setUsername(member.getName());
			token.setSubject(Global.JWT_SUBJECT_APP);
			token = (Token) tokenService.queryEntity(ITokenDao.class, token);
			JSONObject obj = new JSONObject();
			if(null==token){
				token = TokenUtil.getToken(member.getId(),member.getName(),Global.JWT_SUBJECT_APP,Global.getSetting(Global.JWT_SECRET_APP_KEY));
				tokenService.addEntity(ITokenDao.class, token);
			}else{
				//后台改key后重新生成新的token
				/*if(!token.getSecretKey().equals(Global.getSetting(Global.JWT_SECRET_APP_KEY))){
					tokenService.deleteEntityById(ITokenDao.class,token.getId());
					token = TokenUtil.getToken(member.getId(),member.getName(),Global.JWT_SUBJECT_APP,Global.getSetting(Global.JWT_SECRET_APP_KEY));
					tokenService.addEntity(ITokenDao.class, token);
				}else{*/
					//token已过期 生成新的token
					/*if(token.getSessionDate().before(new Date())){*/
						tokenService.deleteEntityById(ITokenDao.class,token.getId());
						token = TokenUtil.getToken(member.getId(),member.getName(),Global.JWT_SUBJECT_APP,Global.getSetting(Global.JWT_SECRET_APP_KEY));
						tokenService.addEntity(ITokenDao.class, token);
					/*}else{
						token.setSessionDate(new Date(new Date().getTime()+Global.JWT_APP_SESSION_TIME_OUT));
						token.setExtendDate(new Date(new Date().getTime()+Global.JWT_EXTEND_TIME));
						tokenService.updateEntity(ITokenDao.class, token);
					}*/
				//}
			}
			obj.put("token", token.getAccess_token());
			if(type.equals(Global.SYS_MEMBER_TYPE_PHONE)){
				String imId = JmessageUtil.getUserInfo(member);
				member.setPassword(Md5Util.getMd5Str(member.getId()));
				if(StringUtils.isEmpty(imId)){
					JmessageUtil.RegisterUser(member);
					imId = JmessageUtil.getUserInfo(member);
				}
				member.setImId(imId);
				member.setPassword(null);
			}
			obj.put("member", member);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 注册
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:08:04
	 */
	@RequestMapping("app/register")
	public void register(HttpServletRequest request, HttpServletResponse response, Member member) throws Exception {
		try {
			if (StringUtils.isEmpty(member.getGender())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "性别不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "昵称不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getCitycode())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "城市编号不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getCityName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "城市名不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getProvinceName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "省名不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getPhone())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "手机号码不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getPassword())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setPhone(member.getPhone());
			temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
			if(null!=temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "该手机号已经注册，可直接登录", response, request);
				return;
			}
			member.setId(UUIDUtil.get32UUID());
			member.setCreateBy(member.getId());
			member.setUpdateBy(member.getId());
			member.setPhoneAuth(true);
			member.setType(Global.SYS_MEMBER_TYPE_PHONE);
			News news = new News();
			news.setId(UUIDUtil.get32UUID());
			news.setMemberId(member.getId());
			news.setType(Global.SYS_MEMBER_NEWS_TYPE_1);
			news.setCitycode(member.getCitycode());
			news.setContent("用户 "+member.getName()+" 注册成功");
			news.setCreateBy(member.getId());
			news.setUpdateBy(member.getId());
			memberService.addEntity(INewsDao.class, news);
			memberService.addEntity(IMemberDao.class, member);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
