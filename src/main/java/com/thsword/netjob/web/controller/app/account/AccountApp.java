package com.thsword.netjob.web.controller.app.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IAccountCoinDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.dao.IRecordDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.pojo.app.Record;
import com.thsword.netjob.service.AccountService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class AccountApp {
	@Resource(name = "accountService")
	AccountService accountService;

	/**
	 * 
	
	 * @Description:充值账户
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/recharge")
	public void recharge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = (String) request.getAttribute("memberId");
			String money = request.getParameter("money");
			String payType = request.getParameter("payType");
			String flowId = request.getParameter("flowId");
			
			Double moneyDouble   =  0d;
			Integer payTypeInt = 1;
			if(StringUtils.isEmpty(money)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(payType)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付方式不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(flowId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "流水号不能为空", response, request);
				return;
			}
			try {
				moneyDouble = Double.parseDouble(money);
				if(moneyDouble<=0){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额必须为正数", response, request);
					return;
				}
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式错误", response, request);
				return;
			}
			try {
				payTypeInt = Integer.parseInt(payType);
				if(!(payTypeInt==Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_1||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_2||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_3||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_4||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_5)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付方式类型异常", response, request);
					return;
				}
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付方式格式错误", response, request);
				return;
			}
			Member member = (Member) accountService.queryEntityById(IMemberDao.class,memberId);
			Account ac = new Account();
			ac.setMemberId(memberId);
			ac = (Account) accountService.queryEntity(IAccountDao.class, ac);
			if(null==ac){
				ac=new Account();
				ac.setMoney(moneyDouble);
				ac.setCreateBy(memberId);
				ac.setUpdateBy(memberId);
				ac.setId(UUIDUtil.get32UUID());
				ac.setIncome(moneyDouble);
				ac.setMemberId(memberId);
				accountService.addEntity(IAccountDao.class, ac);
			}else{
				ac.setMoney(ac.getMoney()+moneyDouble);
				ac.setUpdateBy(memberId);
				ac.setIncome(ac.getIncome()+moneyDouble);
				ac.setMemberId(memberId);
				accountService.updateEntity(IAccountDao.class, ac);
			}
			//添加动态
			News news = new News();
			news.setId(UUIDUtil.get32UUID());
			news.setMemberId(memberId);
			news.setType(Global.SYS_MEMBER_NEWS_TYPE_2);
			news.setCitycode(member.getCitycode());
			news.setContent("用户 "+member.getName()+" 充值"+moneyDouble+"元成功");
			news.setCreateBy(member.getId());
			news.setUpdateBy(member.getId());
			accountService.addEntity(INewsDao.class, news);
			//添加交易记录
			Record record = new Record();
			record.setId(UUIDUtil.get32UUID());
			record.setMemberId(memberId);
			record.setTargetId(memberId);
			record.setTargetName(member.getName());
			record.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_1);
			record.setAccountType(Global.SYS_MEMBER_ACCOUNT_TYPE_1);
			record.setCitycode(member.getCitycode());
			record.setMoney(moneyDouble+"");
			record.setFlowId(flowId);
			record.setPayType(payTypeInt);
			record.setCitycode(member.getCitycode());
			record.setCreateBy(member.getId());
			record.setUpdateBy(member.getId());
			accountService.addEntity(IRecordDao.class, record);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * @Description:充值网币
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/rechargeCoin")
	public void rechargeCoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = (String) request.getAttribute("memberId");
			String money = request.getParameter("money");
			String payType = request.getParameter("payType");
			
			Double moneyDouble   =  0d;
			Integer payTypeInt = 1;
			if(StringUtils.isEmpty(money)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(payType)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付方式不能为空", response, request);
				return;
			}
			try {
				moneyDouble = Double.parseDouble(money);
				if(moneyDouble<=0){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额必须为正数", response, request);
					return;
				}
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式错误", response, request);
				return;
			}
			try {
				payTypeInt = Integer.parseInt(payType);
				if(!(payTypeInt==Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_1||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_2||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_3||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_4||
						payTypeInt!=Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_5)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付方式类型异常", response, request);
					return;
				}
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付方式格式错误", response, request);
				return;
			}
			Member member = (Member) accountService.queryEntityById(IMemberDao.class,memberId);
			AccountCoin ac = new AccountCoin();
			ac.setMemberId(memberId);
			ac = (AccountCoin) accountService.queryEntity(AccountCoin.class, ac);
			long num = 0;
			num = Long.parseLong(moneyDouble*Global.SYS_MEMBER_MONEY_COIN_RATE+"");
			if(null==ac){
				ac=new AccountCoin();
				ac.setNum(num);
				ac.setCreateBy(memberId);
				ac.setUpdateBy(memberId);
				ac.setId(UUIDUtil.get32UUID());
				ac.setIncome(num);
				ac.setMemberId(memberId);
				accountService.addEntity(IAccountDao.class, ac);
			}else{
				ac.setNum(ac.getNum()+num);
				ac.setUpdateBy(memberId);
				ac.setIncome(ac.getIncome()+num);
				ac.setMemberId(memberId);
				accountService.updateEntity(IAccountDao.class, ac);
			}
			//添加动态
		/*	News news = new News();
			news.setId(UUIDUtil.get32UUID());
			news.setMemberId(memberId);
			news.setType(Global.SYS_MEMBER_NEWS_TYPE_2);
			news.setCitycode(member.getCitycode());
			news.setContent("用户 "+name+" 充值"+num+"网币成功");
			news.setCreateBy(member.getId());
			news.setUpdateBy(member.getId());
			accountService.addEntity(INewsDao.class, news);*/
			//添加交易记录(网币)
			Record recordCoin = new Record();
			recordCoin.setId(UUIDUtil.get32UUID());
			recordCoin.setMemberId(memberId);
			recordCoin.setTargetId(memberId);
			recordCoin.setTargetName(member.getName());
			recordCoin.setAccountType(Global.SYS_MEMBER_ACCOUNT_TYPE_2);
			recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_1);
			recordCoin.setCitycode(member.getCitycode());
			recordCoin.setMoney(num+"");
			recordCoin.setFlowId(UUIDUtil.get28FlowID());
			recordCoin.setPayType(payTypeInt);
			recordCoin.setCitycode(member.getCitycode());
			recordCoin.setCreateBy(member.getId());
			recordCoin.setUpdateBy(member.getId());
			accountService.addEntity(IRecordDao.class, recordCoin);
			
			if(payTypeInt==Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_1){
				//添加交易记录(现金)
				Record record = new Record();
				record.setId(UUIDUtil.get32UUID());
				record.setMemberId(memberId);
				record.setTargetId(memberId);
				record.setTargetName(member.getName());
				record.setAccountType(Global.SYS_MEMBER_ACCOUNT_TYPE_1);
				recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_2);
				record.setCitycode(member.getCitycode());
				record.setMoney(moneyDouble+"");
				record.setFlowId(UUIDUtil.get28FlowID());
				record.setPayType(payTypeInt);
				record.setCitycode(member.getCitycode());
				record.setCreateBy(member.getId());
				record.setUpdateBy(member.getId());
				accountService.addEntity(IRecordDao.class, record);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:现金账户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/info")
	public void info(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = (String) request.getAttribute("memberId");
			Account ac=new Account();
			ac.setMemberId(memberId);
			ac = (Account) accountService.queryEntity(IAccountDao.class,ac);
			JsonResponseUtil.successBodyResponse(ac, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:网币账户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/coinInfo")
	public void coinInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = (String) request.getAttribute("memberId");
			AccountCoin ac=new AccountCoin();
			ac.setMemberId(memberId);
			ac = (AccountCoin) accountService.queryEntity(IAccountCoinDao.class,ac);
			JsonResponseUtil.successBodyResponse(ac, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:交易记录
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/member/account/records")
	public void record(HttpServletRequest request, HttpServletResponse response,Page page,Record record) throws Exception {
		try {
			String accountType = request.getParameter("accountType");
			String recordType = request.getParameter("recordType");
			String memberId = (String) request.getAttribute("memberId");
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("accountType",accountType);
			map.put("recordType",recordType);
			map.put("memberId",memberId);
			List<Record> records = (List<Record>) accountService.queryPageEntity(IRecordDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", records);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description:交易明细
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/recordInfo")
	public void record(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String flowId = request.getParameter("flowId");
			if(StringUtils.isEmpty(flowId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "flowId不能为空", response, request);
				return;
			}
			Record record = new Record();
			record.setFlowId(flowId);
			record = (Record) accountService.queryEntity(IRecordDao.class, record);
			JsonResponseUtil.successBodyResponse(record, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
