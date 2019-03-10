package com.thsword.netjob.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thsword.netjob.dao.IAccountCoinDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.ICoinRecordDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.CoinRecord;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.service.AccountService;
import com.thsword.utils.object.UUIDUtil;
@Service(value = "accountService")
public class AccountServiceImpl extends BaseServiceImpl implements AccountService{
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private IAccountCoinDao accounCointDao;
	@Autowired
	private INewsDao newsDao;
	@Autowired
	private ICashRecordDao cashRecordDao;
	@Autowired
	private ICoinRecordDao coinRecordDao;
	@Autowired
	private IMemberDao memberDao;
	@Override
	public Account queryAccountByMemberId(String memberId) {
		return accountDao.queryAccountByMemberId(memberId);
	}
	@Transactional
	@Override
	public void rechargeAccount(String memberId, String out_trade_no,
			BigDecimal money) throws Exception{
		Member member = (Member) memberDao.queryEntityById(memberId);
		Account ac = new Account();
		ac.setMemberId(memberId);
		ac = (Account) accountDao.queryEntity(ac);
		if(null==ac){
			ac=new Account();
			ac.setMoney(new BigDecimal(5));
			ac.setCreateBy(memberId);
			ac.setUpdateBy(memberId);
			ac.setId(UUIDUtil.get32UUID());
			ac.setMemberId(memberId);
			accountDao.addEntity(ac);
		}else{
			ac.setMoney(ac.getMoney().add(money));
			ac.setUpdateBy(memberId);
			ac.setMemberId(memberId);
			accountDao.updateEntity(ac);
		}
		//添加动态
		News news = new News();
		news.setId(UUIDUtil.get32UUID());
		news.setMemberId(memberId);
		news.setType(Global.SYS_MEMBER_NEWS_TYPE_2);
		news.setCitycode(member.getCitycode());
		news.setContent("用户 "+member.getName()+" 充值"+money+"元成功");
		news.setCreateBy(member.getId());
		news.setUpdateBy(member.getId());
		newsDao.addEntity(news);
		//添加交易记录
		CashRecord record = new CashRecord();
		record.setId(UUIDUtil.get32UUID());
		record.setMemberId(memberId);
		record.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_1);
		record.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
		record.setCitycode(member.getCitycode());
		record.setIncome(money);
		record.setFlowId(out_trade_no);
		record.setCitycode(member.getCitycode());
		record.setCreateBy(member.getId());
		record.setUpdateBy(member.getId());
		record.setTargetId(memberId);
		record.setTargetName(member.getName());
		record.setResourceId(memberId);
		record.setResourceName(member.getName());
		cashRecordDao.addEntity(record);
		
	}
	
	@Transactional
	@Override
	public void rechargeAccountCoin(String memberId,BigDecimal money) throws Exception{
		Member member = (Member) memberDao.queryEntityById(memberId);
		//扣减账户余额
		Account ac = new Account();
		ac.setMemberId(memberId);
		ac = (Account) accountDao.queryEntity(ac);
		ac.setMoney(ac.getMoney().subtract(money));
		accountDao.updateEntity(ac);
		
		//新增网币
		AccountCoin acc = new AccountCoin();
		acc.setMemberId(memberId);
		acc = (AccountCoin) accounCointDao.queryEntity(acc);
		long num = 0;
		num = money.multiply(new BigDecimal(Global.SYS_MEMBER_MONEY_COIN_RATE)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
		if(null==acc){
			acc=new AccountCoin();
			acc.setNum(num);
			acc.setCreateBy(memberId);
			acc.setUpdateBy(memberId);
			acc.setId(UUIDUtil.get32UUID());
			acc.setMemberId(memberId);
			accounCointDao.addEntity(acc);
		}else{
			acc.setNum(acc.getNum()+num);
			acc.setUpdateBy(memberId);
			acc.setMemberId(memberId);
			accounCointDao.updateEntity(acc);
		}
		String flowId = UUIDUtil.get32FlowID();
		//添加交易记录(网币)
		CoinRecord recordCoin = new CoinRecord();
		recordCoin.setId(UUIDUtil.get32UUID());
		recordCoin.setMemberId(memberId);
		recordCoin.setTargetId(memberId);
		recordCoin.setTargetName(member.getName());
		recordCoin.setResourceId(memberId);
		recordCoin.setResourceName(member.getName());
		recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_1);
		recordCoin.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
		recordCoin.setCitycode(member.getCitycode());
		recordCoin.setIncome(num);
		recordCoin.setFlowId(UUIDUtil.get32FlowID());
		recordCoin.setCitycode(member.getCitycode());
		recordCoin.setCreateBy(member.getId());
		recordCoin.setUpdateBy(member.getId());
		coinRecordDao.addEntity(recordCoin);
		
		//添加交易记录(现金)
		CashRecord record = new CashRecord();
		record.setId(UUIDUtil.get32UUID());
		record.setMemberId(memberId);
		record.setTargetId(memberId);
		record.setTargetName(member.getName());
		record.setResourceId(memberId);
		record.setResourceName(member.getName());
		record.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_5);
		record.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_2);
		record.setCitycode(member.getCitycode());
		record.setOutcome(money);
		record.setFlowId(flowId);
		record.setCitycode(member.getCitycode());
		record.setCreateBy(member.getId());
		record.setUpdateBy(member.getId());
		cashRecordDao.addEntity(record);
	}

}
