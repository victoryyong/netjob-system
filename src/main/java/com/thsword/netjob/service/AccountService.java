package com.thsword.netjob.service;

import java.math.BigDecimal;

import com.thsword.netjob.pojo.app.Account;

public interface AccountService extends IBaseService{

	Account queryAccountByMemberId(String memberId);

	void rechargeAccount(String memberId, String out_trade_no, BigDecimal money) throws Exception;
	
	void rechargeAccountCoin(String memberId,BigDecimal money) throws Exception;
}
