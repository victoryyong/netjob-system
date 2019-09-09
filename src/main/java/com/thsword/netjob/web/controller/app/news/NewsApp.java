package com.thsword.netjob.web.controller.app.news;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.pojo.resp.news.NewsListResp;
import com.thsword.netjob.service.NewsService;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-NEWS", description = "头条接口")
public class NewsApp {
	@Resource(name = "newsService")
	NewsService newsService;

	/**
	 * 
	 * 
	 * @Description:头条
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
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "头条列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "city", value = "城市", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10") })
	@RequestMapping("app/visitor/news/list")
	public NewsListResp list(@RequestParam String city,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		Map<String, Object> map = new HashMap<>();
		map.put("city", city);
		map.put("page", page);
		List<News> news = (List<News>) newsService.queryPageEntity(
				INewsDao.class, map);
		return NewsListResp.builder().list(news).page(page).build();
	}
}
