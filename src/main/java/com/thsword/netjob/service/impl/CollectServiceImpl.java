package com.thsword.netjob.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.ICollectDao;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.CollectService;
@Service(value = "collectService")
public class CollectServiceImpl extends BaseServiceImpl implements CollectService{
	@Autowired
	private ICollectDao collectDao;
	@Override
	public List<Member> queryPageMembers(Map<String, Object> map) {
		return collectDao.queryPageMembers(map);
	}

	@Override
	public List<Serve> queryPageServes(Map<String, Object> map) {
		return collectDao.queryPageServes(map);
	}

}
