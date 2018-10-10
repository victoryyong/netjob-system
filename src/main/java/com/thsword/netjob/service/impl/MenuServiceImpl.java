package com.thsword.netjob.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IMenuDao;
import com.thsword.netjob.pojo.app.Menu;
import com.thsword.netjob.service.MenuService;
@Service(value = "menuService")
public class MenuServiceImpl extends BaseServiceImpl implements MenuService{
	@Autowired
	private IMenuDao menuDao;
	@Override
	public List<Menu> queryPageMenu(Map<String, Object> map) {
		return menuDao.queryPageMenu(map);
	}
	@Override
	public List<Menu> queryPrivateMenus(Map<String, Object> map) {
		return menuDao.queryPrivateMenus(map);
	}
	@Override
	public void orderPrivateMenu(String id,String memberId, String menuId) {
		menuDao.orderPrivateMenu(id,memberId, menuId);
	}
	@Override
	public List<Menu> queryChilds(String parentId) {
		return menuDao.queryChilds(parentId);
	}
	@Override
	public void updateClicks(String menuId) {
		 menuDao.updateClicks(menuId);
	}
	@Override
	public List<Menu> queryAllMenus() {
		return menuDao.queryAllMenus();
	}
	@Override
	public void deletePrivateMenu(String memberId, String menuId) {
		menuDao.deletePrivateMenu(memberId,menuId);
	}

}
