package com.thsword.netjob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.service.AccountService;
@Service(value = "accountService")
public class AccountServiceImpl extends BaseServiceImpl implements AccountService{
	@Autowired
	private IAccountDao accountDao;
	@Override
	public Account queryAccountByMemberId(String memberId) {
		return accountDao.queryAccountByMemberId(memberId);
	}

}
