package com.thsword.netjob.web.controller.app.member;

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

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.date.DateUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class VisitApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 访问
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/visit")
	public void visit(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception {
		String visitorId = request.getParameter("visitorId");
		String memberId = (String) request.getAttribute("memberId");
		if(StringUtils.isEmpty(visitorId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "访问ID不能为空", response, request);
			return;
		}
		if(memberId.equals(visitorId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿访问自己!", response, request);
			return;
		}
		Member member = (Member) memberService.queryEntityById(IMemberDao.class, visitorId);
		if(null==member){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户不存在!", response, request);
			return;
		}
		Map<String, Object> map = new HashMap<>();
		page.setSort("mv.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("memberId", memberId);
		map.put("visitorId", visitorId);
		map.put("startDate", DateUtil.getDate(DateUtil.getString(new Date(), DateUtil.FORMAT_STYLE_2)+" 00:00:00", DateUtil.FORMAT_STYLE_1));
		map.put("endDate", DateUtil.getDate(DateUtil.getString(new Date(), DateUtil.FORMAT_STYLE_2)+"23:59:59", DateUtil.FORMAT_STYLE_1));
		map.put("page", page);
		List<Map<String, Object>> visitors = memberService.queryPageVisitors(map);
		if(!CollectionUtils.isEmpty(visitors)){
			memberService.updateVisitor(visitors.get(0).get("mv_id")+"");
		}else{
			memberService.addVisitor(UUIDUtil.get32UUID(), memberId, visitorId);
		}
		JsonResponseUtil.successCodeResponse(response, request);
	}
	
	
	/**
	 * 访客
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/visitors")
	public void visitors(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		String memberId = request.getParameter("memberId");
		if(StringUtils.isEmpty(memberId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户ID不能为空", response, request);
			return;
		}
		Map<String, Object> map = new HashMap<>();
		page.setSort("mv.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("visitorId", memberId);
		map.put("page", page);
		List<Map<String, Object>> visitors = memberService.queryPageVisitors(map);
		JSONObject obj = new JSONObject();
		obj.put("list", visitors);
		obj.put("page", page);
		JsonResponseUtil.successBodyResponse(obj,response, request);
	}
}
