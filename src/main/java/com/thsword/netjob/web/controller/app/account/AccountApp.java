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
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.dao.ICoinRecordDao;
import com.thsword.netjob.dao.IOrderAccountDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.CoinRecord;
import com.thsword.netjob.pojo.app.OrderAccount;
import com.thsword.netjob.service.AccountService;
import com.thsword.netjob.service.MemberService;
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
	@Resource(name = "memberService")
	MemberService memberService;
	private static final Log log = LogFactory.getLog(AccountApp.class);
	
	
	/**
	 * 
	 * @Description:设置支付密码
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/setPassword")
	public void setPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String password = request.getParameter("password");
			if(StringUtils.isEmpty(password)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			Account acc = new Account();
			acc.setMemberId(memberId);
			acc = (Account) accountService.queryEntity(IAccountDao.class, acc);
			if(null==acc){
				acc=new Account();
				acc.setCreateBy(memberId);
				acc.setUpdateBy(memberId);
				acc.setId(UUIDUtil.get32UUID());
				acc.setMemberId(memberId);
				accountService.addEntity(IAccountDao.class, acc);
			}
			accountService.updatePassword(acc);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description:重置支付密码
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/reSetPassword")
	public void reSetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String password = request.getParameter("password");
			if(StringUtils.isEmpty(password)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			Account acc = new Account();
			acc.setMemberId(memberId);
			acc = (Account) accountService.queryEntity(IAccountDao.class, acc);
			if(null==acc){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户不存在", response, request);
				return;
			}
			accountService.updatePassword(acc);
			JsonResponseUtil.successCodeResponse(response, request);
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
	@RequestMapping("app/member/account/preWxpay")
	public void rechargeWx(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String total_fee = (String) request.getParameter("total_fee");
			String type = request.getParameter("type");
			String ip = IPUtil.getRemoteHost(request);
			if(StringUtils.isEmpty(total_fee)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(type)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值类型不能为空", response, request);
				return;
			}
			if(!type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1+"")&&type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2+"")&&type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_3+"")){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值类型异常", response, request);
				return;
			}
			try {
				Long.parseLong(total_fee);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值金额格式不正确", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			BigDecimal  formatMoney = new BigDecimal(total_fee).divide(new BigDecimal(100));
			//生成订单
			OrderAccount order;
			if(type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1+"")){
				order = buildCashOrder(memberId, formatMoney,Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_1);
			}else if(type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2+"")){
				order = buildCoinOrder(memberId, formatMoney,Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_1);
			}else{
				order = new OrderAccount();
			}
			String orderId = order.getId();
			accountService.addEntity(IOrderAccountDao.class, order);
			Map<String, String> map = new HashMap<String, String>();
			try {
				map = WxpayAppUtil.doUnifiedOrder(total_fee, order.getTradeNo(), ip);
				map.put("out_trade_no", order.getTradeNo());
				JsonResponseUtil.successBodyResponse(map, response, request);
			} catch (Exception e) {
				log.info("doUnifiedOrder error the params is "+JSONObject.toJSONString(order));
				accountService.deleteEntityById(IOrderAccountDao.class, orderId);
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
	 * @Description:支付结果回调
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
	        		OrderAccount order = new OrderAccount();
	        		order.setTradeNo(out_trade_no);
	        		order = (OrderAccount) accountService.queryEntity(IOrderAccountDao.class, order);
	        		// 支付失败
	        		if(null!=order){
	        			//更新充值订单状态
	        			order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4);
	        			accountService.updateEntity(IOrderAccountDao.class, order);
	        		}
	        	}
	        } else {
	        	log.info("===============付款成功==============");
	        	String return_code = params.get("return_code");
	        	if("SUCCESS".equals(return_code)){
	        		String out_trade_no = params.get("out_trade_no");
	        		OrderAccount order = new OrderAccount();
	        		order.setTradeNo(out_trade_no);
	        		order = (OrderAccount) accountService.queryEntity(IOrderAccountDao.class, order);
	        		if(null==order){
	        			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付订单不存在",response, request);
	        			return;
	        		}
	        		if(order.getType()==Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1){
	        			accountService.rechargeAccount(out_trade_no);
	        		}else if(order.getType()==Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2){
	        			accountService.rechargeAccountCoin(out_trade_no);
	        		}else{
	        			
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
	 * 
	 * @Description:充值账户
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preAlipay")
	public void rechargeAlipay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String total_fee = (String) request.getParameter("total_fee");
			String type = request.getParameter("type");
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
			if(!type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1+"")&&type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2+"")&&type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_3+"")){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值类型异常", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			BigDecimal formatMoney = new BigDecimal(total_fee).divide(new BigDecimal(100));
			//生成订单
			OrderAccount order;
			if(type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1+"")){
				order = buildCashOrder(memberId, formatMoney,Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_2);
			}else if(type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2+"")){
				order = buildCoinOrder(memberId, formatMoney,Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_2);
			}else{
				order = new OrderAccount();
			}
			String orderId = order.getId();
			accountService.addEntity(IOrderAccountDao.class, order);
			Map<String, String> map = new HashMap<String, String>();
			try {
				String orderString = AlipayUtils.doUnifiedOrder(order.getTradeNo(),formatMoney.toString());
				map.put("out_trade_no", order.getTradeNo());
				map.put("orderString", orderString);
				JsonResponseUtil.successBodyResponse(map, response, request);
			} catch (Exception e) {
				log.info("doUnifiedOrder error the params is "+JSONObject.toJSONString(order));
				accountService.deleteEntityById(IOrderAccountDao.class, orderId);
				e.printStackTrace();
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值异常",response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
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
					OrderAccount order = new OrderAccount();
	        		order.setTradeNo(out_trade_no);
	        		order = (OrderAccount) accountService.queryEntity(IOrderAccountDao.class, order);
	        		if(null==order){
	        			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "支付订单不存在",response, request);
	        			return;
	        		}
	        		if(order.getType()==Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1){
	        			accountService.rechargeAccount(out_trade_no);
	        		}else if(order.getType()==Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2){
	        			accountService.rechargeAccountCoin(out_trade_no);
	        		}else{
	        			
	        		}
	        		JsonResponseUtil.successCodeResponse(response, request);
					
				}else{
					OrderAccount order = new OrderAccount();
	        		order.setTradeNo(out_trade_no);
	        		order = (OrderAccount) accountService.queryEntity(IOrderAccountDao.class, order);
	        		// 支付失败
	        		if(null!=order){
	        			//更新充值订单状态
	        			order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4);
	        			accountService.updateEntity(IOrderAccountDao.class, order);
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
	 * @Description:网币兑换
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preSwap")
	public void preSwapCoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String total_fee = (String) request.getParameter("total_fee");
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
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			BigDecimal formatMoney = new BigDecimal(total_fee);
			Account ac=(Account) accountService.queryPwdMember(memberId);
			if(ac==null){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户未激活", response, request);
				return;
			}
			if(StringUtils.isEmpty(ac.getPassword())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请先设置支付密码", response, request);
				return;
			}
			if(ac.getMoney().compareTo(formatMoney)<0){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户余额不足", response, request);
				return;
			}
			//生成订单
			OrderAccount order = buildCoinSwapOrder(memberId, formatMoney);
			String orderId = order.getId();
			accountService.addEntity(IOrderAccountDao.class, order);
			Map<String, String> map = new HashMap<String, String>();
			try {
				map.put("out_trade_no", order.getTradeNo());
				JsonResponseUtil.successBodyResponse(map, response, request);
			} catch (Exception e) {
				log.info("doUnifiedOrder error the params is "+JSONObject.toJSONString(order));
				accountService.deleteEntityById(IOrderAccountDao.class, orderId);
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
	 * @Description:确认兑换网币付款
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/paySwap")
	public void paySwapCoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String password = (String) request.getParameter("password");
			String tradeNo = (String) request.getParameter("tradeNo");
			if(StringUtils.isEmpty(password)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(tradeNo)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "交易号不能为空", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			Account ac=(Account) accountService.queryPwdMember(memberId);
			if(StringUtils.isEmpty(ac.getPassword())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请先设置支付密码", response, request);
				return;
			}
			if(!password.equals(ac.getPassword())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码错误", response, request);
				return;
			}
			accountService.swapAccountCoin(tradeNo);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @Description:打赏网币
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preReward")
	public void preRewardCoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String num = (String) request.getParameter("num");
			String targetId = (String) request.getParameter("targetId");
			long numFormat = 0l;
			if(StringUtils.isEmpty(targetId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "targetId不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(num)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值数目不能为空", response, request);
				return;
			}
			try {
				numFormat = Long.parseLong(num);
			} catch (Exception e) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "充值数目格式不正确", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			AccountCoin acc = new AccountCoin();
			acc.setMemberId(memberId);
			acc = (AccountCoin) accountService.queryEntity(IAccountCoinDao.class, acc);
			if(null==acc){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户未激活", response, request);
				return;
			}
			Account ac=(Account) accountService.queryPwdMember(memberId);
			if(StringUtils.isEmpty(ac.getPassword())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请先设置支付密码", response, request);
				return;
			}
			if(acc.getNum()<numFormat){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "账户余额不足", response, request);
				return;
			}
			//生成订单
			OrderAccount order = buildCoinRewardOrder(memberId, targetId, numFormat);
			String orderId = order.getId();
			accountService.addEntity(IOrderAccountDao.class, order);
			Map<String, String> map = new HashMap<String, String>();
			try {
				map.put("out_trade_no", order.getTradeNo());
				JsonResponseUtil.successBodyResponse(map, response, request);
			} catch (Exception e) {
				log.info("preRewardCoin error the params is "+JSONObject.toJSONString(order));
				accountService.deleteEntityById(IOrderAccountDao.class, orderId);
				e.printStackTrace();
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "打赏异常",response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description:付款打赏网币
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/payReward")
	public void rewardCoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String password = (String) request.getParameter("password");
			String tradeNo = (String) request.getParameter("tradeNo");
			if(StringUtils.isEmpty(password)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(tradeNo)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "交易号不能为空", response, request);
				return;
			}
			if(!memberService.hasPhoneAuth(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请进行手机认证", response, request);
				return;
			}
			Account ac=(Account) accountService.queryPwdMember(memberId);
			if(StringUtils.isEmpty(ac.getPassword())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请先设置支付密码", response, request);
				return;
			}
			if(!password.equals(ac.getPassword())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码错误", response, request);
				return;
			}
			accountService.reward(tradeNo);
			
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
	public void records(HttpServletRequest request, HttpServletResponse response,Page page,CashRecord record) throws Exception {
		try {
			String accountType = request.getParameter("accountType");
			String recordType = request.getParameter("recordType");
			String memberId = (String) request.getAttribute("memberId");
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("recordType", recordType);
			map.put("memberId",memberId);
			if(StringUtils.isEmpty(accountType)){
				accountType = Global.SYS_MEMBER_ACCOUNT_TYPE_1+"";
			}
			JSONObject obj = new JSONObject();
			if(accountType.equals(Global.SYS_MEMBER_ACCOUNT_TYPE_1+"")){
				List<CashRecord> records = (List<CashRecord>) accountService.queryPageEntity(ICashRecordDao.class, map);
				obj.put("list", records);
			}else{
				List<CoinRecord> records = (List<CoinRecord>) accountService.queryPageEntity(ICoinRecordDao.class, map);
				obj.put("list", records);
			}
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
	public void recordInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String tradeNo = request.getParameter("tradeNo");
			if(StringUtils.isEmpty(tradeNo)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "tradeNo不能为空", response, request);
				return;
			}
			CashRecord record = new CashRecord();
			record.setTradeNo(tradeNo);
			record = (CashRecord) accountService.queryEntity(ICashRecordDao.class, record);
			JsonResponseUtil.successBodyResponse(record, response, request);
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
	@RequestMapping("app/member/account/coinRecordInfo")
	public void coinRecordInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String tradeNo = request.getParameter("tradeNo");
			if(StringUtils.isEmpty(tradeNo)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "flowId不能为空", response, request);
				return;
			}
			CoinRecord record = new CoinRecord();
			record.setTradeNo(tradeNo);
			record = (CoinRecord) accountService.queryEntity(ICoinRecordDao.class, record);
			JsonResponseUtil.successBodyResponse(record, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	
	/**
	 * 生成充值现金订单
	 * @param memberId 
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCashOrder(String memberId,BigDecimal formatMoney,int way){
		OrderAccount order = new OrderAccount();
		order.setTradeNo(UUIDUtil.get32TradeNo());
		order.setMemberId(memberId);
		order.setId(UUIDUtil.get32UUID());
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		order.setTotal_fee(formatMoney);
		order.setType(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1);
		order.setWay(way);
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1);
		return order;
	}
	
	/**
	 * 生成充值网币订单
	 * @param memberId 
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCoinOrder(String memberId,BigDecimal formatMoney,int way){
		OrderAccount order = new OrderAccount();
		order.setTradeNo(UUIDUtil.get32TradeNo());
		order.setMemberId(memberId);
		order.setId(UUIDUtil.get32UUID());
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		order.setTotal_fee(formatMoney);
		order.setType(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2);
		order.setWay(way);
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1);
		return order;
	}
	
	/**
	 * 生成兑换网币订单
	 * @param memberId 
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCoinSwapOrder(String memberId,BigDecimal formatMoney){
		OrderAccount order = new OrderAccount();
		order.setTradeNo(UUIDUtil.get32TradeNo());
		order.setMemberId(memberId);
		order.setId(UUIDUtil.get32UUID());
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		order.setTotal_fee(formatMoney);
		order.setType(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2);
		order.setWay(Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_4);
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1);
		return order;
	}
	
	/**
	 * 生成打赏网币订单
	 * @param memberId 
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCoinRewardOrder(String memberId,String targetId,long numFormat){
		OrderAccount order = new OrderAccount();
		order.setTradeNo(UUIDUtil.get32TradeNo());
		order.setMemberId(memberId);
		order.setTargetId(targetId);
		order.setId(UUIDUtil.get32UUID());
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		order.setNum(numFormat);
		order.setType(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2);
		order.setWay(Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_5);
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1);
		return order;
	}
}
