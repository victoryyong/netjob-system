package com.thsword.netjob.web.controller.app.menu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IMenuDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Menu;
import com.thsword.netjob.pojo.resp.menu.MenuListResp;
import com.thsword.netjob.service.MenuService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

/**
 * 菜单
 * 
 * @Description:TODO
 * 
 * @author:yong
 * 
 * @time:2018年5月10日 下午8:52:49
 */
@RestController
@Api(tags = "NETJOB-MENU", description = "菜单接口")
public class MenuApp {
	@Resource(name = "menuService")
	MenuService menuService;

	/**
	 * 个性化定制
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/menu/order")
	@ApiOperation(value = "个性化定制", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "string", paramType = "query", required = true) })
	public BaseResponse list(HttpServletRequest request,
			@RequestParam String menuId) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Menu menu = (Menu) menuService.queryEntityById(IMenuDao.class, menuId);
		if (null == menu) {
			throw new ServiceException("类型不存在");
		}
		if (!menu.getLevel().equals(Global.SYS_MENU_LEVEL_TYPE_2)) {
			throw new ServiceException(ErrorUtil.REQUEST_INVALID_PARAM);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("menuId", menuId);
		List<Menu> menus = menuService.queryPrivateMenus(map);
		if (null != menus && menus.size() > 0) {
			throw new ServiceException("请勿重复定制");

		}
		menuService.orderPrivateMenu(UUIDUtil.get32UUID(), memberId, menuId);
		return BaseResponse.success();
	}

	/**
	 * 批量化定制
	 * @time:2018年5月8日 上午12:07:45
	 */
	@ApiOperation(value = "个性化定制", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "idArray", value = "菜单ID数组", dataType = "string", paramType = "query", required = true) })
	@RequestMapping("app/member/menu/batchOrder")
	public BaseResponse orders(HttpServletRequest request,
			@RequestParam String idArray) {
		String memberId = request.getAttribute("memberId") + "";
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		List<Menu> menus = menuService.queryPrivateMenus(map);
		if (!CollectionUtils.isEmpty(menus)) {
			for (Menu menu : menus) {
				menuService.deletePrivateMenu(memberId, menu.getId());
			}
		}
		JSONArray idJsons = JSONArray.parseArray(idArray);
		for (Object menuJson : idJsons) {
			String menuId = menuJson.toString();
			Menu menu = (Menu) menuService.queryEntityById(IMenuDao.class,
					menuId);
			if (null == menu) {
				throw new ServiceException(menuId + "-类型不存在");
			}
			if (!menu.getLevel().equals(Global.SYS_MENU_LEVEL_TYPE_2)) {
				throw new ServiceException(menuId + "-只能收藏二级菜单");
			}
			map.clear();
			map.put("memberId", memberId);
			map.put("menuId", menuId);
			menuService
					.orderPrivateMenu(UUIDUtil.get32UUID(), memberId, menuId);
		}
		return BaseResponse.success();
	}

	/**
	 * 删除定制
	 * @time:2018年5月8日 上午12:07:45
	 */
	@ApiOperation(value = "删除定制", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "string", paramType = "query",required = true) })
	@RequestMapping("app/member/menu/deleteOrder")
	public BaseResponse deleteOrder(HttpServletRequest request,
			HttpServletResponse response,@RequestParam String menuId) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (StringUtils.isEmpty(menuId)) {
			throw new ServiceException("类型ID不能为空");
		}
		menuService.deletePrivateMenu(memberId, menuId);
		return BaseResponse.success();
	}

	/**
	 * 默认菜单
	 * 
	 * @time:2018年5月10日 下午9:33:16
	 */
	@ApiOperation(value = "默认菜单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "city", value = "城市", dataType = "string", paramType = "query",required = true),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10") })
	@RequestMapping("app/visitor/menu/defaults")
	public MenuListResp defaults(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		String userId = request.getAttribute("memberId") + "";
		Page page = new Page(currentPage, pageSize);
		page.setSort("c_clicks");
		page.setDir(Page.DIR_TYPE_DESC);
		page.setPageSize(9);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		@SuppressWarnings("unchecked")
		List<Menu> menus = (List<Menu>) menuService.queryPageEntity(
				IMenuDao.class, map);

		Map<String, Object> orderMap = new HashMap<>();
		orderMap.put("memberId", userId);
		List<Menu> orderMenus = menuService.queryPrivateMenus(orderMap);

		List<Menu> results = new ArrayList<Menu>();
		int i = 0;
		if (!CollectionUtils.isEmpty(orderMenus)) {
			i = orderMenus.size();
			results.addAll(orderMenus);
			for (Menu menu : menus) {
				if (i == 9) {
					break;
				}
				boolean find = false;
				for (Menu orderMenu : orderMenus) {
					if (orderMenu.getId().equals(menu.getId())) {
						find = true;
						break;
					}
				}
				if (!find) {
					results.add(menu);
					i++;
				}
			}
		} else {
			results = menus;
		}
		return MenuListResp.builder().list(menus).build();
	}

	/**
	 * 查询个性化定制
	 * @time:2018年5月10日 下午9:33:16
	 */
	@ApiOperation(value = "查询个性化定制", httpMethod = "POST")
	@RequestMapping("app/member/menu/orders")
	public MenuListResp privateMenus(HttpServletRequest request) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		List<Menu> menus = menuService.queryPrivateMenus(map);
		return MenuListResp.builder().list(menus).build();
	}

	/**
	 * 查询个性化定制
	 * @time:2018年5月10日 下午9:33:16
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "查询一级菜单列表", httpMethod = "POST")
	@RequestMapping("app/visitor/menu/firstMenus")
	public void firstMenus(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Menu menu = new Menu();
			menu.setLevel(Global.SYS_MENU_LEVEL_TYPE_1);
			List<Menu> menus = (List<Menu>) menuService.queryAllEntity(
					IMenuDao.class, menu);
			JSONObject obj = new JSONObject();
			obj.put("list", menus);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 查询个性化定制
	 * @time:2018年5月10日 下午9:33:16
	 */
	@ApiOperation(value = "查询二级菜单列表", httpMethod = "POST")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "parentId", value = "当前页", dataType = "int", paramType = "query",required = true)})
	@RequestMapping("app/visitor/menu/secondMenus")
	public MenuListResp secondMenus(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String parentId)
			throws Exception {
		List<Menu> menus = (List<Menu>) menuService.queryChilds(parentId);
		return MenuListResp.builder().list(menus).build();
	}

	/**
	 * 查询所有菜单
	 * @time:2018年5月10日 下午9:33:16
	 */
	@ApiOperation(value = "查询所有菜单", httpMethod = "POST")
	@RequestMapping("app/visitor/menu/allMenus")
	public MenuListResp menus() {
		List<Menu> menus = (List<Menu>) menuService.queryAllMenus();
		return MenuListResp.builder().list(menus).build();
	}

	/**
	 * 点击计数
	 * @time:2018年5月10日 下午9:33:16
	 */
	@ApiOperation(value = "查询所有菜单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "string", paramType = "query",required = true) })
	@RequestMapping("app/visitor/menu/click")
	public BaseResponse click(@RequestParam String menuId) throws Exception {
		menuService.updateClicks(menuId);
		return BaseResponse.success();
	}

}
