package com.thsword.netjob.web.controller.app.banner;

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
import com.thsword.netjob.dao.IBannerDao;
import com.thsword.netjob.pojo.app.Banner;
import com.thsword.netjob.service.BannerService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.page.Page;

@Controller
public class BannerApp {
	@Resource(name = "bannerService")
	BannerService bannerService;

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
	@RequestMapping("app/visitor/banner/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String menuId = request.getParameter("menuId");
			/*if(StringUtils.isEmpty(menuId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "menuId不能为空", response, request);
				return;
			}*/
			String city=request.getParameter("citycode");
			Map<String, Object> map = new HashMap<>();
			if(StringUtils.isEmpty(city)){
				map.put("province", "1");
			}
			if(StringUtils.isEmpty(menuId)){
				map.put("firstMenuId", "1");
			}
			map.put("status", 1);
			map.put("secondMenuId", menuId);
			map.put("citycode", city);
			map.put("page", page);
			List<Banner> banners = (List<Banner>) bannerService.queryPageEntity(IBannerDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", banners);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
