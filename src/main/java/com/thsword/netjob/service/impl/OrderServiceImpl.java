package com.thsword.netjob.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.thsword.netjob.dao.IAccountCenterDao;
import com.thsword.netjob.dao.IAccountCenterDetailDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.IAddressDao;
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IOrderDao;
import com.thsword.netjob.dao.IRefundApproveDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCenter;
import com.thsword.netjob.pojo.app.AccountCenterDetail;
import com.thsword.netjob.pojo.app.Address;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Order;
import com.thsword.netjob.pojo.app.RefundApprove;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.OrderService;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;
@Service(value = "orderService")
public class OrderServiceImpl extends BaseServiceImpl implements OrderService{
	@Autowired
	IServeDao serveDao;
	@Autowired
	IMemberDao memberDao;
	@Autowired
	IAddressDao addressDao;
	@Autowired
	IOrderDao orderDao;
	@Autowired
	IAccountDao accountDao;
	@Autowired
	ICashRecordDao cashRecordDao;
	@Autowired
	IAccountCenterDao accountCenterDao;
	@Autowired
	IAccountCenterDetailDao accountCenterDetailDao;
	@Autowired
	IRefundApproveDao refundApproveDao;
	@Transactional
	@Override
	public Order doServeOrder(Order order) throws Exception {
		String memberId = order.getMemberId();
		String addressId = order.getAddressId();
		String serveId = order.getServeId();
		Serve serve = (Serve) serveDao.queryEntityById(serveId);
		if(serve == null){
			throw new ServiceException("服务不存在");
		}
		String sellerId = serve.getMemberId();
		if(!StringUtils.isEmpty(addressId)){
			Address address = (Address) addressDao.queryEntityById(addressId);
			if(null == address){
				throw new ServiceException("地址不存在");
			}
			order.setAddress(address.getProvinceName()+"-"+address.getCityName()+"-"+address.getArea()+" "+address.getDetailAddress());
		}
		Member seller = (Member) memberDao.queryEntityById(sellerId);
		if(null==seller){
			throw new ServiceException("商家不存在");
		}
		Account pwdAccount = accountDao.queryPwdMember(memberId);
		if(null==pwdAccount){
			throw new ServiceException("账户异常");
		}
		order.setSellerId(sellerId);
		order.setSellerName(seller.getName());
		order.setSellerImage(seller.getImage());
		order.setTradeNo(UUIDUtil.get32TradeNo());
		order.setMenuName(serve.getMenuName());
		order.setMenuId(serve.getMenuId());
		order.setFirstMenuId(serve.getFirstMenuId());
		order.setFirstMenuName(serve.getFirstMenuName());
		order.setId(UUIDUtil.get32UUID());
		order.setServeTitle(serve.getTitle());
		order.setServeImage(serve.getImage());
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_PAYING);
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		orderDao.addEntity(order);
		return order;
	}
	

	@Transactional
	@Override
	public void payOrders(String memberId,String orderIds,String password) throws Exception {
		Account pwdAccount = accountDao.queryPwdMember(memberId);
		if(null==pwdAccount){
			throw new ServiceException("请激活账户");
		}else if(!pwdAccount.getPassword().equals(password)){
			throw new ServiceException("密码错误");
		}
		JSONArray idArray = JSONArray.parseArray(orderIds);
		if(CollectionUtils.isEmpty(idArray)){
			throw new ServiceException("订单ID为空");
		}
		List<Order> orders = new ArrayList<Order>();
		BigDecimal totalCount = new BigDecimal(0);
		for (Object orderIdObj : idArray) {
			Order order = (Order) orderDao.queryEntityById(orderIdObj.toString());
			if(null==order){
				throw new ServiceException("订单不存在"+orderIdObj.toString());
			}
			if(!memberId.equals(order.getMemberId())){
				throw new ServiceException("订单用户与付款用户不一致");
			}
			if(!memberId.equals(order.getMemberId())){
				throw new ServiceException("订单用户ID异常"+orderIdObj.toString());
			}
			orders.add(order);
			totalCount = totalCount.add(order.getPrice().multiply(new BigDecimal(order.getCount())));
		}
		if(CollectionUtils.isEmpty(orders)){
			throw new ServiceException("订单数为空");
		}
		Account orderAccount = accountDao.queryAccountByMemberId(memberId);
		if(orderAccount.getMoney().compareTo(totalCount)<0){
			throw new ServiceException("余额不足");
		}
		for (Order order : orders) {
			if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_PAYING){
				throw new ServiceException("订单状态异常,不能付款");
			}
			Member delMember = (Member) memberDao.queryEntityById(memberId);
			BigDecimal orderMoney = order.getPrice().multiply(new BigDecimal(order.getCount()));
			String sellerId = order.getSellerId();
			String sellerName = order.getSellerName();
			//付款方添加付款记录
			CashRecord delRecord = new CashRecord();
			delRecord.setId(UUIDUtil.get32UUID());
			delRecord.setMemberId(memberId);
			delRecord.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_2);
			delRecord.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_2);
			delRecord.setCitycode(delMember.getCitycode());
			delRecord.setOutcome(orderMoney);
			delRecord.setTradeNo(order.getTradeNo());
			delRecord.setOrderId(order.getId());
			delRecord.setCitycode(delMember.getCitycode());
			delRecord.setCreateBy(delMember.getId());
			delRecord.setUpdateBy(delMember.getId());
			delRecord.setTargetId(sellerId);
			delRecord.setTargetName(sellerName);
			cashRecordDao.addEntity(delRecord);
			
			//更新买家账户余额
			orderAccount.setMoney(orderAccount.getMoney().subtract(orderMoney));
			accountDao.updateEntity(orderAccount);
			
			//更新订单状态 买家-已付款 商家-待接单
			order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_PAYED);
			order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_ACCEPTING);
			orderDao.updateEntity(order);
			
			//添加中央账户记录
			AccountCenterDetail accountCenterDetail = new AccountCenterDetail();
			accountCenterDetail.setId(UUIDUtil.get32UUID());
			accountCenterDetail.setBuyerId(order.getMemberId());
			accountCenterDetail.setCreateBy("SYSTEM");
			accountCenterDetail.setMoney(orderMoney);
			accountCenterDetail.setTradeNo(order.getTradeNo());
			accountCenterDetail.setOrderId(order.getId());
			accountCenterDetail.setSellerId(sellerId);
			accountCenterDetail.setStatus(Global.SYS_MEMBER_ACCOUNTCENTER_STATUS_1);
			accountCenterDetail.setUpdateBy("SYSTEM");
			accountCenterDetail.setTip(orderMoney.multiply(new BigDecimal(Global.getSetting(Global.SYS_MEMBER_SERVE_TIP_RATE))));
			accountCenterDetailDao.addEntity(accountCenterDetail);
			
			//更新中央账户金额
			AccountCenter center = (AccountCenter) accountCenterDao.queryEntityById(Global.SYS_NETJOB_CENTER_ACCOUNT_ID);
			if(null==center){
				throw new ServiceException("中央账户为空,系统错误");
			}
			center.setMoney(center.getMoney().add(totalCount));
			accountCenterDao.updateEntity(center);
		}
		
	}

	@Override
	public void signOrders(String memberId, String orderIds) throws Exception {
		JSONArray idArray = JSONArray.parseArray(orderIds);
		if(CollectionUtils.isEmpty(idArray)){
			throw new ServiceException("订单ID为空");
		}
		for (Object orderIdObj : idArray) {
			Order order =(Order) orderDao.queryEntityById(orderIdObj.toString());
			if(null==order){
				throw new ServiceException("订单不存在");
			}
			if(!memberId.equals(order.getMemberId())){
				throw new ServiceException("订单用户与签收用户不一致");
			}
			if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_SIGNING&&
					order.getSellerStatus()!=Global.SYS_ORDER_SELLER_STATUS_FINISH){
				throw new ServiceException("订单状态异常,不能签收");
			}
			String sellerId = order.getSellerId();
			Member member = (Member) memberDao.queryEntityById(memberId);
			BigDecimal totalMoney = order.getPrice().multiply(new BigDecimal(order.getCount()));
			
			//更新订单状态
			order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_COMMENTING);
			order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_CLOSED);
			orderDao.updateEntity(order);
			//更新中央账户记录
			AccountCenterDetail accountCenter = new AccountCenterDetail();
			accountCenter.setOrderId(order.getId());
			accountCenter = (AccountCenterDetail) accountCenterDetailDao.queryEntity(accountCenter);
			if(null==accountCenter){
				throw new ServiceException("未发现入账记录,订单异常");
			}
			if(accountCenter.getMoney().compareTo(totalMoney)!=0){
				throw new ServiceException("付款金额与结算金额不一致,订单异常");
			}
			accountCenter.setStatus(Global.SYS_MEMBER_ACCOUNTCENTER_STATUS_2);
			accountCenterDetailDao.updateEntity(accountCenter);
			
			//更新中央账户金额
			AccountCenter center = (AccountCenter) accountCenterDao.queryEntityById(Global.SYS_NETJOB_CENTER_ACCOUNT_ID);
			if(null==center){
				throw new ServiceException("中央账户为空,系统错误");
			}
			if(center.getMoney().compareTo(totalMoney)<0){
				throw new ServiceException("中央账户余额不足,系统错误");
			}
			center.setMoney(center.getMoney().subtract(totalMoney));
			accountCenterDao.updateEntity(center);
			
			//商户添加收款记录
			CashRecord payRecord = new CashRecord();
			payRecord.setId(UUIDUtil.get32UUID());
			payRecord.setMemberId(sellerId);
			payRecord.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_2);
			payRecord.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
			payRecord.setIncome(totalMoney.subtract(accountCenter.getTip()));
			payRecord.setTradeNo(order.getTradeNo());
			payRecord.setOrderId(order.getId());
			payRecord.setCitycode(member.getCitycode());
			payRecord.setCreateBy(member.getId());
			payRecord.setUpdateBy(member.getId());
			payRecord.setTargetId(member.getId());
			payRecord.setTargetName(member.getName());
			cashRecordDao.addEntity(payRecord);
			
			//商家更新账户余额
			Account sellerAcc = accountDao.queryAccountByMemberId(sellerId);
			if(null==sellerAcc){
				sellerAcc = new Account();
				sellerAcc.setCreateBy(sellerId);
				sellerAcc.setId(UUIDUtil.get32UUID());
				sellerAcc.setMemberId(sellerId);
				sellerAcc.setMoney(totalMoney);
				sellerAcc.setUpdateBy(sellerId);
				throw new ServiceException("商家账户不存在");
			}else{
				sellerAcc.setMoney(sellerAcc.getMoney().add(totalMoney));
			}
			accountDao.updateEntity(sellerAcc);
		}
	}
	
	@Transactional
	@Override
	public void applyRefund(String memberId, String tradeNo, BigDecimal money,
			String reason, String links) throws Exception {
		Order order = new Order();
		order.setTradeNo(tradeNo);
		order = (Order) orderDao.queryEntity(order);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getMemberId())){
			throw new ServiceException("订单用户与申请退款用户不一致");
		}
		//已接单-已付款-待签收-评论-已签收四种 状态时可申请退款
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_ACCEPTED&&
				order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_PAYED&&
					order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_SIGNED&&
						order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_SIGNING&&
						order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_COMMENTING
				){
			throw new ServiceException("订单状态异常,不能退款");
		}
		//添加退款申请
		RefundApprove refund = new RefundApprove();
		refund.setBuyerId(order.getMemberId());
		refund.setCreateBy(memberId);
		refund.setId(UUIDUtil.get32UUID());
		refund.setLinks(links);
		refund.setOrderId(order.getId());
		refund.setReason(reason);
		refund.setMoney(money);
		refund.setSellerId(order.getSellerId());
		refund.setTradeNo(tradeNo);
		refund.setType(Global.SYS_MEMBER_ORDER_REFUND);
		refund.setUpdateBy(memberId);
		refundApproveDao.addEntity(refund);
		//更新订单状态
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_APPLYING);
		orderDao.updateEntity(order);
		
	}

	@Override
	public void applyRight(String memberId, String tradeNo, BigDecimal money,
			String reason, String links) throws Exception {
		Order order = new Order();
		order.setTradeNo(tradeNo);
		order = (Order) orderDao.queryEntity(order);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getMemberId())){
			throw new ServiceException("订单用户与申请维权用户不一致");
		}
		//已拒绝退款待维权状态 可申请维权
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_RIGHTING
				){
			throw new ServiceException("订单状态异常,不能维权");
		}
		//添加维权申请
		RefundApprove refund = new RefundApprove();
		refund.setBuyerId(order.getMemberId());
		refund.setCreateBy(memberId);
		refund.setId(UUIDUtil.get32UUID());
		refund.setLinks(links);
		refund.setOrderId(order.getId());
		refund.setReason(reason);
		refund.setSellerId(order.getSellerId());
		refund.setTradeNo(tradeNo);
		refund.setMoney(money);
		refund.setType(Global.SYS_MEMBER_ORDER_RIGHT);
		refund.setUpdateBy(memberId);
		refundApproveDao.addEntity(refund);
		//更新订单状态
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_RIGHTED);
		order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_APPROVING);
		orderDao.updateEntity(order);
		
	}

	@Transactional
	@Override
	public void acceptOrders(String memberId,String orderIds) throws Exception {
		JSONArray idArray = JSONArray.parseArray(orderIds);
		for (Object orderIdObj : idArray) {
			Order order = (Order) orderDao.queryEntityById(orderIdObj.toString());
			if(null==order){
				throw new ServiceException("订单不存在"+orderIdObj.toString());
			}
			if(!memberId.equals(order.getSellerId())){
				throw new ServiceException("订单商户与接单商户不一致");
			}
			if(order.getSellerStatus()!=Global.SYS_ORDER_SELLER_STATUS_ACCEPTING){
				throw new ServiceException("订单状态异常,不能接单");
			}
			order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_ACCEPTED);
			order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_ACCEPTED);
			order.setAcceptDate(new Date());
			orderDao.updateEntity(order);
		}
	}
	
	@Transactional
	@Override
	public void cancelOrders(String memberId,String orderIds) throws Exception {
		JSONArray idArray = JSONArray.parseArray(orderIds);
		for (Object orderIdObj : idArray) {
			Order order = (Order) orderDao.queryEntityById(orderIdObj.toString());
			if(null==order){
				throw new ServiceException("订单不存在");
			}
			if(!memberId.equals(order.getSellerId())){
				throw new ServiceException("订单商户与拒单商户不一致");
			}
			if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_PAYED){
				throw new ServiceException("订单状态异常,不能拒单");
			}
			BigDecimal totalMoney = order.getPrice().multiply(new BigDecimal(order.getCount()));
			//已支付的退款
			Member member = (Member) memberDao.queryEntityById(order.getMemberId());
			//付款方添加退款记录
			CashRecord refundRecord = new CashRecord();
			refundRecord.setId(UUIDUtil.get32UUID());
			refundRecord.setMemberId(order.getMemberId());
			refundRecord.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_3);
			refundRecord.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
			refundRecord.setIncome(totalMoney);
			refundRecord.setTradeNo(order.getTradeNo());
			refundRecord.setOrderId(order.getId());
			refundRecord.setCitycode(member.getCitycode());
			refundRecord.setCreateBy(member.getId());
			refundRecord.setUpdateBy(member.getId());
			refundRecord.setTargetId(member.getId());
			refundRecord.setTargetName(member.getName());
			cashRecordDao.addEntity(refundRecord);
			
			//更新买家账户余额
			Account acc = accountDao.queryAccountByMemberId(member.getId());
			acc.setMoney(acc.getMoney().add(totalMoney));
			accountDao.updateEntity(acc);
			
			//更新订单状态
			order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_CANCEL);
			order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_CANCEL_REFUNDED);
			orderDao.updateEntity(order);
			
			//更新中央账户记录
			AccountCenterDetail accountCenterDetail = new AccountCenterDetail();
			accountCenterDetail.setOrderId(order.getId());
			accountCenterDetail = (AccountCenterDetail) accountCenterDetailDao.queryEntity(accountCenterDetail);
			if(null==accountCenterDetail){
				throw new ServiceException("未发现入账记录,订单异常");
			}
			accountCenterDetail.setStatus(Global.SYS_MEMBER_ACCOUNTCENTER_STATUS_3);
			accountCenterDetailDao.updateEntity(accountCenterDetail);
		}
	}


	@Override
	public void acceptRefund(String memberId,String orderId) throws Exception {
		Order order = (Order) orderDao.queryEntityById(orderId);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getSellerId())){
			throw new ServiceException("订单商户与接单商户不一致");
		}
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_APPLYING){
			throw new ServiceException("订单状态异常，无法接受退款");
		}
		Member member = (Member) memberDao.queryEntityById(order.getMemberId());
		BigDecimal totalMoney = order.getPrice().multiply(new BigDecimal(order.getCount()));
		//付款方添加退款记录
		CashRecord refundRecord = new CashRecord();
		refundRecord.setId(UUIDUtil.get32UUID());
		refundRecord.setMemberId(order.getMemberId());
		refundRecord.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_3);
		refundRecord.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
		refundRecord.setIncome(totalMoney);
		refundRecord.setTradeNo(order.getTradeNo());
		refundRecord.setOrderId(order.getId());
		refundRecord.setCitycode(member.getCitycode());
		refundRecord.setCreateBy(member.getId());
		refundRecord.setUpdateBy(member.getId());
		refundRecord.setTargetId(member.getId());
		refundRecord.setTargetName(member.getName());
		cashRecordDao.addEntity(refundRecord);
		//更新订单状态
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_CANCEL_REFUNDED);
		order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_APPLY_REFUND);
		orderDao.updateEntity(order);
		//更新中央账户记录
		AccountCenterDetail accountCenterDetail = new AccountCenterDetail();
		accountCenterDetail.setOrderId(order.getId());
		accountCenterDetail = (AccountCenterDetail) accountCenterDetailDao.queryEntity(accountCenterDetail);
		if(null==accountCenterDetail){
			throw new ServiceException("未发现入账记录,订单异常");
		}
		accountCenterDetail.setStatus(Global.SYS_MEMBER_ACCOUNTCENTER_STATUS_3);
		accountCenterDetailDao.updateEntity(accountCenterDetail);
	}

	@Override
	public void rejectRefund(String memberId,String orderId) throws Exception {
		Order order = (Order) orderDao.queryEntityById(orderId);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getSellerId())){
			throw new ServiceException("订单商户与拒绝退款商户不一致");
		}
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_APPLYING){
			throw new ServiceException("订单状态异常，无法拒绝退款");
		}
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_RIGHTING);
		order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_APPLY_REJECT);
		orderDao.updateEntity(order);
	}

	@Override
	public void applyApprove(String memberId, String tradeNo, BigDecimal money,
			String reason, String links) throws Exception {
		Order order = new Order();
		order.setTradeNo(tradeNo);
		order = (Order) orderDao.queryEntity(order);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getSellerId())){
			throw new ServiceException("订单商户与举证商家用户不一致");
		}
		//买方申请维权后才能进行举证
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_RIGHTED&&
				order.getSellerStatus()!=Global.SYS_ORDER_SELLER_STATUS_APPROVING
				){
			throw new ServiceException("订单状态异常,不能维权");
		}
		//添加举证申请
		RefundApprove refund = new RefundApprove();
		refund.setBuyerId(order.getMemberId());
		refund.setCreateBy(memberId);
		refund.setId(UUIDUtil.get32UUID());
		refund.setLinks(links);
		refund.setOrderId(order.getId());
		refund.setReason(reason);
		refund.setSellerId(order.getSellerId());
		refund.setTradeNo(tradeNo);
		refund.setMoney(money);
		refund.setType(Global.SYS_MEMBER_ORDER_APPROVE);
		refund.setUpdateBy(memberId);
		refundApproveDao.addEntity(refund);
		//更新订单状态
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_RIGHTED);
		order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_APPROVED);
		orderDao.updateEntity(order);
		
	}


	@Override
	public void finishOrder(String memberId,String orderId) throws Exception {
		Order order = (Order) orderDao.queryEntityById(orderId);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getSellerId())){
			throw new ServiceException("订单商户与完成订单商户不一致");
		}
		//订单状态都为已接单可完成订单
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_ACCEPTED&&
				order.getSellerStatus()!=Global.SYS_ORDER_SELLER_STATUS_ACCEPTED
				){
			throw new ServiceException("订单状态异常,不能完成订单");
		}
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_SIGNING);
		order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_FINISH);
		orderDao.updateEntity(order);
		
		//从中央账户给商家付款
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> orderList(String memberId,String sellerId,List<Integer> buyerStatus,List<Integer> sellerStatus,Page page)throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("memberId", memberId);
			map.put("sellerId", sellerId);
			map.put("sellerStatus", sellerStatus);
			map.put("buyerStatus", buyerStatus);
			map.put("page", page);
			return (List<Order>) orderDao.queryPageEntity(map);
		
	}


	@Override
	public void commentOrder(String memberId, String bizId) {
		Order order = (Order) orderDao.queryEntityById(bizId);
		if(null==order){
			throw new ServiceException("订单不存在");
		}
		if(!memberId.equals(order.getMemberId())){
			throw new ServiceException("评价用户与订单用户不一致");
		}
		//订单状态都为已接单可完成订单
		if(order.getBuyerStatus()!=Global.SYS_ORDER_BUYER_STATUS_COMMENTING
				){
			throw new ServiceException("订单状态异常,不能评论订单");
		}
		order.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_CLOSED);
		order.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_CLOSED);
		orderDao.updateEntity(order);
	}
	
	

}
