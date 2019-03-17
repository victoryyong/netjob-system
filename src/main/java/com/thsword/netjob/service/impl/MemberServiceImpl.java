package com.thsword.netjob.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.MemberService;
@Service(value = "memberService")
public class MemberServiceImpl extends BaseServiceImpl implements MemberService{
	@Autowired
	private IMemberDao memberDao;
	@Override
	public List<Member> queryPageFriends(Map<String, Object> map) {
		return memberDao.queryPageFriends(map);
	}

	@Override
	public void addFriend(String id, String memberId, String friendId) {
		 memberDao.addFriend(id,memberId,friendId);
		 memberDao.addFriendCount(friendId);
	}
	@Override
	public void deleteFriend(String memberId, String friendId) {
		memberDao.deleteFriend(memberId,friendId);
		memberDao.deleteFriendCount(friendId);
	}

	@Override
	public List<Map<String, Object>> queryPageVisitors(Map<String, Object> map) {
		return memberDao.queryPageVisitors(map);
	}

	@Override
	public void updateVisitor(String id) {
		memberDao.updateVisitor(id);
	}

	@Override
	public void addVisitor(String id, String memberId, String visitorId) {
		memberDao.addVisitor(id,memberId,visitorId);
		memberDao.addVisitorCount(visitorId);
	}

	@Override
	public List<Member> queryPageFamous(Class<IMemberDao> class1, Map<String, Object> map) {
		return memberDao.queryPageFamous(map);
	}

	@Override
	public boolean hasPhoneAuth(String memberId) throws Exception{
		try {
			Member member = (Member) memberDao.queryEntityById(memberId);
			if(null==member){
				throw new Exception("账号不存在");
			}
			return member.getPhoneAuth();
		} catch (Exception e) {
			throw e;
		}
	}
}
