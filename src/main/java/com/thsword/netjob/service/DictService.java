package com.thsword.netjob.service;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.Dict;

public interface DictService extends IBaseService{

	List<Dict> queryPageRoots(Map<String, Object> map);

	List<Dict> queryChilds(String id);

}
