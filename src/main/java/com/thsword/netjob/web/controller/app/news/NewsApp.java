package com.thsword.netjob.web.controller.app.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.service.NewsService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.page.Page;

@Controller
public class NewsApp {
	@Resource(name = "newsService")
	NewsService newsService;

	/**
	 * 
	
	 * @Description:头条
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/visitor/news/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String city=request.getParameter("city");
			Map<String, Object> map = new HashMap<>();
			map.put("city", city);
			map.put("page", page);
			List<News> news = (List<News>) newsService.queryPageEntity(INewsDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", news);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
