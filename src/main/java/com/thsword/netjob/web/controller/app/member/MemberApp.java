package com.thsword.netjob.web.controller.app.member;

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
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
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
	@RequestMapping("app/member/info")
	public void visit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String memberId = (String) request.getAttribute("memberId");
		if(StringUtils.isEmpty(memberId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "memberId不能为空", response, request);
			return;
		}
		Member member = (Member) memberService.queryEntityById(IMemberDao.class, memberId);
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
			Map<String, Object> map = new HashMap<>();
			map.put("citycode", citycode);
			map.put("page", page);
			List<Member> members = (List<Member>) memberService.queryPageFamous(IMemberDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", members);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:修改手机
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/changePhone")
	public void changePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String phone=request.getParameter("phone");
			Member member = (Member) memberService.queryEntityById(IMemberDao.class, memberId);
			if(null==member){
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER, response, request);
				return;
			}
			if(StringUtils.isEmpty(phone)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"phone不能为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setId(memberId);
			temp.setPhone(phone);
			memberService.updateEntity(IMemberDao.class, temp);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:修改昵称
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/changeName")
	public void changeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String name=request.getParameter("name");
			Member member = (Member) memberService.queryEntityById(IMemberDao.class, memberId);
			if(null==member){
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER, response, request);
				return;
			}
			if(StringUtils.isEmpty(name)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"name不能为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setId(memberId);
			temp.setName(name);
			memberService.updateEntity(IMemberDao.class, temp);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:修改图像
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/changeImage")
	public void changeImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String image=request.getParameter("image");
			Member member = (Member) memberService.queryEntityById(IMemberDao.class, memberId);
			if(null==member){
				JsonResponseUtil.codeResponse(ErrorUtil.LOGIN_ERROR_USER, response, request);
				return;
			}
			if(StringUtils.isEmpty(image)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"image不能为空", response, request);
				return;
			}
			Member temp = new Member();
			temp.setId(memberId);
			temp.setImage(image);
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
			if (StringUtils.isEmpty(member.getPhone())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "电话不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(member.getPassword())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
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
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
