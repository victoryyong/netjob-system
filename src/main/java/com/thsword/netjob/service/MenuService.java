package com.thsword.netjob.service;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.app.Menu;

public interface MenuService extends IBaseService{
	/**
	 * 查询菜单 级联子菜单
	
	 * @Description:TODO
	
	 * @param map
	 * @return
	
	 * List<Menu>
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月9日 上午12:33:32
	 */
	List<Menu> queryPageMenu(Map<String, Object> map);
	/**
	 * 查询个性化定制
	
	 * @Description:TODO
	
	 * @param memberId
	 * @param menuId
	 * @return
	
	 * List<Menu>
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午9:06:16
	 */
	List<Menu> queryPrivateMenus(Map<String,Object> map);
	/**
	 * 个性化定制
	
	 * @Description:TODO
	
	 * @param memberId
	 * @param menuId
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	 * @param menuId2 
	
	 * @time:2018年5月10日 下午9:06:37
	 */
	void orderPrivateMenu(String id,String memberId, String menuId);
	/**
	 * 查询子类型
	
	 * @Description:TODO
	
	 * @param parentId
	 * @return
	
	 * List<Menu>
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月10日 下午10:35:22
	 */
	List<Menu> queryChilds(String parentId);
	/**
	 * 更新点击次数
	
	 * @Description:TODO
	
	 * @param menuId
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月11日 下午4:50:29
	 */
	void updateClicks(String menuId);
	
	
	/**
	 * 查询所有
	
	 * @Description:TODO
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月11日 下午4:50:29
	 */
	List<Menu> queryAllMenus();
	/**
	 * 取消个性化定制
	
	 * @Description:TODO
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月11日 下午4:50:29
	 */
	void deletePrivateMenu(String memberId, String menuId);

}
