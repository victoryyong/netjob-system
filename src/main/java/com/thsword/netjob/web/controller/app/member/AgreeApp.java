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

@Controller
public class AgreeApp {
	@Resource(name = "agreeService")
	AgreeService agreeService;

	/**
	 * 点赞
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/agree")
	public void visit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String agreeId = request.getParameter("agreeId");
		String memberId = (String) request.getAttribute("memberId");
		if(StringUtils.isEmpty(agreeId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞ID不能为空", response, request);
			return;
		}
		if(memberId.equals(agreeId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿给自己点赞!", response, request);
			return;
		}
		Member member = (Member) memberService.queryEntityById(IMemberDao.class, memberId);
		if(null==member){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户不存在!", response, request);
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("agreeId", agreeId);
		List<Map<String, Object>> agrees = memberService.queryAgrees(map);
		if(!CollectionUtils.isEmpty(agrees)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿重复点赞!", response, request);
			return;
		}
		memberService.addAgree(UUIDUtil.get32UUID(), memberId, agreeId);
		JsonResponseUtil.successCodeResponse(response, request);
	}
	
	
	/**
	 * 取消点赞
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/unAgree")
	public void visitors(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String agreeId = request.getParameter("agreeId");
		String memberId = (String) request.getAttribute("memberId");
		memberService.unAgree(memberId,agreeId);
		JsonResponseUtil.successCodeResponse(response, request);
	}
	/**
	 * 是否点赞过
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月16日 下午3:16:22
	 */
	@RequestMapping("app/member/isAgreed")
	public void isAgreed(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String agreeId = request.getParameter("agreeId");
		String memberId = (String) request.getAttribute("memberId");
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("agreeId", agreeId);
		List<Map<String, Object>> agrees = memberService.queryAgrees(map);
		boolean flag = false;
		if(!CollectionUtils.isEmpty(agrees)){
			flag = true;
		}
		JSONObject obj = new JSONObject();
		obj.put("isAgreed", flag);
		JsonResponseUtil.successBodyResponse(obj, response, request);
	}
}
