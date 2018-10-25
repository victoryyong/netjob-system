package com.thsword.netjob.service;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.pojo.app.Member;

public interface MemberService extends IBaseService{
	/**
	 * 查询好友
	
	 * @Description:TODO
	
	 * @param class1
	 * @param map
	 * @return
	
	 * List<Member>
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月12日 下午9:14:11
	 */
	List<Member> queryPageFriends(Map<String, Object> map);
	/**
	 * 添加好友
	
	 * @Description:TODO
	
	 * @param id
	 * @param memberId
	 * @param friendId
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 下午1:48:36
	 */
	void addFriend(String id, String memberId, String friendId);
	/**
	 * 取消收藏
	
	 * @Description:TODO
	
	 * @param memberId
	 * @param friendId
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月16日 下午3:43:18
	 */
	void deleteFriend(String memberId, String friendId);
	/**
	 * 查询访客
	
	 * @Description:TODO
	
	 * @param map
	 * @return
	
	 * List<Member>
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 下午1:48:48
	 */
	List<Map<String, Object>> queryPageVisitors(Map<String, Object> map);
	/**
	 * 更新访客记录
	
	 * @Description:TODO
	
	 * @param map
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 下午2:14:59
	 */
	void updateVisitor(String id);
	/**
	 * 添加访客记录
	
	 * @Description:TODO
	
	 * @param id
	 * @param memberId
	 * @param visitorId
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月14日 下午2:17:57
	 */
	void addVisitor(String id, String memberId, String visitorId);
	
	/**
	 * 同城大牌
	
	 * @Description:TODO
	
	 * @param class1
	 * @param map
	 * @return
	
	 * List<Member>
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年9月6日 下午9:27:09
	 */
	List<Member> queryPageFamous(Class<IMemberDao> class1, Map<String, Object> map);
	

}
