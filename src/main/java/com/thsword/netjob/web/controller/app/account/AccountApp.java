package com.thsword.netjob.web.controller.app.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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

import lombok.extern.log4j.Log4j2;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IAccountCoinDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.dao.ICoinRecordDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IOrderAccountDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCoin;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.CoinRecord;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.OrderAccount;
import com.thsword.netjob.service.AccountService;
import com.thsword.netjob.service.MemberService;
import com.thsword.netjob.util.RedisUtils;
import com.thsword.netjob.util.WxpayAppUtil;
import com.thsword.netjob.util.alipay.AlipayUtils;
import com.thsword.netjob.util.wxpay.WXPay;
import com.thsword.netjob.util.wxpay.WXPayUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.ip.IPUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-ACCOUNT", description = "账户接口")
@Log4j2
public class AccountApp {
	@Resource(name = "accountService")
	AccountService accountService;
	@Resource(name = "memberService")
	MemberService memberService;

	@RequestMapping("app/member/account/hasPassword")
	@ApiOperation(value = "是否设置支付密码", httpMethod = "POST")
	public JSONObject hasPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean flag = false;
		String memberId = request.getAttribute("memberId") + "";
		Account acc = accountService.queryPwdMember(memberId);
		if (null != acc && !StringUtils.isEmpty(acc.getPassword())) {
			flag = true;
		}
		JSONObject obj = new JSONObject();
		obj.put("hasSetPassword", flag);
		return obj;
	}

	/**
	 * 
	 * @Description:设置支付密码
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/setPassword")
	@ApiOperation(value = "设置支付密码", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true) })
	public Account setPassword(HttpServletRequest request,
			@RequestParam String password) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Account acc = new Account();
		acc.setMemberId(memberId);
		acc = accountService.queryPwdMember(memberId);
		if (null == acc) {
			acc = new Account();
			acc.setCreateBy(memberId);
			acc.setUpdateBy(memberId);
			acc.setId(UUIDUtil.get32UUID());
			acc.setMemberId(memberId);
			acc.setMoney(new BigDecimal(0));
			accountService.addEntity(IAccountDao.class, acc);
			acc.setPassword(password);
		} else if (StringUtils.isEmpty(acc.getPassword())) {
			acc.setPassword(password);
		} else {
			throw new ServiceException("已设置支付密码");
		}
		accountService.updatePassword(acc);
		return acc;
	}

	/**
	 * 
	 * @Description:重置支付密码
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/reSetPassword")
	@ApiOperation(value = "重置支付密码", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "password", value = "旧密码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "long", paramType = "query", required = true) })
	public BaseResponse reSetPassword(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String password,
			@RequestParam String newPassword) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Account acc = accountService.queryPwdMember(memberId);
		if (null == acc) {
			throw new ServiceException("账户不存在");
		}
		if (!acc.getPassword().equals(password)) {
			throw new ServiceException("密码错误");
		}
		acc.setPassword(newPassword);
		accountService.updatePassword(acc);
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:忘记密码
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/account/forgetPwd")
	@ApiOperation(value = "忘记密码", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "identifyCode", value = "验证码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "phone", value = "手机号", dataType = "long", paramType = "query", required = true),
			@ApiImplicitParam(name = "password", value = "新密码", dataType = "long", paramType = "query", required = true) })
	public BaseResponse forgetPassword(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String password,
			@RequestParam String identifyCode, @RequestParam String phone)
			throws Exception {
		String redisKey = "member:forgetPayPwd:" + phone;
		String redisValue = RedisUtils.get(redisKey);
		if (StringUtils.isEmpty(redisValue)) {
			throw new ServiceException("验证码无效");

		}
		JSONObject idenfifyInfo = JSONObject.parseObject(redisValue);

		String checkPhone = idenfifyInfo.getString("phone");
		String checkIdentifyCode = idenfifyInfo.getString("identifyCode");
		if (!checkPhone.equals(phone)) {
			throw new ServiceException("号码不匹配");

		}
		if (!checkIdentifyCode.equals(identifyCode)) {
			throw new ServiceException("验证码错误");

		}
		Member temp = new Member();
		temp.setPhone(phone);
		temp = (Member) memberService.queryEntity(IMemberDao.class, temp);
		if (null == temp) {
			throw new ServiceException("账户不存在");

		}
		Account acc = accountService.queryAccountByMemberId(temp.getId());
		if (acc == null) {
			acc = new Account();
			acc.setCreateBy(temp.getId());
			acc.setUpdateBy(temp.getId());
			acc.setId(UUIDUtil.get32UUID());
			acc.setMemberId(temp.getId());
			acc.setMoney(new BigDecimal(0));
			accountService.addEntity(IAccountDao.class, acc);
		}
		acc.setPassword(password);
		accountService.updatePassword(acc);

		RedisUtils.del(redisKey);
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:充值账户
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preWxpay")
	@ApiOperation(value = "微信预付款-生成订单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "total_fee", value = "充值金额（分）", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "type", value = "充值类型（1-账户充值，2-充值网币，3-充值保证金）", dataType = "long", paramType = "query", required = true) })
	public Map rechargeWx(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String total_fee,
			@RequestParam String type) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		String ip = IPUtil.getRemoteHost(request);
		if (!type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1 + "")
				&& type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2 + "")
				&& type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_3 + "")) {
			throw new ServiceException("充值类型异常");

		}
		try {
			Long.parseLong(total_fee);
		} catch (Exception e) {
			throw new ServiceException("充值金额格式不正确");

		}
		BigDecimal formatMoney = new BigDecimal(total_fee)
				.divide(new BigDecimal(100));
		// 生成订单
		OrderAccount order;
		if (type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1 + "")) {
			order = buildCashOrder(memberId, formatMoney,
					Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_1);
		} else if (type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2 + "")) {
			order = buildCoinOrder(memberId, formatMoney,
					Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_1);
		} else {
			order = buildDepositOrder(memberId, formatMoney,
					Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_1);
		}
		String orderId = order.getId();
		accountService.addEntity(IOrderAccountDao.class, order);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = WxpayAppUtil
					.doUnifiedOrder(total_fee, order.getTradeNo(), ip);
			map.put("out_trade_no", order.getTradeNo());
			return map;
		} catch (Exception e) {
			log.info("doUnifiedOrder error the params is "
					+ JSONObject.toJSONString(order));
			accountService.deleteEntityById(IOrderAccountDao.class, orderId);
			throw new ServiceException("充值异常");
		}
	}

	/**
	 * 
	 * @Description:支付结果回调
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/rechangeWx/callback")
	@ApiOperation(value = "微信支付回调", httpMethod = "POST")
	public BaseResponse callback(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream inStream = null;
		ByteArrayOutputStream outSteam = null;
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
			log.info(wxPay.isPayResultNotifySignatureValid(wxPay
					.fillRequestData(params)));
			log.info("wxpay callback params is "
					+ JSONObject.toJSONString(params));
			if (!wxPay.isPayResultNotifySignatureValid(wxPay
					.fillRequestData(params))) {
				log.info("===============付款失败==============");
				String out_trade_no = params.get("out_trade_no");
				if (!StringUtils.isEmpty(out_trade_no)) {
					OrderAccount order = new OrderAccount();
					order.setTradeNo(out_trade_no);
					order = (OrderAccount) accountService.queryEntity(
							IOrderAccountDao.class, order);
					// 支付失败
					if (null != order) {
						// 更新充值订单状态
						order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4);
						accountService.updateEntity(IOrderAccountDao.class,
								order);
					}
				}
			} else {
				log.info("===============付款成功==============");
				String return_code = params.get("return_code");
				if ("SUCCESS".equals(return_code)) {
					String out_trade_no = params.get("out_trade_no");
					OrderAccount order = new OrderAccount();
					order.setTradeNo(out_trade_no);
					order = (OrderAccount) accountService.queryEntity(
							IOrderAccountDao.class, order);
					if (null == order) {
						throw new ServiceException("支付订单不存在");
					}
					if (order.getType() == Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1) {
						accountService.rechargeAccount(out_trade_no);
					} else if (order.getType() == Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2) {
						accountService.rechargeAccountCoin(out_trade_no);
					} else {

					}
					return BaseResponse.success();
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			throw new ServiceException("微信支付回调异常");
		} finally {
			if (null != outSteam) {
				outSteam.close();
			}
			if (null != inStream) {
				inStream.close();
			}
		}
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:充值账户
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preAlipay")
	@ApiOperation(value = "支付宝预充值-生成订单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "total_fee", value = "充值金额（分）", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "type", value = "充值类型（1-账户充值，2-充值网币，3-充值保证金）", dataType = "long", paramType = "query", required = true) })
	public BaseResponse rechargeAlipay(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String total_fee,
			@RequestParam String type) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		try {
			Long.parseLong(total_fee);
		} catch (Exception e) {
			throw new ServiceException("充值金额格式不正确");

		}
		if (!type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1 + "")
				&& type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2 + "")
				&& type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_3 + "")) {
			throw new ServiceException("充值类型异常");

		}
		BigDecimal formatMoney = new BigDecimal(total_fee)
				.divide(new BigDecimal(100));
		// 生成订单
		OrderAccount order;
		if (type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1 + "")) {
			order = buildCashOrder(memberId, formatMoney,
					Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_2);
		} else if (type.equals(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2 + "")) {
			order = buildCoinOrder(memberId, formatMoney,
					Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_2);
		} else {
			order = buildDepositOrder(memberId, formatMoney,
					Global.SYS_MEMBER_ACCOUNT_RECHANGE_WAY_2);
		}
		String orderId = order.getId();
		accountService.addEntity(IOrderAccountDao.class, order);
		Map<String, String> map = new HashMap<String, String>();
		try {
			String orderString = AlipayUtils.doUnifiedOrder(order.getTradeNo(),
					formatMoney.toString());
			map.put("out_trade_no", order.getTradeNo());
			map.put("orderString", orderString);
			return BaseResponse.success(map);
		} catch (Exception e) {
			log.info("doUnifiedOrder error the params is "
					+ JSONObject.toJSONString(order));
			accountService.deleteEntityById(IOrderAccountDao.class, orderId);
			e.printStackTrace();
			throw new ServiceException("充值异常");
		}
	}

	/**
	 * @function:支付宝充值结果通知接口 2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "支付宝回调", httpMethod = "POST")
	@RequestMapping(value = "app/rechangeAlipay/callback", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public BaseResponse getAlipayNotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> requestParams = request.getParameterMap();
		log.info("rechangeAlipay requestParams info is "
				+ JSONObject.toJSONString(requestParams));
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		log.info("alipay callback params is " + JSONObject.toJSONString(params));
		boolean flag = AlipayUtils.rsaCheck(params);
		log.info("alipay rsaCheck result is " + flag);
		if (flag) {
			String trade_status = params.get("trade_status");
			String out_trade_no = params.get("out_trade_no");
			if ("TRADE_SUCCESS".equals(trade_status)) { // 交易支付成功的执行相关业务逻辑
				log.info("=========支付宝支付结果：付款成功==========");
				OrderAccount order = new OrderAccount();
				order.setTradeNo(out_trade_no);
				order = (OrderAccount) accountService.queryEntity(
						IOrderAccountDao.class, order);
				if (null == order) {
					throw new ServiceException("支付订单不存在");

				}
				if (order.getType() == Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_1) {
					accountService.rechargeAccount(out_trade_no);
				} else if (order.getType() == Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_2) {
					accountService.rechargeAccountCoin(out_trade_no);
				} else {
					accountService.rechargeAccountDeposit(out_trade_no);
				}
				return BaseResponse.success();
			} else {
				OrderAccount order = new OrderAccount();
				order.setTradeNo(out_trade_no);
				order = (OrderAccount) accountService.queryEntity(
						IOrderAccountDao.class, order);
				// 支付失败
				if (null != order) {
					// 更新充值订单状态
					order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_4);
					accountService.updateEntity(IOrderAccountDao.class, order);
				}
				log.info("===============付款失败==============");
				throw new ServiceException();
			}
		}
		log.info("===============回调完成==============");
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:网币兑换
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preSwap")
	@ApiOperation(value = "网币兑换预付款-生成订单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "total_fee", value = "兑换金额（元）", dataType = "string", paramType = "query", required = true) })
	public Map preSwapCoin(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String total_fee)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		try {
			Long.parseLong(total_fee);
		} catch (Exception e) {
			throw new ServiceException("充值金额格式不正确");

		}
		/*
		 * if(!memberService.hasPhoneAuth(memberId)){ throw new
		 * ServiceException("请进行手机认证");
		 * 
		 * }
		 */
		BigDecimal formatMoney = new BigDecimal(total_fee);
		Account ac = (Account) accountService.queryPwdMember(memberId);
		if (ac == null) {
			throw new ServiceException("账户未激活");

		}
		if (StringUtils.isEmpty(ac.getPassword())) {
			throw new ServiceException("请先设置支付密码");

		}
		if (ac.getMoney().compareTo(formatMoney) < 0) {
			throw new ServiceException("账户余额不足");

		}
		// 生成订单
		OrderAccount order = buildCoinSwapOrder(memberId, formatMoney);
		String orderId = order.getId();
		accountService.addEntity(IOrderAccountDao.class, order);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put("out_trade_no", order.getTradeNo());
			return map;
		} catch (Exception e) {
			log.info("doUnifiedOrder error the params is "
					+ JSONObject.toJSONString(order));
			accountService.deleteEntityById(IOrderAccountDao.class, orderId);
			e.printStackTrace();
			throw new ServiceException("充值异常");
		}
	}

	/**
	 * 
	 * @Description:确认兑换网币付款
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/paySwap")
	@ApiOperation(value = "支付兑换网币订单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "tradeNo", value = "订单交易号", dataType = "long", paramType = "query", required = true) })
	public BaseResponse paySwapCoin(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String password,
			@RequestParam String tradeNo) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Account ac = (Account) accountService.queryPwdMember(memberId);
		if (StringUtils.isEmpty(ac.getPassword())) {
			throw new ServiceException("请先设置支付密码");

		}
		if (!password.equals(ac.getPassword())) {
			throw new ServiceException("密码错误");

		}
		accountService.swapAccountCoin(tradeNo);
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:打赏网币
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/preReward")
	@ApiOperation(value = "打赏网币预付款-生成订单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "num", value = "打赏数目（个）", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "targetId", value = "用户id", dataType = "long", paramType = "query", required = true) })
	public Map preRewardCoin(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String num,
			@RequestParam String targetId) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		long numFormat = 0l;
		try {
			numFormat = Long.parseLong(num);
		} catch (Exception e) {
			throw new ServiceException("充值数目格式不正确");

		}
		if (!memberService.hasPhoneAuth(targetId)) {
			throw new ServiceException("收款方未进行手机认证");

		}
		AccountCoin acc = new AccountCoin();
		acc.setMemberId(memberId);
		acc = (AccountCoin) accountService.queryEntity(IAccountCoinDao.class,
				acc);
		if (null == acc) {
			throw new ServiceException("账户未激活");
		}
		Account ac = (Account) accountService.queryPwdMember(memberId);
		if (StringUtils.isEmpty(ac.getPassword())) {
			throw new ServiceException("请先设置支付密码");
		}
		if (acc.getNum() < numFormat) {
			throw new ServiceException("账户余额不足");
		}
		// 生成订单
		OrderAccount order = buildCoinRewardOrder(memberId, targetId, numFormat);
		String orderId = order.getId();
		accountService.addEntity(IOrderAccountDao.class, order);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put("out_trade_no", order.getTradeNo());
			return map;
		} catch (Exception e) {
			log.info("preRewardCoin error the params is "
					+ JSONObject.toJSONString(order));
			accountService.deleteEntityById(IOrderAccountDao.class, orderId);
			e.printStackTrace();
			throw new ServiceException("打赏订单异常");
		}
	}

	/**
	 * 
	 * @Description:付款打赏网币
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/payReward")
	@ApiOperation(value = "付款打赏网币", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "password", value = "支付密码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "tradeNo", value = "订单交易号", dataType = "long", paramType = "query", required = true) })
	public BaseResponse rewardCoin(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String password,
			@RequestParam String tradeNo) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Account ac = (Account) accountService.queryPwdMember(memberId);
		if (StringUtils.isEmpty(ac.getPassword())) {
			throw new ServiceException("请先设置支付密码");

		}
		if (!password.equals(ac.getPassword())) {
			throw new ServiceException("密码错误");

		}
		accountService.reward(tradeNo);
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:查询支付结果
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/queryWxPayInfo")
	@ApiOperation(value = "查询微信支付信息", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "out_trade_no", value = "订单交易号", dataType = "string", paramType = "query", required = true) })
	public Map queryWxPayInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String out_trade_no)
			throws Exception {
		Map<String, String> map = WxpayAppUtil.doOrderQuery(out_trade_no);
		return map;
	}

	/**
	 * 
	 * @Description:查询支付结果
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/queryAlipayInfo")
	@ApiOperation(value = "查询支付结果", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "out_trade_no", value = "订单交易号", dataType = "string", paramType = "query", required = true) })
	public String queryAlipayInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String out_trade_no)
			throws Exception {
		return AlipayUtils.orderQuery(out_trade_no);
	}

	@RequestMapping("app/member/account/wxPay/getSandKey")
	@ApiOperation(value = "获取沙箱KEY", httpMethod = "POST")
	public String getSandKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return WxpayAppUtil.doGetSandboxSignKey();
	}

	/**
	 * 
	 * @Description:现金账户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/info")
	@ApiOperation(value = "查询个人账户信息", httpMethod = "POST")
	public Account info(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String memberId = (String) request.getAttribute("memberId");
		Account ac = new Account();
		ac.setMemberId(memberId);
		ac = (Account) accountService.queryEntity(IAccountDao.class, ac);
		return ac;
	}

	/**
	 * 
	 * @Description:网币账户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/coinInfo")
	@ApiOperation(value = "查询个人网币账户信息", httpMethod = "POST")
	public AccountCoin coinInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String memberId = (String) request.getAttribute("memberId");
		AccountCoin ac = new AccountCoin();
		ac.setMemberId(memberId);
		ac = (AccountCoin) accountService
				.queryEntity(IAccountCoinDao.class, ac);
		return ac;
	}

	/**
	 * 
	 * @Description:交易记录
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/member/account/records")
	@ApiOperation(value = "查询交易记录列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "accountType", value = "账户类型（1-现金账户 2-网币账户）", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "recordType", value = "交易类型（1-充值 2-支付订单 3-退款 4-账户提现 5-兑换网币 6-网币提现 7-充值保证金）", dataType = "long", paramType = "query", required = true) })
	public JSONObject records(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam String accountType, @RequestParam String recordType)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		String memberId = (String) request.getAttribute("memberId");
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("recordType", recordType);
		map.put("memberId", memberId);
		if (StringUtils.isEmpty(accountType)) {
			accountType = Global.SYS_MEMBER_ACCOUNT_TYPE_1 + "";
		}
		JSONObject obj = new JSONObject();
		if (accountType.equals(Global.SYS_MEMBER_ACCOUNT_TYPE_1 + "")) {
			List<CashRecord> records = (List<CashRecord>) accountService
					.queryPageEntity(ICashRecordDao.class, map);
			obj.put("list", records);
		} else {
			List<CoinRecord> records = (List<CoinRecord>) accountService
					.queryPageEntity(ICoinRecordDao.class, map);
			obj.put("list", records);
		}
		obj.put("page", page);
		return obj;
	}

	/**
	 * 
	 * @Description:交易明细
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/recordInfo")
	@ApiOperation(value = "查看账户交易明细", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "tradeNo", value = "订单交易号", dataType = "long", paramType = "query", required = true) })
	public CashRecord recordInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String tradeNo)
			throws Exception {
		CashRecord record = new CashRecord();
		record.setTradeNo(tradeNo);
		record = (CashRecord) accountService.queryEntity(ICashRecordDao.class,
				record);
		return record;
	}

	/**
	 * 
	 * @Description:交易明细
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/account/coinRecordInfo")
	@ApiOperation(value = "查询网币交易明细", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "tradeNo", value = "订单交易号", dataType = "long", paramType = "query", required = true) })
	public CoinRecord coinRecordInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String tradeNo)
			throws Exception {
		CoinRecord record = new CoinRecord();
		record.setTradeNo(tradeNo);
		record = (CoinRecord) accountService.queryEntity(ICoinRecordDao.class,
				record);
		return record;
	}

	/**
	 * 生成充值现金订单
	 * 
	 * @param memberId
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCashOrder(String memberId,
			BigDecimal formatMoney, int way) {
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
	 * 
	 * @param memberId
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCoinOrder(String memberId,
			BigDecimal formatMoney, int way) {
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
	 * 生成充值保证金订单
	 * 
	 * @param memberId
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildDepositOrder(String memberId,
			BigDecimal formatMoney, int way) {
		OrderAccount order = new OrderAccount();
		order.setTradeNo(UUIDUtil.get32TradeNo());
		order.setMemberId(memberId);
		order.setId(UUIDUtil.get32UUID());
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		order.setTotal_fee(formatMoney);
		order.setType(Global.SYS_MEMBER_ACCOUNT_RECHANGE_TYPE_3);
		order.setWay(way);
		order.setStatus(Global.SYS_MEMBER_ACCOUNT_RECHANGE_ORDER_STATUS_1);
		return order;
	}

	/**
	 * 生成兑换网币订单
	 * 
	 * @param memberId
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCoinSwapOrder(String memberId,
			BigDecimal formatMoney) {
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
	 * 
	 * @param memberId
	 * @param total_fee
	 * @param type
	 * @param way
	 * @return
	 */
	private OrderAccount buildCoinRewardOrder(String memberId, String targetId,
			long numFormat) {
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
