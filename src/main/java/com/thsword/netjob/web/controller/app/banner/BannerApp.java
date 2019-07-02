package com.thsword.netjob.web.controller.app.banner;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IBannerDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Banner;
import com.thsword.netjob.service.BannerService;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-BANNER", description = "广告接口")
public class BannerApp {
	@Resource(name = "bannerService")
	BannerService bannerService;

	/**
	 * 
	 * 
	 * @Description:广告
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
	@RequestMapping("app/visitor/banner/list")
	@ApiOperation(value = "广告列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "citycode", value = "城市", dataType = "string", paramType = "query") })
	public JSONObject list(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false) String menuId,
			@RequestParam(required = false) String citycode) throws Exception {
		Page page = new Page(currentPage, pageSize);
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isEmpty(citycode)) {
			map.put("province", "1");
		}
		if (StringUtils.isEmpty(menuId)) {
			map.put("firstMenuId", "1");
		}
		map.put("status", 1);
		map.put("secondMenuId", menuId);
		map.put("citycode", citycode);
		map.put("page", page);
		map.put("status", Global.SYS_AUTH_STATUS_2);
		List<Banner> banners = (List<Banner>) bannerService.queryPageEntity(
				IBannerDao.class, map);
		JSONObject obj = new JSONObject();
		obj.put("page", page);
		if (!StringUtils.isEmpty(citycode) && banners.size() < 8) {
			page.setPageSize(8 - banners.size());
			map.clear();
			map.put("page", page);
			map.put("province", "1");
			List<Banner> countryBanners = (List<Banner>) bannerService
					.queryPageEntity(IBannerDao.class, map);
			banners.addAll(countryBanners);
		}
		obj.put("list", banners);
		return obj;
	}
}
