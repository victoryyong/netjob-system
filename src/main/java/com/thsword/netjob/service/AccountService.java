package com.thsword.netjob.service;

import com.thsword.netjob.pojo.app.Account;

public interface AccountService extends IBaseService{

	Account queryAccountByMemberId(String memberId);

}
