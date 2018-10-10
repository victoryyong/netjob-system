package com.thsword.netjob.web.controller.app.member;

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

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IMemberDao;
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
	
	 * @Description:我的收藏列表
	
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
			map.put("memberId", memberId);
			page.setSort("mf.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			map.put("page", page);
			List<Member> friends = (List<Member>) memberService.queryPageFriends(map);
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
	
	 * @Description:我的粉丝列表
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/fans")
	public void fans(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			Map<String, Object> map = new HashMap<>();
			map.put("friendId", memberId);
			page.setSort("mf.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			map.put("page", page);
			List<Member> friends = (List<Member>) memberService.queryPageFriends(map);
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
	public void addFriend(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String friendId = request.getParameter("friendId");
			if (StringUtils.isEmpty(friendId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "friendId不能为空", response, request);
				return;
			}
			if (memberId.equals(friendId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿添加自己!", response, request);
				return;
			}
			Member member = (Member) memberService.queryEntityById(IMemberDao.class, friendId);
			if(null==member){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户不存在!", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("memberId", memberId);
			map.put("friendId", friendId);
			map.put("page", page);
			page.setSort("mf.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			List<Member> friends = (List<Member>) memberService.queryPageFriends(map);
			if(!CollectionUtils.isEmpty(friends)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿重复添加!", response, request);
				return;
			}
			memberService.addFriend(UUIDUtil.get32UUID(),memberId,friendId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 取消收藏
	
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
		memberService.deleteFriend(memberId,friendId);
		JsonResponseUtil.successCodeResponse(response, request);
	}
	
	/**
	 * 取消收藏
	
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
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("friendId", friendId);
		map.put("page", page);
		page.setSort("mf.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		List<Member> friends = (List<Member>) memberService.queryPageFriends(map);
		boolean flag = false;
		if(!CollectionUtils.isEmpty(friends)){
			flag = true;
		}
		JSONObject obj = new JSONObject();
		obj.put("isFriend", flag);
		JsonResponseUtil.successBodyResponse(obj, response, request);
	}
	
	
}
