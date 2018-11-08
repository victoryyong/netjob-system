package com.thsword.netjob.web.controller.admin;

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
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.utils.page.Page;
/**
 * 用户管理

 * @Description用户管理

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class MemberAdmin {
	@Resource(name="memberService")
	MemberService memberService;
	@AuthAnnotation(permissions="admin.member.list")
	@RequestMapping("admin/member/list")
	public void authList(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			JSONObject result = new JSONObject();
			String status = request.getParameter("status");
			String type = request.getParameter("type");
			String name = request.getParameter("name");
			String provinceName = request.getParameter("provinceName");
			String cityName = request.getParameter("cityName");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			map.put("type", type);
			map.put("citycode", citycode);
			map.put("name", name);
			map.put("provinceName", provinceName);
			map.put("cityName", cityName);
			map.put("status", status);
			@SuppressWarnings("unchecked")
			List<Member> members = (List<Member>) memberService.queryPageEntity(IMemberDao.class, map);
			result.put("page", page);
			result.put("list", members);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.member.edit")
	@RequestMapping("admin/member/edit")
	public void changeStatus(HttpServletRequest request,HttpServletResponse response,Member member) throws Exception{
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			if (StringUtils.isEmpty(member.getStatus())) {
				if(member.getStatus()!=Global.SYS_MEMBER_STATUS_1&&member.getStatus()!=Global.SYS_MEMBER_STATUS_2){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "status参数异常", response, request);
					return;
				}
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "status不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setId(member.getId());
			temp.setStatus(member.getStatus());
			memberService.updateEntity(IMemberDao.class, temp);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
