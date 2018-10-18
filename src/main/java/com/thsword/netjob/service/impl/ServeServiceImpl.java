package com.thsword.netjob.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.ServeService;
@Service(value = "serveService")
public class ServeServiceImpl extends BaseServiceImpl implements ServeService{
	@Autowired
	IServeDao serveDao;
	@Override
	public void addClick(String serveId) {
		serveDao.addClick(serveId);
	}
	@Override
	public List<Serve> queryPageServe(Map<String, Object> map) {
		return serveDao.queryPageServe(map);
	}

}
