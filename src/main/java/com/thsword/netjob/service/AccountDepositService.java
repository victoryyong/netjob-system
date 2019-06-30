package com.thsword.netjob.service;

public interface AccountDepositService extends IBaseService{
	/**
	 * 检查保证金账户
	 * @return
	 */
	public boolean checkAccount(String memberId);
}
