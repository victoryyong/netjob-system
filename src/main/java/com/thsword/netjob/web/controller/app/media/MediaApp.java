package com.thsword.netjob.web.controller.app.media;

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
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.page.Page;

@Controller
public class MediaApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 
	
	 * @Description:广告
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/visitor/index/videos")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String userId = request.getAttribute("userId")+"";
			String longitude = request.getParameter("longitude");
			String latitude = request.getParameter("latitude");
			if(!StringUtils.isEmpty(latitude)&&!StringUtils.isEmpty(latitude)){
				Member member = new Member();
				member.setId(userId);
				member.setLatitude(latitude);
				member.setLongitude(longitude);
				memberService.updateEntity(IMemberDao.class, member);
			};
			Map<String, Object> map = new HashMap<>();
			String citycode = request.getParameter("citycode");
			map.put("page", page);
			map.put("citycode", citycode);
			map.put("type", Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
			List<Media> medias = (List<Media>) memberService.queryPageEntity(IMediaDao.class, map);
			if(StringUtils.isEmpty(medias)){
				map.remove("citycode");
				medias = (List<Media>) memberService.queryPageEntity(IMediaDao.class, map);
			}
			JSONObject obj = new JSONObject();
			obj.put("list", medias);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
