package com.thsword.netjob.dao;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.app.Menu;

public interface IMenuDao extends IBaseDao{

	List<Menu> queryPageMenu(Map<String, Object> map);
	
	List<Menu> queryChilds(String menuId);

	List<Menu> queryPrivateMenus(Map<String, Object> map);

	void orderPrivateMenu(String id,String memberId, String menuId);

	void updateClicks(String menuId);

	List<Menu> queryAllMenus();

	void deletePrivateMenu(String memberId, String menuId);
}
