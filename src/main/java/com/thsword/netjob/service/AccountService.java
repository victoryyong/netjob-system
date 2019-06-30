package com.thsword.netjob.service;

import com.thsword.netjob.pojo.app.Account;

public interface AccountService extends IBaseService{

	Account queryAccountByMemberId(String memberId);
	/**
	 * 充值现金账户
	 * @param out_trade_no
	 * @throws Exception
	 */
	void rechargeAccount(String out_trade_no);
	/**
	 * 充值网币账户
	 * @param out_trade_no
	 * @throws Exception
	 */
	void rechargeAccountCoin(String out_trade_no);
	/**
	 * 充值保证金
	 * @param out_trade_no
	 * @throws Exception
	 */
	void rechargeAccountDeposit(String out_trade_no);
	/**
	 * 兑换网币
	 * @param out_trade_no
	 * @throws Exception
	 */
	void swapAccountCoin(String out_trade_no);
	/**
	 * 打赏网币
	 * @param out_trade_no
	 * @throws Exception
	 */
	void reward(String out_trade_no);
	/**
	 * 更新密码
	 * @param memberId
	 * @param password
	 */
	void updatePassword(Account acc);
	/**
	 * 查询账户
	 * @param memberId
	 */
	Account queryPwdMember(String memberId) throws Exception;
	
}
