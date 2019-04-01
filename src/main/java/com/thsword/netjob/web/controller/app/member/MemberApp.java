package com.thsword.netjob.web.controller.app.member;

import java.util.ArrayList;
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

import cn.jmessage.api.common.model.Members;

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
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.RedisUtils;
import com.thsword.netjob.util.TokenUtil;
import com.thsword.utils.date.DateUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class MemberApp {
	@Resource(name = "memberService")
	MemberService memberService;

	
	
	/**
	 * 查询用户信息
	 * @Description:TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/bindPhone")
	public void bindPhone(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			
			Member member = (Member) request.getAttribute("member");
			String memberId = (String) request.getAttribute("memberId");
			String phone = (String) request.getParameter("phone");
			String citycode = request.getParameter("citycode");
			String cityName = request.getParameter("cityName");
			String provinceName = request.getParameter("provinceName");
			JSONObject obj = new JSONObject();
			if(!StringUtils.isEmpty(member.getPhone())||member.getPhoneAuth()||member.getType()==Global.SYS_MEMBER_TYPE_PHONE){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "已绑定手机", response, request);
				return;
			}
			if(StringUtils.isEmpty(phone)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "phone不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(citycode)) {
				if(!StringUtils.isEmpty(member.getCitycode())){
					citycode=member.getCitycode();
				}else{
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "citycode不能为空", response, request);
					return;
				}
			}
			if (StringUtils.isEmpty(cityName)) {
				if(!StringUtils.isEmpty(member.getCityName())){
					cityName=member.getCityName();
				}else{
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "cityName不能为空", response, request);
					return;
				}
			}
			if (StringUtils.isEmpty(provinceName)) {
				if(!StringUtils.isEmpty(member.getProvinceName())){
					provinceName=member.getProvinceName();
				}else{
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "provinceName不能为空", response, request);
					return;
				}
			}
			Member temp = new Member();
			temp.setPhone(phone);
			temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
			//手机未被注册
			if(null==temp){
				temp = new Member();
				temp.setId(memberId);
				temp.setPhone(phone);
				temp.setCitycode(citycode);
				temp.setCityName(cityName);
				temp.setProvinceName(provinceName);
				memberService.updateEntity(IMemberDao.class, temp);
				obj.put("token", null);
			}else{
				String existId = temp.getId();
				String existName = temp.getName();
				temp = new Member();
				temp.setId(existId);
				if(member.getType().equals(Global.SYS_MEMBER_TYPE_QQ)){
					temp.setQqId(member.getQqId());
				}else if(member.getType().equals(Global.SYS_MEMBER_TYPE_WX)){
					temp.setWxId(member.getWxId());
				}
				memberService.updateEntity(IMemberDao.class, temp);
				memberService.deleteEntityById(IMemberDao.class, memberId);
				Token token = new Token();
				token.setUserId(memberId);
				token.setSubject(Global.JWT_SUBJECT_APP);
				Token oldToken = (Token) memberService.queryEntity(ITokenDao.class, token);
				memberService.deleteEntity(ITokenDao.class, oldToken);
				token.setUserId(existId);
				oldToken = (Token) memberService.queryEntity(ITokenDao.class, token);
				memberService.deleteEntity(ITokenDao.class, oldToken);
				//生成新的token
				token = TokenUtil.getToken(existId,existName,Global.JWT_SUBJECT_APP,Global.getSetting(Global.JWT_SECRET_APP_KEY));
				memberService.addEntity(ITokenDao.class, token);
				obj.put("token", token.getAccess_token());
			}
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询用户信息
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/visitor/memberInfo")
	public void visit(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception {
		String memberId = request.getParameter("userId")+"";
		String friendId = request.getParameter("memberId");
		if (StringUtils.isEmpty(friendId)) {
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "memberId不能为空", response, request);
			return;
		}
		if (StringUtils.isEmpty(memberId)) {
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "userId不能为空", response, request);
			return;
		}
		Member member = (Member) memberService.queryEntityById(IMemberDao.class, friendId);
		//是否好友
		Friend friend = new Friend();
		friend.setFriendId(friendId);
		friend.setMemberId(memberId);
		friend = (Friend) memberService.queryEntity(IFriendDao.class, friend);
		boolean flag = false;
		if(friend!=null){
			flag = true;
		}
		member.setMyFriend(flag);
		//是否粉丝
		Collect collect = new Collect();
		collect.setBizId(friendId);
		collect.setMemberId(memberId);
		collect = (Collect) memberService.queryEntity(ICollectDao.class, collect);
		flag = false;
		if(friend!=null){
			flag = true;
		}
		member.setMyFans(flag);
		//添加访问记录
		Map<String, Object> map = new HashMap<>();
		page.setSort("mv.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("memberId", memberId);
		map.put("visitorId", friendId);
		map.put("startDate", DateUtil.getDate(DateUtil.getString(new Date(), DateUtil.FORMAT_STYLE_2)+" 00:00:00", DateUtil.FORMAT_STYLE_1));
		map.put("endDate", DateUtil.getDate(DateUtil.getString(new Date(), DateUtil.FORMAT_STYLE_2)+" 23:59:59", DateUtil.FORMAT_STYLE_1));
		map.put("page", page);
		List<Map<String, Object>> visitors = memberService.queryPageVisitors(map);
		if(!CollectionUtils.isEmpty(visitors)){
			memberService.updateVisitor(visitors.get(0).get("mv_id")+"");
		}else{
			memberService.addVisitor(UUIDUtil.get32UUID(), memberId, friendId);
		}
		//计算距离
		if(!StringUtils.isEmpty(member.getLatitude())&&!StringUtils.isEmpty(member.getLongitude())){
			String longitude = request.getParameter("longitude");
			String latitude = request.getParameter("latitude");
			if(!StringUtils.isEmpty(longitude)&&!StringUtils.isEmpty(latitude)){
				Double distance = AmapUtil.distanceByLongNLat(Double.parseDouble(longitude), Double.parseDouble(longitude), Double.parseDouble(member.getLatitude()), Double.parseDouble(member.getLongitude()));
				member.setDistance(distance);
			}
		}
		JsonResponseUtil.successBodyResponse(member, response, request);
	}	
	
	/**
	 * 
	
	 * @Description:同城大牌
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/famous")
	public void recommend(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String citycode=request.getParameter("citycode");
			Member temp = new Member();
			temp.setCitycode(citycode);
			int count = memberService.queryCountEntity(IMemberDao.class, temp);
			if(count<50){
				List<Member> members = (List<Member>) memberService.queryAllEntity(IMemberDao.class, temp);
				int lackCount = 50;
				Page lackPage = new Page(1, lackCount);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", lackPage);
				List<Member> lackMembers = (List<Member>) memberService.queryPageFamous(IMemberDao.class, map);
				List<Member> temps = new ArrayList<Member>();
				temps.addAll(members);
				for (Member lackMember : lackMembers) {
					boolean findMember = false;
					for (Member member : members) {
						if(member.getId().equals(lackMember.getId())){
							findMember=true;
							break;
						}
					}
					if(!findMember){
						temps.add(lackMember);
					}
				}
				List<Member> resultMembers = new ArrayList<Member>();
				int startNum = (page.getCurrentPage()-1)*page.getPageSize()+1;
				int endNum = page.getCurrentPage()*page.getPageSize();
				for(int i=1;i<=temps.size();i++){
					if(i>=startNum&&i<=endNum){
						if(temps.size()>=i){
							resultMembers.add(temps.get(i-1));
						}
					}
				}
				JsonResponseUtil.successBodyResponse(resultMembers, response, request);
			}else{
				Map<String, Object> map = new HashMap<>();
				map.put("citycode", citycode);
				map.put("page", page);
				List<Member> members = (List<Member>) memberService.queryPageFamous(IMemberDao.class, map);
				JSONObject obj = new JSONObject();
				obj.put("list", members);
				obj.put("page", page);
				JsonResponseUtil.successBodyResponse(obj, response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:修改用户信息
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/changeInfo")
	public void changeInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String image=request.getParameter("image");
			String name=request.getParameter("name");
			String background=request.getParameter("background");
			Member member = (Member) memberService.queryEntityById(IMemberDao.class, memberId);
			if(null==member){
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER, response, request);
				return;
			}
			if(StringUtils.isEmpty(image)&&StringUtils.isEmpty(name)&&StringUtils.isEmpty(background)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"参数不能都为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setId(memberId);
			temp.setImage(image);
			temp.setName(name);
			temp.setBackground(background);
			memberService.updateEntity(IMemberDao.class, temp);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * changePwd
	 * @author: yong
	 * @time:2018年5月8日 上午12:08:04
	 */
	@RequestMapping("app/changePwd")
	public void changePwd(HttpServletRequest request, HttpServletResponse response, Member member) throws Exception {
		try {
			if (StringUtils.isEmpty(member.getPhone())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "电话不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getPassword())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			String newPassword = request.getParameter("newPassword");
			if (StringUtils.isEmpty(newPassword)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "新密码不能为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setPhone(member.getPhone());
			temp.setPassword(member.getPassword());
			temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
			if(null==temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账号或密码错误", response, request);
				return;
			}
			temp.setPassword(newPassword);
			memberService.updateEntity(IMemberDao.class, temp);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("app/forgetPwd")
	public void forgetPwd(HttpServletRequest request, HttpServletResponse response, Member member) throws Exception {
		try {
			String identifyCode = request.getParameter("identifyCode");
			if (StringUtils.isEmpty(member.getPhone())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "电话不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getPassword())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(identifyCode)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "验证码不能为空", response, request);
				return;
			}
			String redisKey = "member:forgetLoginPwd:"+member.getPhone();
			String redisValue=RedisUtils.get(redisKey);
			if(StringUtils.isEmpty(redisValue)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "验证码无效", response, request);
				return;
			}
			JSONObject idenfifyInfo = JSONObject.parseObject(redisValue);
			
			String checkPhone = idenfifyInfo.getString("phone");
			String checkIdentifyCode = idenfifyInfo.getString("identifyCode");
			if(!checkPhone.equals(member.getPhone())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "号码不匹配", response, request);
				return;
			}
			if(!checkIdentifyCode.equals(identifyCode)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "验证码错误", response, request);
				return;
			}
			Member temp = new Member();
			temp.setPhone(member.getPhone());
			temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
			if(null==temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账号不存在", response, request);
				return;
			}
			temp.setPassword(member.getPassword());
			memberService.updateEntity(IMemberDao.class, temp);
			RedisUtils.del(redisKey);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
