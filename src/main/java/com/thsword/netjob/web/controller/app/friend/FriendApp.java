package com.thsword.netjob.web.controller.app.friend;

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
import com.thsword.netjob.dao.IFriendDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.pojo.app.Friend;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class FriendApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 
	 * @Description:我的好友列表
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/friends")
	public void friends(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			Map<String, Object> map = new HashMap<>();
			page.setSort("f.c_createDate");
			map.put("memberId", memberId);
			map.put("page", page);
			@SuppressWarnings("unchecked")
			List<Friend> friends = (List<Friend>) memberService.queryPageEntity(IFriendDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", friends);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 
	 * @Description:添加好友
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/addFriend")
	public void addFriend(HttpServletRequest request, HttpServletResponse response,Page page,Friend friend) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(friend.getFriendId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "friendId不能为空", response, request);
				return;
			}
			if (memberId.equals(friend.getFriendId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿添加自己!", response, request);
				return;
			}
			Member member = (Member) memberService.queryEntityById(IMemberDao.class, friend.getFriendId());
			if(null==member){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户不存在!", response, request);
				return;
			}
			friend.setMemberId(memberId);
			Friend temp = (Friend) memberService.queryEntity(IFriendDao.class, friend);
			if(null!=temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿重复添加!", response, request);
				return;
			}
			friend.setId(UUIDUtil.get32UUID());
			memberService.addEntity(IFriendDao.class, friend);
			
			friend.setId(UUIDUtil.get32UUID());
			friend.setMemberId(friend.getFriendId());
			friend.setFriendId(memberId);
			memberService.addEntity(IFriendDao.class, friend);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 删除好友
	 * @Description:TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/deleteFriend")
	public void deleteFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String memberId = request.getAttribute("memberId")+"";
		String friendId = request.getParameter("friendId");
		if (StringUtils.isEmpty(friendId)) {
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "friendId不能为空", response, request);
			return;
		}
		Friend temp = new Friend();
		temp.setFriendId(friendId);
		temp.setMemberId(memberId);
		memberService.deleteEntity(IFriendDao.class, temp);
		temp.setFriendId(memberId);
		temp.setMemberId(friendId);
		memberService.deleteEntity(IFriendDao.class, temp);
		JsonResponseUtil.successCodeResponse(response, request);
	}
	
	/**
	 * 是否是好友 
	 * @Description:TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/isFriend")
	public void isFriend(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		String memberId = request.getAttribute("memberId")+"";
		String friendId = request.getParameter("friendId");
		if (StringUtils.isEmpty(friendId)) {
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "friendId不能为空", response, request);
			return;
		}
		Friend temp = new Friend();
		temp.setFriendId(friendId);
		temp.setMemberId(memberId);
		temp = (Friend) memberService.queryEntity(IFriendDao.class, temp);
		boolean flag = false;
		if(temp!=null){
			flag = true;
		}
		JSONObject obj = new JSONObject();
		obj.put("isFriend", flag);
		JsonResponseUtil.successBodyResponse(obj, response, request);
	}
	
	
}
