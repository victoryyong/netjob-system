package com.thsword.netjob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IAccountDepositDao;
import com.thsword.netjob.pojo.app.AccountDeposit;
import com.thsword.netjob.service.AccountDepositService;
import com.thsword.netjob.web.exception.ServiceException;
@Service(value = "accountDepositService")
public class AccountDepositServiceImpl extends BaseServiceImpl implements AccountDepositService{
	@Autowired
	IAccountDepositDao  accountDepositDao;
	
	
	@Override
	public boolean checkAccount(String memberId) {
		try {
			AccountDeposit deposit = new AccountDeposit();
			deposit.setMemberId(memberId);
			deposit = (AccountDeposit) accountDepositDao.queryEntity(deposit);
			if(null!=deposit){
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ServiceException("检查保证金出错");
		}
	}
}
