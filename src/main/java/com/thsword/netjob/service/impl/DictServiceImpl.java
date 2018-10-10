package com.thsword.netjob.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.pojo.Dict;
import com.thsword.netjob.service.DictService;
@Service(value = "dictService")
public class DictServiceImpl extends BaseServiceImpl implements DictService{
	@Autowired
	private IDictDao dictDao;
	@Override
	public List<Dict> queryPageRoots(Map<String, Object> map) {
		return dictDao.queryPageRoots(map);
	}
	@Override
	public List<Dict> queryChilds(String id) {
		return dictDao.queryChilds(id);
	}

}
