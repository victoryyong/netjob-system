package com.thsword.netjob.web.controller.app.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.thsword.netjob.util.TokenUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.md5.Md5Util;
import com.thsword.utils.object.UUIDUtil;

@RestController
@Api(tags = "NETJOB-LOGIN", description = "登陆/注册")
public class LoginApp {
	@Resource(name = "memberService")
	MemberService memberService;
	@Resource(name = "tokenService")
	TokenService tokenService;

	/**
	 * 
	 * 
	 * @Description:登陆
	 * 
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	 * 
	 *             void
	 * 
	 * @exception:
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/login")
	@ApiOperation(value = "登陆", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "登录类型（phone、qq、wx）", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "手机账号", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "qqId", value = "qq登录Id", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "wxId", value = "wx登录Id", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "gender", value = "性别", dataType = "string", paramType = "query") })
	public JSONObject login(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String type,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) String password,
			@RequestParam(required = false) String qqId,
			@RequestParam(required = false) String wxId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer gender) throws Exception {
		if (StringUtils.isEmpty(type)) {
			throw new ServiceException("登陆类型不能为空");
		}
		Member temp = new Member();
		Member member = new Member();
		member.setPhone(phone);
		member.setPassword(password);
		member.setWxId(wxId);
		member.setQqId(qqId);
		member.setGender(gender);
		member.setName(name);
		if (type.equals(Global.SYS_MEMBER_TYPE_PHONE)) {
			if (StringUtils.isEmpty(phone)) {
				throw new ServiceException("账号不能为空");
			}
			if (StringUtils.isEmpty(password)) {
				throw new ServiceException("密码不能为空");
			}
			temp.setPassword(password);
			temp.setPhone(phone);
			member = (Member) memberService.queryEntity(IMemberDao.class, temp);
			if (null == member) {
				throw new ServiceException(ErrorUtil.LOGIN_NAME_PASSWORD_ERROR);
			}
			if (member.getStatus() == Global.SYS_MEMBER_STATUS_2) {
				throw new ServiceException(ErrorUtil.LOGIN_ERROR_USER_DISABLED);
			}
		} else if (type.equals(Global.SYS_MEMBER_TYPE_QQ)) {
			if (StringUtils.isEmpty(member.getQqId())) {
				throw new ServiceException("QQId不能为空");
			}
			if (StringUtils.isEmpty(member.getName())) {
				throw new ServiceException("昵称不能为空");
			}
			if (StringUtils.isEmpty(member.getGender())) {
				throw new ServiceException("性别不能为空");
			}
			temp.setQqId(member.getQqId());
			temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
			if (null == temp) {
				member.setId(UUIDUtil.get32UUID());
				member.setCreateBy(member.getId());
				member.setUpdateBy(member.getId());
				member.setPhoneAuth(false);
				memberService.addEntity(IMemberDao.class, member);
				temp = new Member();
				temp.setQqId(member.getQqId());
				member = (Member) memberService.queryEntity(IMemberDao.class,
						temp);
			} else {
				if (member.getStatus() == Global.SYS_MEMBER_STATUS_2) {
					throw new ServiceException(
							ErrorUtil.LOGIN_ERROR_USER_DISABLED);
				}
				member = temp;
			}
		} else if (type.equals(Global.SYS_MEMBER_TYPE_WX)) {
			if (StringUtils.isEmpty(member.getWxId())) {
				throw new ServiceException("WXId不能为空");
			}
			if (StringUtils.isEmpty(member.getName())) {
				throw new ServiceException("昵称不能为空");
			}
			if (StringUtils.isEmpty(member.getGender())) {
				throw new ServiceException("性别不能为空");
			}
			temp.setWxId(member.getWxId());
			temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
			if (null == temp) {
				member.setId(UUIDUtil.get32UUID());
				member.setCreateBy(member.getId());
				member.setUpdateBy(member.getId());
				member.setPhoneAuth(false);
				memberService.addEntity(IMemberDao.class, member);
				temp = new Member();
				temp.setWxId(member.getWxId());
				member = (Member) memberService.queryEntity(IMemberDao.class,
						temp);
			} else {
				if (member.getStatus() == Global.SYS_MEMBER_STATUS_2) {
					throw new ServiceException(
							ErrorUtil.LOGIN_ERROR_USER_DISABLED);
				}
				member = temp;
			}
		} else {
			throw new ServiceException(ErrorUtil.REQUEST_INVALID_PARAM);
		}
		Token token = new Token();
		token.setUserId(member.getId());
		token.setUsername(member.getName());
		token.setSubject(Global.JWT_SUBJECT_APP);
		token = (Token) tokenService.queryEntity(ITokenDao.class, token);
		JSONObject obj = new JSONObject();
		if (null == token) {
			token = TokenUtil.getToken(member.getId(), member.getName(),
					Global.JWT_SUBJECT_APP,
					Global.getSetting(Global.JWT_SECRET_APP_KEY));
			tokenService.addEntity(ITokenDao.class, token);
		} else {
			tokenService.deleteEntityById(ITokenDao.class, token.getId());
			token = TokenUtil.getToken(member.getId(), member.getName(),
					Global.JWT_SUBJECT_APP,
					Global.getSetting(Global.JWT_SECRET_APP_KEY));
			tokenService.addEntity(ITokenDao.class, token);
		}
		obj.put("token", token.getAccess_token());
		if (type.equals(Global.SYS_MEMBER_TYPE_PHONE)) {
			String imId = JmessageUtil.getUserInfo(member);
			member.setPassword(Md5Util.getMd5Str(member.getId()));
			if (StringUtils.isEmpty(imId)) {
				JmessageUtil.RegisterUser(member);
				imId = JmessageUtil.getUserInfo(member);
			}
			member.setImId(imId);
			member.setPassword(null);
		}
		obj.put("member", member);
		return obj;
	}

	/**
	 * 注册
	 * 
	 * @Description:TODO
	 * 
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	 * 
	 *             void
	 * 
	 * @exception:
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:08:04
	 */
	@RequestMapping("app/register")
	@ApiOperation(value = "注册", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "gender", value = "性别", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "citycode", value = "城市代码", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "cityName", value = "城市名称", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "provinceName", value = "省名称", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "手机号码", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query") })
	public BaseResponse register(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String name,
			@RequestParam Integer gender, @RequestParam String citycode,
			@RequestParam String cityName, @RequestParam String provinceName,
			@RequestParam String phone, @RequestParam String password)
			throws Exception {
		Member member = new Member();
		member.setName(name);
		member.setGender(gender);
		member.setCitycode(citycode);
		member.setCityName(cityName);
		member.setProvinceName(provinceName);
		member.setPhone(phone);
		member.setPassword(password);

		Member temp = new Member();
		temp.setPhone(member.getPhone());
		temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
		if (null != temp) {
			throw new ServiceException("该手机号已经注册，可直接登录");
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
		news.setContent("用户 " + member.getName() + " 注册成功");
		news.setCreateBy(member.getId());
		news.setUpdateBy(member.getId());
		memberService.addEntity(INewsDao.class, news);
		memberService.addEntity(IMemberDao.class, member);
		return BaseResponse.success();
	}
}
