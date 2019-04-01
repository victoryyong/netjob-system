package com.thsword.netjob.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thsword.netjob.dao.IAccountCoinDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.dao.ICoinRecordDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.dao.IOrderAccountDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.CoinRecord;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.pojo.app.OrderAccount;
import com.thsword.netjob.service.AccountService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
@Service(value = "accountService")
public class AccountServiceImpl extends BaseServiceImpl implements AccountService{
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private IAccountCoinDao accountCoinDao;
	@Autowired
	private INewsDao newsDao;
	@Autowired
	private ICashRecordDao cashRecordDao;
	@Autowired
	private ICoinRecordDao coinRecordDao;
	@Autowired
	private IMemberDao memberDao;
	@Autowired
	private IOrderAccountDao orderAccountDao;
	@Override
	public Account queryAccountByMemberId(String memberId) {
		return accountDao.queryAccountByMemberId(memberId);
	}
	@Transactional
	@Override
	public void rechargeAccount(String out_trade_no) throws Exception{
		OrderAccount order = new OrderAccount();
		order.setTradeNo(out_trade_no);
		order = (OrderAccount) orderAccountDao.queryEntity(order);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(order.getStatus()!=Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1){
			throw new ServiceException("订单已关闭");
		}
		String memberId = order.getMemberId();
		BigDecimal money= order.getTotal_fee();
		Member member = (Member) memberDao.queryEntityById(order.getMemberId());
		Account ac = new Account();
		ac.setMemberId(memberId);
		ac = (Account) accountDao.queryEntity(ac);
		if(null==ac){
			ac=new Account();
			ac.setMoney(money);
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
		record.setTradeNo(out_trade_no);
		record.setOrderId(order.getId());
		record.setCitycode(member.getCitycode());
		record.setCreateBy(member.getId());
		record.setUpdateBy(member.getId());
		record.setTargetId(memberId);
		record.setTargetName(member.getName());
		cashRecordDao.addEntity(record);
		
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2);
		orderAccountDao.updateEntity(order);
	}
	
