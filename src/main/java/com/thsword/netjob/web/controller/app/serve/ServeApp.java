package com.thsword.netjob.web.controller.app.serve;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IMenuDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Dict;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Menu;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.pojo.resp.serve.ServeListResp;
import com.thsword.netjob.service.ServeService;
import com.thsword.netjob.util.AmapUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

/**
 * 服务
 * 
 * @Description:TODO
 * 
 * @author:yong
 * 
 * @time:2018年5月10日 下午8:52:49
 */
@RestController
@Api(tags = "NETJOB-SERVE", description = "需求/服务接口")
public class ServeApp {
	@Resource(name = "serveService")
	ServeService serveService;

	/**
	 * 
	 * 
	 * 添加需求
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/serve/add")
	@ApiOperation(value = "添加需求/服务", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "标题", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "longitude", value = "经度", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "latitude", value = "纬度", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "type", value = "类型", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "image", value = "缩略图", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "workType", value = "服务方式", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "payType", value = "付款方式", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "priceType", value = "价格类型", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "price", value = "价格", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "validity", value = "有效期方式", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "serveTime", value = "服务时间", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "menuId", value = "一级菜单ID", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "firstMenuId", value = "二级菜单ID", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "links", value = "文件", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "provinceName", value = "省份名", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "citycode", value = "市区代码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "cityName", value = "市区名", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "detailAddress", value = "详细地址", dataType = "string", paramType = "query", required = true), })
	public BaseResponse add(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String title,
			@RequestParam String longitude, @RequestParam String latitude,
			@RequestParam String type, @RequestParam String image,
			@RequestParam String workType, @RequestParam String payType,
			@RequestParam String priceType, @RequestParam String price,
			@RequestParam String validity, @RequestParam String serveTime,
			@RequestParam String menuId, @RequestParam String firstMenuId,
			@RequestParam String links, @RequestParam String provinceName,
			@RequestParam String citycode, @RequestParam String cityName,
			@RequestParam String area, @RequestParam String detailAddress)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (!type.equals(Global.SYS_MEMBER_SERVE_TYPE_1)
				&& !type.equals(Global.SYS_MEMBER_SERVE_TYPE_2)) {
			throw new ServiceException("类型参数错误");
		}
		if (!workType.equals(Global.SYS_MEMBER_WORK_TYPE_1)
				&& !workType.equals(Global.SYS_MEMBER_WORK_TYPE_2)) {
			throw new ServiceException("服务方式参数错误");
		}
		if (!payType.equals(Global.SYS_MEMBER_PAY_TYPE_1)
				&& !payType.equals(Global.SYS_MEMBER_PAY_TYPE_2)) {
			throw new ServiceException("付款方式参数错误");
		}
		if (priceType.equals(Global.SYS_MEMBER_PRICE_TYPE_1)) {
			if (StringUtils.isEmpty(price)) {
				throw new ServiceException("价格不能为空");
			}
		} else if (!priceType.equals(Global.SYS_MEMBER_PRICE_TYPE_2)) {
			throw new ServiceException("定价方式参数错误");
		}
		Serve serve = new Serve();
		serve.setId(UUIDUtil.get32UUID());
		serve.setMemberId(memberId);
		serve.setCreateBy(memberId);
		serve.setUpdateBy(memberId);
		serveService.addEntity(IServeDao.class, serve);
		try {
			JSONArray linkArray = JSONArray.parseArray(serve.getLinks());
			for (Object object : linkArray) {
				Media media = new Media();
				media.setId(UUIDUtil.get32UUID());
				media.setBizId(serve.getId());
				media.setCreateBy(memberId);
				media.setMemberId(memberId);
				media.setUpdateBy(memberId);
				media.setCitycode(serve.getCitycode());
				media.setLink(object.toString());
				media.setResource(Global.SYS_MEMBER_ACTIVE_RESOURCE_3);
				if (Global.isImage(object.toString())) {
					media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
				} else {
					media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
				}
				serveService.addEntity(IMediaDao.class, media);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseResponse.success();
	}

	/**
	 * 
	 * 
	 * 需求列表
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/list")
	@ApiOperation(value = "需求/服务列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "标题", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "gender", value = "经度", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "menuId", value = "纬度", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "citycode", value = "类型", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "endDate", value = "缩略图", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "startDate", value = "服务方式", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "timeRange", value = "付款方式", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query"), })
	public ServeListResp list(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) String menuId,
			@RequestParam(required = false) String citycode,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String timeRange,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		String memberId = request.getParameter("memberId");
		// 判断类型级别
		Menu menu = (Menu) serveService.queryEntityById(IMenuDao.class, menuId);
		int level = 1;
		if (null != menu) {
			level = menu.getLevel();
		}
		// 查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", Global.SYS_AUTH_STATUS_2);
		String ageId = request.getParameter("ageId");
		if (!StringUtils.isEmpty(ageId)) {
			// 最大年级
			Integer maxAge = 0;
			// 最小年级
			Integer minAge = 0;

			Dict ageDict = (Dict) serveService.queryEntityById(IDictDao.class,
					ageId);
			if (null != ageDict) {
				String ageRange = ageDict.getValue();
				if (!StringUtils.isEmpty(ageRange) && ageRange.indexOf("-") > 0) {
					int index = ageRange.indexOf("-");
					maxAge = Integer.parseInt(ageRange.substring(index + 1));
					minAge = Integer.parseInt(ageRange.substring(0, index));
					map.put("maxAge", maxAge);
					map.put("minAge", minAge);
				}
			}
		}

		String distanceId = request.getParameter("distanceId");
		if (!StringUtils.isEmpty(distanceId)) {
			String longitude = request.getParameter("longitude");
			String latitude = request.getParameter("latitude");
			if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)) {
				throw new ServiceException("经纬度不能为空");

			}
			// 最大经度
			double maxLongitude = 0l;
			// 最大纬度
			double maxLatitude = 0l;
			// 最小经度
			double minLongitude = 0l;
			// 最小纬度
			double minLatitude = 0l;

			Dict disDict = (Dict) serveService.queryEntityById(IDictDao.class,
					distanceId);
			if (null != disDict) {
				int mile = Integer.parseInt(disDict.getValue());
				// {minLat, minLng, maxLat, maxLng};
				double[] disRanges = AmapUtil.getAround(
						Double.parseDouble(latitude),
						Double.parseDouble(longitude), mile * 1000);
				minLatitude = disRanges[0];
				minLongitude = disRanges[1];
				maxLatitude = disRanges[2];
				maxLongitude = disRanges[3];
				map.put("minLatitude", minLatitude);
				map.put("minLongitude", minLongitude);
				map.put("maxLatitude", maxLatitude);
				map.put("maxLongitude", maxLongitude);
			}
		}

		String skillId = request.getParameter("skillId");
		if (!StringUtils.isEmpty(skillId)) {
			// 最大技能评分
			Integer maxSkill = 0;
			// 最小技能评分
			Integer minSkill = 0;

			Dict skillDict = (Dict) serveService.queryEntityById(
					IDictDao.class, skillId);
			if (null != skillDict) {
				String skillRange = skillDict.getValue();
				if (!StringUtils.isEmpty(skillRange)
						&& skillRange.indexOf("-") > 0) {
					int index = skillRange.indexOf("-");
					maxSkill = Integer
							.parseInt(skillRange.substring(index + 1));
					minSkill = Integer.parseInt(skillRange.substring(0, index));
					map.put("maxSkill", maxSkill);
					map.put("minSkill", minSkill);
				}
			}
		}

		String creditId = request.getParameter("creditId");
		if (!StringUtils.isEmpty(creditId)) {
			// 最大诚信评分
			Integer maxCredit = 0;
			// 最小诚信评分
			Integer minCredit = 0;

			Dict creditDict = (Dict) serveService.queryEntityById(
					IDictDao.class, creditId);
			if (null != creditDict) {
				String creditRange = creditDict.getValue();
				if (!StringUtils.isEmpty(creditRange)
						&& creditRange.indexOf("-") > 0) {
					int index = creditRange.indexOf("-");
					maxCredit = Integer.parseInt(creditRange
							.substring(index + 1));
					minCredit = Integer.parseInt(creditRange
							.substring(0, index));
					map.put("maxCredit", maxCredit);
					map.put("minCredit", minCredit);
				}
			}
		}

		if (StringUtils.isEmpty(page.getSort())) {
			page.setSort("t.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
		} else {
			if (page.getSort().equals("c_distance")) {
				String longitude = request.getParameter("longitude");
				String latitude = request.getParameter("latitude");
				if (StringUtils.isEmpty(longitude)
						|| StringUtils.isEmpty(latitude)) {
					throw new ServiceException("经纬度不能为空");
				}
				map.put("longitude", longitude);
				map.put("latitude", latitude);
				map.put("dis_sort", "c_distance");
			}
			page.setSort(page.getSort() + " " + page.getDir()
					+ ",t.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
		}

		if (!StringUtils.isEmpty(timeRange)) {
			map.put("startTime", timeRange.split("-")[0]);
			map.put("endTime", timeRange.split("-")[1]);
		}
		map.put("page", page);
		map.put("type", type);
		map.put("gender", gender);
		if (level == 1) {
			map.put("firstMenuId", menuId);
		} else {
			map.put("menuId", menuId);
		}
		map.put("memberId", memberId);
		map.put("citycode", citycode);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		@SuppressWarnings("unchecked")
		List<Serve> serves = (List<Serve>) serveService.queryPageEntity(
				IServeDao.class, map);
		return ServeListResp.builder().list(serves).page(page).build();
	}

	/**
	 * 
	 * 
	 * 同城推荐
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/recommend")
	@ApiOperation(value = "同城推荐", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "citycode", value = "城市码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query"), })
	public ServeListResp recommend(HttpServletRequest request,
			@RequestParam String citycode,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		page.setSort("t.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("page", page);
		map.put("citycode", citycode);
		@SuppressWarnings("unchecked")
		List<Serve> serves = (List<Serve>) serveService.queryPageEntity(
				IServeDao.class, map);
		return ServeListResp.builder().list(serves).page(page).build();
	}

	/**
	 * 
	 * 点击量
	 * 
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/click")
	@ApiImplicitParams({ @ApiImplicitParam(name = "serveId", value = "需求/服务id", dataType = "string", paramType = "query", required = true), })
	public BaseResponse click(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String serveId)
			throws Exception {
		serveService.addClick(serveId);
		return BaseResponse.success();
	}
}
