package com.thsword.netjob.service;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.app.Serve;

public interface ServeService extends IBaseService{

	void addClick(String serveId);

	List<Serve> queryPageServe(Map<String, Object> map);
	
}