	@Transactional
	@Override
	public void rechargeAccountCoin(String tradeNo) throws Exception{
		OrderAccount order = new OrderAccount();
		order.setTradeNo(tradeNo);
		order = (OrderAccount) orderAccountDao.queryEntity(order);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(order.getStatus()!=Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1){
			throw new ServiceException("订单已关闭");
		}
		String memberId = order.getMemberId();
		BigDecimal money= order.getTotal_fee();
		Member member = (Member) memberDao.queryEntityById(memberId);
		AccountCoin addAcc = new AccountCoin();
		addAcc.setMemberId(memberId);
		addAcc = (AccountCoin) accountCoinDao.queryEntity(addAcc);
		long numFormat = money.multiply(new BigDecimal(Global.getSetting(Global.SYS_MEMBER_MONEY_COIN_RATE))).setScale(0, BigDecimal.ROUND_DOWN).longValue();;
		if(null==addAcc){
			addAcc=new AccountCoin();
			addAcc.setNum(numFormat);
			addAcc.setCreateBy(memberId);
			addAcc.setUpdateBy(memberId);
			addAcc.setId(UUIDUtil.get32UUID());
			addAcc.setMemberId(memberId);
			accountCoinDao.addEntity(addAcc);
		}else{
			addAcc.setNum(addAcc.getNum()+numFormat);
			addAcc.setUpdateBy(memberId);
			addAcc.setMemberId(memberId);
			accountCoinDao.updateEntity(addAcc);
		}
		//添加动态
		News news = new News();
		news.setId(UUIDUtil.get32UUID());
		news.setMemberId(memberId);
		news.setType(Global.SYS_MEMBER_NEWS_TYPE_2);
		news.setCitycode(member.getCitycode());
		news.setContent("用户 "+member.getName()+" 充值"+numFormat+"网币成功");
		news.setCreateBy(member.getId());
		news.setUpdateBy(member.getId());
		newsDao.addEntity(news);
		//添加交易记录
		CoinRecord recordCoin = new CoinRecord();
		recordCoin.setId(UUIDUtil.get32UUID());
		recordCoin.setMemberId(memberId);
		//recordCoin.setTargetId(memberId);
		//recordCoin.setTargetName(member.getName());
		recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_1);
		recordCoin.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
		recordCoin.setCitycode(member.getCitycode());
		recordCoin.setIncome(numFormat);
		recordCoin.setTradeNo(tradeNo);
		recordCoin.setOrderId(order.getId());
		recordCoin.setTradeNo(tradeNo);
		recordCoin.setCitycode(member.getCitycode());
		recordCoin.setCreateBy(member.getId());
		recordCoin.setUpdateBy(member.getId());
		coinRecordDao.addEntity(recordCoin);
		
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2);
		orderAccountDao.updateEntity(order);
	}
	
	@Transactional
	@Override
	public void swapAccountCoin(String tradeNo) throws Exception{
		OrderAccount order = new OrderAccount();
		order.setTradeNo(tradeNo);
		order = (OrderAccount) orderAccountDao.queryEntity(order);
		String memberId = order.getMemberId();
		BigDecimal money= order.getTotal_fee();
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(order.getStatus()!=Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1){
			throw new ServiceException("订单已关闭");
		}
		Member member = (Member) memberDao.queryEntityById(memberId);
		//扣减账户余额
		Account ac = new Account();
		ac.setMemberId(memberId);
		ac = (Account) accountDao.queryEntity(ac);
		if(ac==null){
			throw new ServiceException("账户不存在");
		}
		if(order.getTotal_fee().compareTo(ac.getMoney())>0){
			throw new ServiceException("账户余额不足");
		}
		ac.setMoney(ac.getMoney().subtract(money));
		accountDao.updateEntity(ac);
		
		//新增网币
		AccountCoin acc = new AccountCoin();
		acc.setMemberId(memberId);
		acc = (AccountCoin) accountCoinDao.queryEntity(acc);
		long num = 0;
		num = money.multiply(new BigDecimal(Global.getSetting(Global.SYS_MEMBER_MONEY_COIN_RATE+""))).setScale(0, BigDecimal.ROUND_DOWN).longValue();
		if(null==acc){
			acc=new AccountCoin();
			acc.setNum(num);
			acc.setCreateBy(memberId);
			acc.setUpdateBy(memberId);
			acc.setId(UUIDUtil.get32UUID());
			acc.setMemberId(memberId);
			accountCoinDao.addEntity(acc);
		}else{
			acc.setNum(acc.getNum()+num);
			acc.setUpdateBy(memberId);
			acc.setMemberId(memberId);
			accountCoinDao.updateEntity(acc);
		}
		//添加交易记录(网币)
		CoinRecord recordCoin = new CoinRecord();
		recordCoin.setId(UUIDUtil.get32UUID());
		recordCoin.setMemberId(memberId);
		recordCoin.setTargetId(memberId);
		recordCoin.setTargetName(member.getName());
		recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_2);
		recordCoin.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
		recordCoin.setCitycode(member.getCitycode());
		recordCoin.setIncome(num);
		recordCoin.setOrderId(order.getId());
		recordCoin.setTradeNo(tradeNo);
		recordCoin.setCitycode(member.getCitycode());
		recordCoin.setCreateBy(member.getId());
		recordCoin.setUpdateBy(member.getId());
		coinRecordDao.addEntity(recordCoin);
		
		//添加交易记录(现金)
		CashRecord record = new CashRecord();
		record.setId(UUIDUtil.get32UUID());
		record.setMemberId(memberId);
		//record.setTargetId(memberId);
		//record.setTargetName(member.getName());
		record.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_5);
		record.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_2);
		record.setCitycode(member.getCitycode());
		record.setOutcome(money);
		record.setOrderId(order.getId());
		record.setTradeNo(tradeNo);
		record.setCitycode(member.getCitycode());
		record.setCreateBy(member.getId());
		record.setUpdateBy(member.getId());
		cashRecordDao.addEntity(record);
		
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2);
		orderAccountDao.updateEntity(order);
	}
	
	
	@Override
	@Transactional
	public void reward(String tradeNo) throws Exception{
		OrderAccount order = new OrderAccount();
		order.setTradeNo(tradeNo);
		order = (OrderAccount) orderAccountDao.queryEntity(order);
		String memberId = order.getMemberId();
		long numFormat = order.getNum();
		String targetId = order.getTargetId();
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(order.getStatus()!=Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1){
			throw new ServiceException("订单已关闭");
		}
		Member member = (Member) memberDao.queryEntityById(memberId);
		Member targetMember = (Member) memberDao.queryEntityById(targetId);	
		if(null==targetMember){
			throw new Exception("收款方不存在");
		}
		//付款方扣减
		AccountCoin delAcc = new AccountCoin();
		delAcc.setMemberId(memberId);
		delAcc = (AccountCoin) accountCoinDao.queryEntity(delAcc);
		if(null==delAcc){
			throw new Exception("账户不存在");
		}
		if(delAcc.getNum()<numFormat){
			throw new Exception("余额不足");
		}
		delAcc.setNum(delAcc.getNum()-numFormat);
		delAcc.setUpdateBy(memberId);
		accountCoinDao.updateEntity(delAcc);
		
		
		AccountCoin addAcc = new AccountCoin();
		addAcc.setMemberId(targetId);
		addAcc = (AccountCoin) accountCoinDao.queryEntity(addAcc);
		//收款方新增
		if(null==addAcc){
			addAcc=new AccountCoin();
			addAcc.setNum(numFormat);
			addAcc.setCreateBy(targetId);
			addAcc.setUpdateBy(targetId);
			addAcc.setId(UUIDUtil.get32UUID());
			addAcc.setMemberId(targetId);
			accountCoinDao.addEntity(addAcc);
		}else{
			addAcc.setNum(addAcc.getNum()+numFormat);
			addAcc.setUpdateBy(targetId);
			addAcc.setMemberId(targetId);
			accountCoinDao.updateEntity(addAcc);
		}
		
		//添加收款方交易记录
		CoinRecord addRecordCoin = new CoinRecord();
		addRecordCoin.setId(UUIDUtil.get32UUID());
		addRecordCoin.setMemberId(targetMember.getId());
		addRecordCoin.setTargetId(memberId);
		addRecordCoin.setTargetName(member.getName());
		addRecordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_3);
		addRecordCoin.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
		addRecordCoin.setCitycode(targetMember.getCitycode());
		addRecordCoin.setIncome(numFormat);
		addRecordCoin.setOrderId(order.getId());
		addRecordCoin.setTradeNo(tradeNo);
		addRecordCoin.setCreateBy(targetMember.getId());
		addRecordCoin.setUpdateBy(targetMember.getId());
		coinRecordDao.addEntity(addRecordCoin);
		
		//添加付款方交易记录
		CoinRecord delRecordCoin = new CoinRecord();
		delRecordCoin.setId(UUIDUtil.get32UUID());
		delRecordCoin.setMemberId(memberId);
		delRecordCoin.setTargetId(targetMember.getId());
		delRecordCoin.setTargetName(targetMember.getName());
		delRecordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNTCOIN_RECORD_TYPE_3);
		delRecordCoin.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_2);
		delRecordCoin.setCitycode(targetMember.getCitycode());
		delRecordCoin.setOutcome(numFormat);
		delRecordCoin.setOrderId(order.getId());
		delRecordCoin.setTradeNo(tradeNo);
		delRecordCoin.setCreateBy(memberId);
		delRecordCoin.setUpdateBy(memberId);
		coinRecordDao.addEntity(delRecordCoin);
		
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2);
		orderAccountDao.updateEntity(order);
		
	}
	@Override
	public void updatePassword(Account acc) throws Exception{
		accountDao.updatePassword(acc);
	}
	@Override
	public Account queryPwdMember(String memberId) throws Exception{
		return accountDao.queryPwdMember(memberId);
	}
}
