package com.thsword.netjob.dao;

import com.thsword.netjob.pojo.app.Account;

public interface IAccountDao extends IBaseDao{

	Account queryAccountByMemberId(String memberId);

	void updatePassword(Account acc);
	
	Account queryPwdMember(String memberId);

}
