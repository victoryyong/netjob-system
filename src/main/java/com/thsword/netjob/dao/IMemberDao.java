package com.thsword.netjob.dao;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.Role;
import com.thsword.netjob.pojo.app.Member;


/**
 * 
* @ClassName: IUserDao 
* @Description: TODO(用户dao) 
* @author yong
* @date 2017年5月14日 下午1:52:13 
*
 */
public interface IMemberDao extends IBaseDao{
	/**
	 * 删除用户角色
	* @Title: deleteUserRole 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void deleteUserRole(String userId);
	/**
	 * 新增用户角色
	* @Title: addUserRole 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId
	* @param @param roleId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void addUserRole(String id,String userId, String roleId);
	/**
	 * 查询用户角色
	* @Title: queryUserRole 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param userId
	* @param @return    设定文件 
	* @return List<Role>    返回类型 
	* @throws
	 */
	List<Role> queryUserRole(String userId);
	List<Member> queryPageFriends(Map<String, Object> map);
	void addFriend(String id, String memberId, String friendId);
	void addFriendCount(String memberId);
	void deleteFriend(String memberId, String friendId);
	void deleteFriendCount(String memberId);
	List<Map<String, Object>> queryPageVisitors(Map<String, Object> map);
	void updateVisitor(String id);
	void addVisitor(String id, String memberId, String visitorId);
	void addVisitorCount(String memberId);
	List<Map<String, Object>> queryAgrees(Map<String, Object> map);
	void addAgree(String id, String memberId, String agreeId);
	void addAgreeCount(String memberId);
	void unAgree(String memberId, String agreeId);
	void deleteAgreeCount(String memberId);
	List<Member> queryPageFamous(Map<String, Object> map);
}
