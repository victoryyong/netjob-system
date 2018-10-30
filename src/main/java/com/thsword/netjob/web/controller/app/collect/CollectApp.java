package com.thsword.netjob.web.controller.app.collect;

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
import com.thsword.netjob.dao.ICollectDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Collect;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.CollectService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class CollectApp {
	@Resource(name = "collectService")
	CollectService collectService;
	/**
	 * 
	 * @Description:收藏
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/collect")
	public void fans(HttpServletRequest request, HttpServletResponse response,Collect collect) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(collect.getType())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(collect.getBizId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "业务ID不能为空", response, request);
				return;
			}
			Collect temp = new Collect();
			temp.setMemberId(memberId);
			temp.setBizId(collect.getBizId());
			temp  = (Collect) collectService.queryEntity(ICollectDao.class, temp);
			if(null!=temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿重复收藏", response, request);
				return;
			}
			//会员收藏
			if(collect.getType()==Global.SYS_COLLECT_TYPE_1){
				Member member = (Member) collectService.queryEntityById(IMemberDao.class, collect.getBizId());
				if(null==member){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "收藏对象不存在", response, request);
					return;
				}
				member.setFans(member.getFans()+1);
				collectService.updateEntity(IMemberDao.class, member);
			//服务或者需求收藏
			}else if(collect.getType()==Global.SYS_COLLECT_TYPE_2||collect.getType()==Global.SYS_COLLECT_TYPE_3){
				Serve serve = (Serve) collectService.queryEntityById(IServeDao.class, collect.getBizId());
				if(null==serve){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "收藏对象不存在", response, request);
					return;
				}
			}
			collect.setId(UUIDUtil.get32UUID());
			collect.setMemberId(memberId);
			collect.setCreateBy(memberId);
			collect.setUpdateBy(memberId);
			collectService.addEntity(ICollectDao.class, collect);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

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
	@RequestMapping("app/member/getCollects")
	public void friends(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String type = request.getParameter("type");
			if (StringUtils.isEmpty(type)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<>();
			page.setSort("c.c_createDate");
			map.put("memberId", memberId);
			map.put("type", type);
			map.put("page", page);
			JSONObject obj = new JSONObject();
			if(Integer.parseInt(type)==Global.SYS_COLLECT_TYPE_1){
				List<Member> members = (List<Member>) collectService.queryPageMembers(map);
				obj.put("list", members);
			}else if(Integer.parseInt(type)==Global.SYS_COLLECT_TYPE_2||Integer.parseInt(type)==Global.SYS_COLLECT_TYPE_3){
				List<Serve> serves = (List<Serve>) collectService.queryPageServes(map);
				obj.put("list", serves);
			}
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:是否已收藏
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/isCollected")
	public void addFriend(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String bizId = request.getParameter("bizId");
			if (StringUtils.isEmpty(bizId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "收藏ID不能为空", response, request);
				return;
			}
			JSONObject obj = new JSONObject();
			Collect temp = new Collect();
			temp.setBizId(bizId);
			temp.setMemberId(memberId);
			temp = (Collect) collectService.queryEntity(ICollectDao.class, temp);
			if(null!=temp){
				obj.put("isCollected", true);
			}else{
				obj.put("isCollected", false);
			}
			JsonResponseUtil.successBodyResponse(obj,response, request);
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
	@RequestMapping("app/member/unCollect")
	public void deleteFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String memberId = request.getAttribute("memberId")+"";
		String bizId = request.getParameter("bizId");
		if (StringUtils.isEmpty(bizId)) {
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "收藏ID不能为空", response, request);
			return;
		}
		Collect temp = new Collect();
		temp.setBizId(bizId);
		temp.setMemberId(memberId);
		temp = (Collect) collectService.queryEntity(ICollectDao.class, temp);
		if(null!=temp){
			collectService.deleteEntityById(ICollectDao.class, temp.getId());
			//会员收藏
			if(temp.getType()==Global.SYS_COLLECT_TYPE_1){
				Member member = (Member) collectService.queryEntityById(IMemberDao.class, temp.getBizId());
				member.setFans(member.getFans()-1);
				collectService.updateEntity(IMemberDao.class, member);
			//服务或者需求收藏
			}
		}
		JsonResponseUtil.successCodeResponse(response, request);
	}
}
