package com.thsword.netjob.web.controller.app.account;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.thsword.netjob.dao.IRechargeOrderDao;
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.RechargeOrder;
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
			String memberId = request.getAttribute("memberId")+"";
			String total_fee = (String) request.getParameter("total_fee");
			String ip = IPUtil.getRemoteHost(request);
			if(StringUtils.isEmpty(total_fee)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			try {
				Long.parseLong(total_fee);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式不正确", response, request);
				return;
			}
			BigDecimal  formatMoney = new BigDecimal(total_fee).divide(new BigDecimal(100));
			//生成订单
			RechargeOrder order = buildChargeOrder(memberId, formatMoney,Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1,Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1);
			String orderId = order.getId();
			accountService.addEntity(IRechargeOrderDao.class, order);
			Map<String, String> map = new HashMap<String, String>();
			try {
				map = WxpayAppUtil.doUnifiedOrder(total_fee, order.getFlowId(), ip);
				map.put("out_trade_no", order.getFlowId());
				JsonResponseUtil.successBodyResponse(map, response, request);
			} catch (Exception e) {
				log.info("doUnifiedOrder error the params is "+JSONObject.toJSONString(order));
				accountService.deleteEntityById(IRechargeOrderDao.class, orderId);
				e.printStackTrace();
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值异常",response, request);
			}
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
			String memberId = request.getAttribute("memberId")+"";
			String total_fee = (String) request.getParameter("total_fee");
			String flowId = UUIDUtil.get32FlowID();
			if(StringUtils.isEmpty(total_fee)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			try {
				Long.parseLong(total_fee);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式不正确", response, request);
				return;
			}
			BigDecimal formatMoney = new BigDecimal(total_fee).divide(new BigDecimal(100));
			//生成订单
			RechargeOrder order = buildChargeOrder(memberId, formatMoney,Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1,Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2);
			String orderId = order.getId();
			accountService.addEntity(IRechargeOrderDao.class, order);
			Map<String, String> map = new HashMap<String, String>();
			try {
				String orderString = AlipayUtils.doUnifiedOrder(flowId,formatMoney.toString());
				map.put("out_trade_no", flowId);
				map.put("orderString", orderString);
				JsonResponseUtil.successBodyResponse(map, response, request);
			} catch (Exception e) {
				log.info("doUnifiedOrder error the params is "+JSONObject.toJSONString(order));
				accountService.deleteEntityById(IRechargeOrderDao.class, orderId);
				e.printStackTrace();
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值异常",response, request);
			}
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
	        log.info("wxpay callback params is "+JSONObject.toJSONString(params));
	        if (!wxPay.isPayResultNotifySignatureValid(wxPay.fillRequestData(params))) {
	        	log.info("===============付款失败==============");
	        	String out_trade_no = params.get("out_trade_no");
	        	if(!StringUtils.isEmpty(out_trade_no)){
	        		RechargeOrder order = new RechargeOrder();
	        		order.setFlowId(out_trade_no);
	        		order = (RechargeOrder) accountService.queryEntity(IRechargeOrderDao.class, order);
	        		// 支付失败
	        		if(null!=order){
	        			//更新充值订单状态
	        			order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4);
	        			accountService.updateEntity(IRechargeOrderDao.class, order);
	        		}
	        	}
	        } else {
	        	log.info("===============付款成功==============");
	        	String return_code = params.get("return_code");
	        	if("SUCCESS".equals(return_code)){
	        		String out_trade_no = params.get("out_trade_no");
	        		RechargeOrder order = new RechargeOrder();
	        		order.setFlowId(out_trade_no);
	        		order = (RechargeOrder) accountService.queryEntity(IRechargeOrderDao.class, order);
	        		
	        		String memberId = order.getMemberId();
	        		//充值账户
	        		if(order.getStatus()==Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1){
	        			accountService.rechargeAccount(memberId,out_trade_no,order.getTotal_fee());
	        			order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2);
	        			accountService.updateEntity(IRechargeOrderDao.class, order);
	        		}
	        		
	        		JsonResponseUtil.successCodeResponse(response, request);
	        	}
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "app/rechangeAlipay/callback", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void getAlipayNotify(HttpServletRequest request, HttpServletResponse response) {
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
			log.info("alipay callback params is "+JSONObject.toJSONString(params));
			boolean flag = Alipay.rsaCheck(params);
			if (flag) {
				String trade_status = params.get("trade_status");
				String out_trade_no = params.get("out_trade_no");
				if ("TRADE_SUCCESS".equals(trade_status)) { // 交易支付成功的执行相关业务逻辑
					log.info("=========支付宝支付结果：付款成功==========");
	        		RechargeOrder order = new RechargeOrder();
	        		order.setFlowId(out_trade_no);
	        		order = (RechargeOrder) accountService.queryEntity(IRechargeOrderDao.class, order);
	        		
	        		String memberId = order.getMemberId();
	        		//充值账户
	        		if(order.getStatus()==Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1){
	        			accountService.rechargeAccount(memberId,out_trade_no,order.getTotal_fee());
	        			order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_2);
	        			accountService.updateEntity(IRechargeOrderDao.class, order);
	        		}
	        		JsonResponseUtil.successCodeResponse(response, request);
					
				}else{
					RechargeOrder order = new RechargeOrder();
	        		order.setFlowId(out_trade_no);
	        		order = (RechargeOrder) accountService.queryEntity(IRechargeOrderDao.class, order);
	        		// 支付失败
	        		if(null!=order){
	        			//更新充值订单状态
	        			order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4);
	        			accountService.updateEntity(IRechargeOrderDao.class, order);
	        		}
					log.info("===============付款失败==============");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponseUtil.successCodeResponse(response, request);
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
			String result = AlipayUtils.orderQuery(out_trade_no);
			JsonResponseUtil.successBodyResponse(result, response, request);
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
			Double moneyDouble=0d;
			if(StringUtils.isEmpty(money)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
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
			Account ac=new Account();
			ac.setMemberId(memberId);
			ac = (Account) accountService.queryEntity(IAccountDao.class,ac);
			if(null==ac){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户不存在", response, request);
				return;
			}
			if(ac.getMoney().compareTo(new BigDecimal(money))<0){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户余额不足", response, request);
				return;
			}
			accountService.rechargeAccountCoin(memberId,new BigDecimal(money));
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
			List<CashRecord> records = (List<CashRecord>) accountService.queryPageEntity(ICashRecordDao.class, map);
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
			record = (CashRecord) accountService.queryEntity(ICashRecordDao.class, record);
			JsonResponseUtil.successBodyResponse(record, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	/**
	 * 生成订单
	 * @param memberId 
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private RechargeOrder buildChargeOrder(String memberId,BigDecimal formatMoney,int type,int way){
		RechargeOrder order = new RechargeOrder();
		order.setFlowId(UUIDUtil.get32FlowID());
		order.setMemberId(memberId);
		order.setId(UUIDUtil.get32UUID());
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		order.setTotal_fee(formatMoney);
		order.setType(type);
		order.setWay(way);
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1);
		return order;
	}
}
