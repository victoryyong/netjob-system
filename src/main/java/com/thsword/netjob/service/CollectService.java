package com.thsword.netjob.service;

import java.util.List;
import java.util.Map;

import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Serve;

public interface CollectService extends IBaseService{

	List<Member> queryPageMembers(Map<String, Object> map);

	List<Serve> queryPageServes(Map<String, Object> map);

}
