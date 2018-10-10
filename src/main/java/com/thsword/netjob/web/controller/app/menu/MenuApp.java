package com.thsword.netjob.web.controller.app.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IMenuDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Menu;
import com.thsword.netjob.service.MenuService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
/**
 * 菜单

 * @Description:TODO

 * @author:yong

 * @time:2018年5月10日 下午8:52:49
 */
@Controller
public class MenuApp {
	@Resource(name = "menuService")
	MenuService menuService;

	/**
	 * 
	
	 * 个性化定制
	
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/menu/order")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String menuId = request.getParameter("menuId");
			if (StringUtils.isEmpty(menuId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型ID不能为空", response, request);
				return;
			}
			Menu menu = (Menu) menuService.queryEntityById(IMenuDao.class, menuId);
			if(null==menu){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不存在", response, request);
				return;
			}
			if(!menu.getLevel().equals(Global.SYS_MENU_LEVEL_TYPE_2)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response, request);
				return;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("memberId", memberId);
			map.put("menuId", menuId);
			List<Menu> menus = menuService.queryPrivateMenus(map);
			if(null!=menus&&menus.size()>0){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿重复定制", response, request);
				return;
			}
			menuService.orderPrivateMenu(UUIDUtil.get32UUID(),memberId,menuId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 批量化定制
	
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/menu/batchOrder")
	public void orders(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			Map<String, Object> map = new HashMap<>();
			map.put("memberId", memberId);
			List<Menu> menus = menuService.queryPrivateMenus(map);
			if(!CollectionUtils.isEmpty(menus)){
				for (Menu menu : menus) {
					menuService.deletePrivateMenu(memberId,menu.getId());
				}
			}
			String idArray = request.getParameter("idArray");
			JSONArray idJsons = JSONArray.parseArray(idArray);
			for (Object menuJson : idJsons) {
				String menuId = menuJson.toString();
				/*if (StringUtils.isEmpty(menuId)) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, menuId+"-为空", response, request);
					return;
				}*/
				Menu menu = (Menu) menuService.queryEntityById(IMenuDao.class, menuId);
				if(null==menu){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, menuId+"-类型不存在", response, request);
					return;
				}
				if(!menu.getLevel().equals(Global.SYS_MENU_LEVEL_TYPE_2)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, menuId+"-只能收藏二级菜单", response, request);
					return;
				}
				map.clear();
				map.put("memberId", memberId);
				map.put("menuId", menuId);
				/*List<Menu> menus = menuService.queryPrivateMenus(map);
				if(null!=menus&&menus.size()>0){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, menuId+"-重复定制", response, request);
					return;
				}*/
				menuService.orderPrivateMenu(UUIDUtil.get32UUID(),memberId,menuId);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 个性化定制
	
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/menu/deleteOrder")
	public void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String menuId = request.getParameter("menuId");
			if (StringUtils.isEmpty(menuId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型ID不能为空", response, request);
				return;
			}
			menuService.deletePrivateMenu(memberId,menuId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查询个性化定制
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:33:16
	 */
	@RequestMapping("app/member/menu/orders")
	public void privateMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			Map<String, Object> map = new HashMap<>();
			map.put("memberId", memberId);
			List<Menu> menus = menuService.queryPrivateMenus(map);
			JSONObject obj = new JSONObject();
			obj.put("list", menus);
			JsonResponseUtil.successBodyResponse(obj,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查询个性化定制
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:33:16
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/visitor/menu/firstMenus")
	public void firstMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Menu menu = new Menu();
			menu.setLevel(Global.SYS_MENU_LEVEL_TYPE_1);
			List<Menu> menus = (List<Menu>) menuService.queryAllEntity(IMenuDao.class, menu);
			//for (Menu temp : menus) {
			//	temp.setIcon(Global.getSetting(Global.SYSTEM_ADMIN_DOMAIN)+temp.getIcon());
			//}
			JSONObject obj = new JSONObject();
			obj.put("list", menus);
			JsonResponseUtil.successBodyResponse(obj,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查询个性化定制
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:33:16
	 */
	@RequestMapping("app/visitor/menu/secondMenus")
	public void secondMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String parentId = request.getParameter("parentId");
			if (StringUtils.isEmpty(parentId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "父ID不能为空", response, request);
				return;
			}
			List<Menu> menus = (List<Menu>) menuService.queryChilds(parentId);
			//for (Menu temp : menus) {
			//	temp.setIcon(Global.getSetting(Global.SYSTEM_ADMIN_DOMAIN)+temp.getIcon());
			//}
			JSONObject obj = new JSONObject();
			obj.put("list", menus);
			JsonResponseUtil.successBodyResponse(obj,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 查询所有菜单
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:33:16
	 */
	@RequestMapping("app/visitor/menu/allMenus")
	public void menus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			List<Menu> menus = (List<Menu>) menuService.queryAllMenus();
			//for (Menu temp : menus) {
			//	temp.setIcon(Global.getSetting(Global.SYSTEM_ADMIN_DOMAIN)+temp.getIcon());
			//}
			JSONObject obj = new JSONObject();
			obj.put("list", menus);
			JsonResponseUtil.successBodyResponse(obj,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 点击计数
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:33:16
	 */
	@RequestMapping("app/visitor/menu/click")
	public void click(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String menuId = request.getParameter("menuId");
			if (StringUtils.isEmpty(menuId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "menuId不能为空", response, request);
				return;
			}
			menuService.updateClicks(menuId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
