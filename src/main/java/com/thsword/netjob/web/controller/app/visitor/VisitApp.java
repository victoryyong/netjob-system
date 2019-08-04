package com.thsword.netjob.web.controller.app.visitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.date.DateUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-VISIT", description = "访客接口")
public class VisitApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 访问
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
	@ApiOperation(value = "访问", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "visitorId", value = "访客ID", dataType = "string", paramType = "query"), })
	@RequestMapping("app/member/visit")
	public BaseResponse visit(HttpServletRequest request,
			HttpServletResponse response, Page page) throws Exception {
		String visitorId = request.getParameter("visitorId");
		String memberId = (String) request.getAttribute("memberId");
		if (StringUtils.isEmpty(visitorId)) {
			throw new ServiceException("访问ID不能为空");
		}
		if (memberId.equals(visitorId)) {
			throw new ServiceException("请勿访问自己!");
		}
		Member member = (Member) memberService.queryEntityById(
				IMemberDao.class, visitorId);
		if (null == member) {
			throw new ServiceException("用户不存在!");
		}
		Map<String, Object> map = new HashMap<>();
		page.setSort("mv.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("memberId", memberId);
		map.put("visitorId", visitorId);
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
			memberService.addVisitor(UUIDUtil.get32UUID(), memberId, visitorId);
		}
		return BaseResponse.success();
	}

	/**
	 * 访客
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
	@RequestMapping("app/visitor/visitors")
	@ApiOperation(value = "访客列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberId", value = "用戶", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query"), })
	public JSONObject visitors(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		String memberId = request.getParameter("memberId");
		if (StringUtils.isEmpty(memberId)) {
			throw new ServiceException("用户ID不能为空");
		}
		Map<String, Object> map = new HashMap<>();
		page.setSort("mv.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("visitorId", memberId);
		map.put("page", page);
		List<Map<String, Object>> visitors = memberService
				.queryPageVisitors(map);
		JSONObject obj = new JSONObject();
		obj.put("list", visitors);
		obj.put("page", page);
		return obj;
	}
}
