package com.thsword.netjob.web.controller.app.friend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IFriendDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.pojo.app.Friend;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-FRIEND", description = "好友接口")
public class FriendApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 
	 * @Description:我的好友列表
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/friends")
	@ApiOperation(value = "我的好友列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10") })
	public JSONObject friends(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		String memberId = request.getAttribute("memberId") + "";
		Map<String, Object> map = new HashMap<>();
		page.setSort("f.c_createDate");
		map.put("memberId", memberId);
		map.put("page", page);
		@SuppressWarnings("unchecked")
		List<Friend> friends = (List<Friend>) memberService.queryPageEntity(
				IFriendDao.class, map);
		JSONObject obj = new JSONObject();
		obj.put("list", friends);
		obj.put("page", page);
		return obj;
	}

	/**
	 * 
	 * @Description:添加好友
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/addFriend")
	@ApiOperation(value = "添加好友", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "friendId", value = "好友ID", dataType = "string", paramType = "query") })
	public BaseResponse addFriend(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String friendId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (memberId.equals(friendId)) {
			throw new ServiceException("请勿添加自己!");
		}
		Member member = (Member) memberService.queryEntityById(
				IMemberDao.class, friendId);
		if (null == member) {
			throw new ServiceException("用户不存在!");
		}
		Friend friend = new Friend();
		friend.setMemberId(memberId);
		Friend temp = (Friend) memberService.queryEntity(IFriendDao.class,
				friend);
		if (null != temp) {
			throw new ServiceException("请勿重复添加!");
		}
		friend.setId(UUIDUtil.get32UUID());
		memberService.addEntity(IFriendDao.class, friend);

		friend.setId(UUIDUtil.get32UUID());
		friend.setMemberId(friend.getFriendId());
		friend.setFriendId(memberId);
		memberService.addEntity(IFriendDao.class, friend);
		return BaseResponse.success();
	}

	/**
	 * 删除好友
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
	@RequestMapping("app/member/deleteFriend")
	@ApiOperation(value = "删除好友", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "friendId", value = "好友ID", dataType = "string", paramType = "query") })
	public BaseResponse deleteFriend(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String friendId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";

		Friend temp = new Friend();
		temp.setFriendId(friendId);
		temp.setMemberId(memberId);
		memberService.deleteEntity(IFriendDao.class, temp);
		temp.setFriendId(memberId);
		temp.setMemberId(friendId);
		memberService.deleteEntity(IFriendDao.class, temp);
		return BaseResponse.success();
	}

	/**
	 * 是否是好友
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
	@RequestMapping("app/member/isFriend")
	@ApiOperation(value = "是否是好友", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "friendId", value = "好友ID", dataType = "string", paramType = "query") })
	public JSONObject isFriend(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String friendId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Friend temp = new Friend();
		temp.setFriendId(friendId);
		temp.setMemberId(memberId);
		temp = (Friend) memberService.queryEntity(IFriendDao.class, temp);
		boolean flag = false;
		if (temp != null) {
			flag = true;
		}
		JSONObject obj = new JSONObject();
		obj.put("isFriend", flag);
		return obj;
	}

}
