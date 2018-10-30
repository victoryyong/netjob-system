package com.thsword.netjob.web.controller.app.active;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IActiveDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Active;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.service.ActiveService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class ActiveApp {
	@Resource(name = "activeService")
	ActiveService activeService;

	
	@RequestMapping("app/member/active/add")
	public void add(HttpServletRequest request, HttpServletResponse response,Active active) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String citycode = request.getAttribute("citycode")+"";
			if (StringUtils.isEmpty(active.getTitle())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "标题不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(active.getLinks())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "文件不能为空", response, request);
				return;
			}
			active.setId(UUIDUtil.get32UUID());
			active.setMemberId(memberId);
			active.setCreateBy(memberId);
			active.setUpdateBy(memberId);
			activeService.addEntity(IActiveDao.class, active);
			try {
				JSONArray linkArray = JSONArray.parseArray(active.getLinks());
				for (Object object : linkArray) {
					Media media = new Media();
					media.setId(UUIDUtil.get32UUID());
					media.setBizId(active.getId());
					media.setCreateBy(memberId);
					media.setMemberId(memberId);
					media.setUpdateBy(memberId);
					media.setCitycode(citycode);
					media.setLink(object.toString());
					media.setResource(Global.SYS_MEMBER_ACTIVE_RESOURCE_1);
					if(Global.isImage(object.toString())){
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
					}else{
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
					}
					activeService.addEntity(IMediaDao.class, media);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("app/visitor/active/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getParameter("memberId");
			if(StringUtils.isEmpty(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户ID不能为空", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("memberId", memberId);
			map.put("page", page);
			@SuppressWarnings("unchecked")
			List<Active> actives = (List<Active>) activeService.queryPageEntity(IActiveDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("page", page);
			obj.put("list", actives);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
