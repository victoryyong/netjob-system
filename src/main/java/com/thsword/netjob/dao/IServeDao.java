package com.thsword.netjob.dao;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.app.Serve;

public interface IServeDao extends IBaseDao{
	public void addClick(String id);

	public List<Serve> queryPageServe(Map<String, Object> map);
}
