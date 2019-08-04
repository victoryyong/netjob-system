package com.thsword.netjob.web.controller.app.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.ICollectDao;
import com.thsword.netjob.dao.IFriendDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.ITokenDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Token;
import com.thsword.netjob.pojo.app.Collect;
import com.thsword.netjob.pojo.app.Friend;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.AmapUtil;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.RedisUtils;
import com.thsword.netjob.util.TokenUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.date.DateUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-用户", description = "用户接口")
public class MemberApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 绑定电话号码
	 * 
	 * @Description:TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/bindPhone")
	@ApiOperation(value = "热播视频", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "手机", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "citycode", value = "城市编码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "cityName", value = "城市名称", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "provinceName", value = "省名", dataType = "string", paramType = "query", required = true) })
	public JSONObject bindPhone(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String phone,
			@RequestParam String citycode, @RequestParam String cityName,
			@RequestParam String provinceName) throws Exception {
		Member member = (Member) request.getAttribute("member");
		String memberId = (String) request.getAttribute("memberId");
		JSONObject obj = new JSONObject();
		if (!StringUtils.isEmpty(member.getPhone()) || member.getPhoneAuth()
				|| member.getType() == Global.SYS_MEMBER_TYPE_PHONE) {
			throw new ServiceException("已绑定手机");
		}
		Member temp = new Member();
		temp.setPhone(phone);
		temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
		// 手机未被注册
		if (null == temp) {
			temp = new Member();
			temp.setId(memberId);
			temp.setPhone(phone);
			temp.setCitycode(citycode);
			temp.setCityName(cityName);
			temp.setProvinceName(provinceName);
			memberService.updateEntity(IMemberDao.class, temp);
			obj.put("token", null);
		} else {
			String existId = temp.getId();
			String existName = temp.getName();
			temp = new Member();
			temp.setId(existId);
			if (member.getType().equals(Global.SYS_MEMBER_TYPE_QQ)) {
				temp.setQqId(member.getQqId());
			} else if (member.getType().equals(Global.SYS_MEMBER_TYPE_WX)) {
				temp.setWxId(member.getWxId());
			}
			memberService.updateEntity(IMemberDao.class, temp);
			memberService.deleteEntityById(IMemberDao.class, memberId);
			Token token = new Token();
			token.setUserId(memberId);
			token.setSubject(Global.JWT_SUBJECT_APP);
			Token oldToken = (Token) memberService.queryEntity(ITokenDao.class,
					token);
			memberService.deleteEntity(ITokenDao.class, oldToken);
			token.setUserId(existId);
			oldToken = (Token) memberService
					.queryEntity(ITokenDao.class, token);
			memberService.deleteEntity(ITokenDao.class, oldToken);
			// 生成新的token
			token = TokenUtil.getToken(existId, existName,
					Global.JWT_SUBJECT_APP,
					Global.getSetting(Global.JWT_SECRET_APP_KEY));
			memberService.addEntity(ITokenDao.class, token);
			obj.put("token", token.getAccess_token());
		}
		return obj;
	}

	/**
	 * 查询用户信息
	 * 
	 * @Description:TODO
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * 
	 *             void
	 * 
	 * @exception:
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/visitor/memberInfo")
	@ApiOperation(value = "查询用户信息", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "memberId", value = "查询的用户ID", dataType = "string", paramType = "query", required = true) })
	public BaseResponse visit(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String memberId)
			throws Exception {
		String friendId = memberId;
		memberId = request.getAttribute("memberId") + "";
		Member member = (Member) memberService.queryEntityById(
				IMemberDao.class, friendId);
		// 是否好友
		Friend friend = new Friend();
		friend.setFriendId(friendId);
		friend.setMemberId(memberId);
		friend = (Friend) memberService.queryEntity(IFriendDao.class, friend);
		boolean flag = false;
		if (friend != null) {
			flag = true;
		}
		member.setMyFriend(flag);
		// 是否粉丝
		Collect collect = new Collect();
		collect.setBizId(friendId);
		collect.setMemberId(memberId);
		collect = (Collect) memberService.queryEntity(ICollectDao.class,
				collect);
		flag = false;
		if (friend != null) {
			flag = true;
		}
		member.setMyFans(flag);
		// 添加访问记录
		Map<String, Object> map = new HashMap<>();
		Page page = new Page();
		page.setSort("mv.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("memberId", memberId);
		map.put("visitorId", friendId);
		map.put("startDate",
				DateUtil.getDate(
						DateUtil.getString(new Date(), DateUtil.FORMAT_STYLE_2)
								+ " 00:00:00", DateUtil.FORMAT_STYLE_1));
		map.put("endDate",
				DateUtil.getDate(
						DateUtil.getString(new Date(), DateUtil.FORMAT_STYLE_2)
								+ " 23:59:59", DateUtil.FORMAT_STYLE_1));
		map.put("page", page);
		List<Map<String, Object>> visitors = memberService
				.queryPageVisitors(map);
		if (!CollectionUtils.isEmpty(visitors)) {
			memberService.updateVisitor(visitors.get(0).get("mv_id") + "");
		} else {
			memberService.addVisitor(UUIDUtil.get32UUID(), memberId, friendId);
		}
		// 计算距离
		if (!StringUtils.isEmpty(member.getLatitude())
				&& !StringUtils.isEmpty(member.getLongitude())) {
			String longitude = request.getParameter("longitude");
			String latitude = request.getParameter("latitude");
			if (!StringUtils.isEmpty(longitude)
					&& !StringUtils.isEmpty(latitude)) {
				Double distance = AmapUtil.distanceByLongNLat(
						Double.parseDouble(longitude),
						Double.parseDouble(longitude),
						Double.parseDouble(member.getLatitude()),
						Double.parseDouble(member.getLongitude()));
				member.setDistance(distance);
			}
		}
		return BaseResponse.success();
	}

	/**
	 * 
	 * 
	 * @Description:同城大牌
	 * 
	 * @param request
	 * @param response
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
	@RequestMapping("app/visitor/famous")
	@ApiOperation(value = "同城大牌", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "citycode", value = "城市编码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10") })
	public BaseResponse recommend(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String citycode,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		Member temp = new Member();
		temp.setCitycode(citycode);
		int count = memberService.queryCountEntity(IMemberDao.class, temp);
		if (count < 50) {
			@SuppressWarnings("unchecked")
			List<Member> members = (List<Member>) memberService.queryAllEntity(
					IMemberDao.class, temp);
			int lackCount = 50;
			Page lackPage = new Page(1, lackCount);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", lackPage);
			List<Member> lackMembers = (List<Member>) memberService
					.queryPageFamous(IMemberDao.class, map);
			List<Member> temps = new ArrayList<Member>();
			temps.addAll(members);
			for (Member lackMember : lackMembers) {
				boolean findMember = false;
				for (Member member : members) {
					if (member.getId().equals(lackMember.getId())) {
						findMember = true;
						break;
					}
				}
				if (!findMember) {
					temps.add(lackMember);
				}
			}
			List<Member> resultMembers = new ArrayList<Member>();
			int startNum = (page.getCurrentPage() - 1) * page.getPageSize() + 1;
			int endNum = page.getCurrentPage() * page.getPageSize();
			for (int i = 1; i <= temps.size(); i++) {
				if (i >= startNum && i <= endNum) {
					if (temps.size() >= i) {
						resultMembers.add(temps.get(i - 1));
					}
				}
			}
			return BaseResponse.success(resultMembers);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("citycode", citycode);
			map.put("page", page);
			List<Member> members = (List<Member>) memberService
					.queryPageFamous(IMemberDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", members);
			obj.put("page", page);
			return BaseResponse.success(obj);
		}
	}

	/**
	 * 
	 * @Description:修改用户信息
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/changeInfo")
	@ApiOperation(value = "修改用户信息", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "image", value = "缩略图", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "昵称", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "background", value = "背景图", dataType = "string", paramType = "query") })
	public BaseResponse changeInfo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String image,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String background) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Member member = (Member) memberService.queryEntityById(
				IMemberDao.class, memberId);
		if (null == member) {
			throw new ServiceException(ErrorUtil.LOGIN_ERROR_USER);
		}
		if (StringUtils.isEmpty(image) && StringUtils.isEmpty(name)
				&& StringUtils.isEmpty(background)) {
			throw new ServiceException("参数不能都为空");
		}
		Member temp = new Member();
		temp.setId(memberId);
		temp.setImage(image);
		temp.setName(name);
		temp.setBackground(background);
		memberService.updateEntity(IMemberDao.class, temp);
		return BaseResponse.success();
	}

	/**
	 * changePwd
	 * 
	 * @author: yong
	 * @time:2018年5月8日 上午12:08:04
	 */
	@RequestMapping("app/changePwd")
	@ApiOperation(value = "修改用户密码", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "电话", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "password", value = "旧密码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "string", paramType = "query", required = true) })
	public BaseResponse changePwd(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String phone,
			@RequestParam String password, @RequestParam String newPassword)
			throws Exception {
		Member temp = new Member();
		temp.setPhone(phone);
		temp.setPassword(password);
		temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
		if (null == temp) {
			throw new ServiceException("账号或密码错误");
		}
		temp.setPassword(newPassword);
		memberService.updateEntity(IMemberDao.class, temp);
		return BaseResponse.success();
	}

	@RequestMapping("app/visitor/member/forgetPwd")
	@ApiOperation(value = "忘记密码", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "identifyCode", value = "验证码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "phone", value = "电话", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true) })
	public BaseResponse forgetPwd(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String phone,
			@RequestParam String identifyCode, @RequestParam String password)
			throws Exception {
		String redisKey = "member:forgetLoginPwd:" + phone;
		String redisValue = RedisUtils.get(redisKey);
		if (StringUtils.isEmpty(redisValue)) {
			throw new ServiceException("验证码无效");
		}
		JSONObject idenfifyInfo = JSONObject.parseObject(redisValue);

		String checkPhone = idenfifyInfo.getString("phone");
		String checkIdentifyCode = idenfifyInfo.getString("identifyCode");
		if (!checkPhone.equals(phone)) {
			throw new ServiceException("号码不匹配");
		}
		if (!checkIdentifyCode.equals(identifyCode)) {
			throw new ServiceException("验证码错误");
		}
		Member temp = new Member();
		temp.setPhone(phone);
		temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
		if (null == temp) {
			throw new ServiceException("账号不存在");
		}
		temp.setPassword(password);
		memberService.updateEntity(IMemberDao.class, temp);
		RedisUtils.del(redisKey);
		return BaseResponse.success();
	}
}
