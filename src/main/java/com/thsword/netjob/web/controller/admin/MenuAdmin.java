package com.thsword.netjob.web.controller.admin;

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

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IMenuDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Menu;
import com.thsword.netjob.service.MenuService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

/**
 * 菜单
 * 
 * @Description:菜单
 * 
 * @author:yong
 * 
 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class MenuAdmin {
	@Resource(name = "menuService")
	MenuService menuService;

	/**
	 * 广告列表
	 * 
	 * @Description:TODO
	 * 
	 * @param request
	 * @param response
	 * @param menu
	 * @throws Exception
	 * 
	 *             void
	 * 
	 * @exception:
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 下午5:03:29
	 */
	@AuthAnnotation(permissions = "admin.menu.list")
	@RequestMapping("admin/menu/list")
	public void list(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("level", Global.SYS_MENU_LEVEL_TYPE_1);
			page.setSort("c_sort");
			page.setDir(Page.DIR_TYPE_ASC);
			map.put("page", page);
			List<Menu> menus = (List<Menu>) menuService.queryPageMenu(map);
			JSONObject result = new JSONObject();
			result.put("list", menus);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 新增菜单
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param menu
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 上午12:46:29
	 */
	@AuthAnnotation(permissions = "admin.menu.add")
	@RequestMapping("admin/menu/add")
	public void add(HttpServletRequest request, HttpServletResponse response, Menu menu) throws Exception {
		try {
			if (StringUtils.isEmpty(menu.getName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "名称不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(menu.getIcon())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "图片不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(menu.getSort())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "排序号不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(menu.getHot())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "是否热门为空", response, request);
				return;
			}
			String userId = request.getAttribute("userId") + "";
			menu.setId(UUIDUtil.get32UUID());
			menu.setCreateBy(userId);
			menu.setUpdateBy(userId);
			menu.dbIcon();
			if(StringUtils.isEmpty(menu.getParentId())){
				menu.setLevel(Global.SYS_MENU_LEVEL_TYPE_1);
			}else{
				menu.setLevel(Global.SYS_MENU_LEVEL_TYPE_2);
			}
			menuService.addEntity(IMenuDao.class, menu);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 编辑菜单
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param menu
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 上午12:45:44
	 */
	@AuthAnnotation(permissions = "admin.menu.edit")
	@RequestMapping("admin/menu/edit")
	public void edit(HttpServletRequest request, HttpServletResponse response, Menu menu) throws Exception {
		try {
			if (StringUtils.isEmpty(menu.getId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空", response, request);
				return;
			}
			menuService.updateEntity(IMenuDao.class, menu);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 删除
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:33:16
	 */
	@RequestMapping("admin/menu/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String menuId = request.getParameter("menuId");
			if (StringUtils.isEmpty(menuId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "menuId不能为空", response, request);
				return;
			}	
			List<Menu> menus = menuService.queryChilds(menuId);
			if(CollectionUtils.isEmpty(menus)){
				JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_HAS_CHILD,response, request);
				return;
			}
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
	@SuppressWarnings("unchecked")
	@RequestMapping("admin/menu/firstMenus")
	public void firstMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Menu menu = new Menu();
			menu.setLevel(Global.SYS_MENU_LEVEL_TYPE_1);
			List<Menu> menus = (List<Menu>) menuService.queryAllEntity(IMenuDao.class, menu);
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
	@RequestMapping("admin/menu/secondMenus")
	public void secondMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String parentId = request.getParameter("parentId");
			if (StringUtils.isEmpty(parentId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "父ID不能为空", response, request);
				return;
			}
			List<Menu> menus = (List<Menu>) menuService.queryChilds(parentId);
			for (Menu temp : menus) {
				temp.setIcon(Global.getSetting(Global.SYSTEM_ADMIN_DOMAIN)+temp.getIcon());
			}
			JSONObject obj = new JSONObject();
			obj.put("list", menus);
			JsonResponseUtil.successBodyResponse(obj,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
