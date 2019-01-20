package com.thsword.netjob.web.controller.app.account;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IAccountCoinDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.INewsDao;
import com.thsword.netjob.dao.IRecordDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.News;
import com.thsword.netjob.service.AccountService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.WxpayAppUtil;
import com.thsword.netjob.util.alipay.Alipay;
import com.thsword.netjob.util.alipay.AlipayUtils;
import com.thsword.netjob.util.wxpay.WXPay;
import com.thsword.netjob.util.wxpay.WXPayUtil;
import com.thsword.utils.ip.IPUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class AccountApp {
	@Resource(name = "accountService")
	AccountService accountService;
	private static final Log log = LogFactory.getLog(AccountApp.class);
	/**
	 * 
	 * @Description:充值账户
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preWxpay")
	public void rechargeWx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String total_fee = (String) request.getParameter("total_fee");
			String flowId = UUIDUtil.get32FlowID();
			String ip = IPUtil.getRemoteHost(request);
			if(StringUtils.isEmpty(total_fee)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			try {
				Integer.parseInt(total_fee);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式不正确", response, request);
				return;
			}
			Map<String, String> map = WxpayAppUtil.doUnifiedOrder(total_fee, flowId, ip);
			map.put("out_trade_no", flowId);
			JsonResponseUtil.successBodyResponse(map, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description:充值账户
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preAlipay")
	public void rechargeAlipay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String total_fee = (String) request.getParameter("total_fee");
			String flowId = UUIDUtil.get32FlowID();
			if(StringUtils.isEmpty(total_fee)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			try {
				Double.parseDouble(total_fee);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式不正确", response, request);
				return;
			}
			String orderString = AlipayUtils.doUnifiedOrder(flowId,total_fee);
			Map<String, String> map = new HashMap<String, String>();
			map.put("out_trade_no", flowId);
			map.put("orderString", orderString);
			JsonResponseUtil.successBodyResponse(map, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description:查询支付结果
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/rechangeWx/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InputStream inStream=null;
		ByteArrayOutputStream outSteam=null;
		try {
			System.out.println("微信支付回调");
			log.info("微信支付回调");
	        inStream = request.getInputStream();
	        outSteam = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = inStream.read(buffer)) != -1) {
	            outSteam.write(buffer, 0, len);
	        }
	        String resultxml = new String(outSteam.toByteArray(), "utf-8");
	        Map<String, String> params = WXPayUtil.xmlToMap(resultxml);
	        WXPay wxPay = new WXPay();
	        log.info(wxPay.isPayResultNotifySignatureValid(wxPay.fillRequestData(params)));
	        if (!wxPay.isPayResultNotifySignatureValid(wxPay.fillRequestData(params))) {
	            // 支付失败
	        } else {
	        	log.info("===============付款成功==============");
	            // ------------------------------
	            // 处理业务开始
	            // ------------------------------
	            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
	            // ------------------------------
	 
	           /* String total_fee = params.get("total_fee");
	            double v = Double.valueOf(total_fee) / 100;
	            String out_trade_no = String.valueOf(Long.parseLong(params.get("out_trade_no").split("O")[0]));
				Date accountTime = DateUtil.getDate(params.get("time_end"), "yyyyMMddHHmmss");
				Date ordertime = new Date();
				String totalAmount = String.valueOf(v);
				String appId = params.get("appid");
				String tradeNo = params.get("transaction_id");*/
	        }
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}finally{
			if(null!=outSteam){
				outSteam.close();
			}
			if(null!=inStream){
				inStream.close();
			}
		}
	}
	
	

	/**
	 * @function:支付宝充值结果通知接口
	 * 2018年5月8日 上午12:07:45
	 */
	@RequestMapping(value = "app/rechangeAlipay/callback", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void getAlipayNotify(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String,Object> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		try {
			boolean flag = Alipay.rsaCheck(params);
			if (flag) {
				String trade_status = params.get("trade_status");
				String out_trade_no = params.get("out_trade_no");
				String trade_no = params.get("trade_no");
				if ("TRADE_SUCCESS".equals(trade_status)) { // 交易支付成功的执行相关业务逻辑
					log.info("=========支付宝支付结果：付款成功==========");
				} else if ("TRADE_CLOSED".equals(trade_status)) { // 未付款交易超时关闭,或支付完成后全额退款,执行相关业务逻辑
						
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * @Description:查询支付结果
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/queryWxPayInfo")
	public void queryWxPayInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String out_trade_no = (String) request.getParameter("out_trade_no");
			if(StringUtils.isEmpty(out_trade_no)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "out_trade_no不能为空", response, request);
				return;
			}
			Map<String, String> map = WxpayAppUtil.doOrderQuery(out_trade_no);
			JsonResponseUtil.successBodyResponse(map, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description:查询支付结果
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/queryAlipayInfo")
	public void queryAlipayInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String out_trade_no = (String) request.getParameter("out_trade_no");
			if(StringUtils.isEmpty(out_trade_no)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "out_trade_no不能为空", response, request);
				return;
			}
			Map<String, String> map = AlipayUtils.doOrderQuery(out_trade_no);
			JsonResponseUtil.successBodyResponse(map, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	@RequestMapping("app/member/account/wxPay/getSandKey")
	public void getSandKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String key = WxpayAppUtil.doGetSandboxSignKey();
			JsonResponseUtil.successBodyResponse(key, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	

	/**
	 * 
	 * @Description:充值账户
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
			CashRecord record = new CashRecord();
			record.setId(UUIDUtil.get32UUID());
			record.setMemberId(memberId);
			record.setTargetId(memberId);
			record.setTargetName(member.getName());
			record.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_1);
			record.setCitycode(member.getCitycode());
			record.setMoney(moneyDouble+"");
			record.setFlowId(flowId);
			record.setCitycode(member.getCitycode());
			record.setCreateBy(member.getId());
			record.setUpdateBy(member.getId());
			accountService.addEntity(IRecordDao.class, record);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
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
			CashRecord recordCoin = new CashRecord();
			recordCoin.setId(UUIDUtil.get32UUID());
			recordCoin.setMemberId(memberId);
			recordCoin.setTargetId(memberId);
			recordCoin.setTargetName(member.getName());
			recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_1);
			recordCoin.setCitycode(member.getCitycode());
			recordCoin.setMoney(num+"");
			recordCoin.setFlowId(UUIDUtil.get32FlowID());
			recordCoin.setCitycode(member.getCitycode());
			recordCoin.setCreateBy(member.getId());
			recordCoin.setUpdateBy(member.getId());
			accountService.addEntity(IRecordDao.class, recordCoin);
			
			if(payTypeInt==Global.SYS_MEMBER_ACCOUNT_PAY_TYPE_1){
				//添加交易记录(现金)
				CashRecord record = new CashRecord();
				record.setId(UUIDUtil.get32UUID());
				record.setMemberId(memberId);
				record.setTargetId(memberId);
				record.setTargetName(member.getName());
				recordCoin.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_2);
				record.setCitycode(member.getCitycode());
				record.setMoney(moneyDouble+"");
				record.setCitycode(member.getCitycode());
				record.setCreateBy(member.getId());
				record.setUpdateBy(member.getId());
				accountService.addEntity(IRecordDao.class, record);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
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
			log.info(e.getMessage());
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
			log.info(e.getMessage());
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
	public void record(HttpServletRequest request, HttpServletResponse response,Page page,CashRecord record) throws Exception {
		try {
			String accountType = request.getParameter("accountType");
			String recordType = request.getParameter("recordType");
			String memberId = (String) request.getAttribute("memberId");
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("accountType",accountType);
			map.put("recordType",recordType);
			map.put("memberId",memberId);
			List<CashRecord> records = (List<CashRecord>) accountService.queryPageEntity(IRecordDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", records);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
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
			CashRecord record = new CashRecord();
			record.setFlowId(flowId);
			record = (CashRecord) accountService.queryEntity(IRecordDao.class, record);
			JsonResponseUtil.successBodyResponse(record, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
}
