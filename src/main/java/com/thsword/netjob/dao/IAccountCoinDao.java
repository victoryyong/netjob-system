package com.thsword.netjob.dao;

import com.thsword.netjob.pojo.app.Account;

public interface IAccountCoinDao extends IBaseDao{

	Account queryAccountByMemberId(String memberId);

}
