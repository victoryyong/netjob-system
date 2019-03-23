package com.thsword.netjob.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.thsword.netjob.dao.IAccountCenterDao;
import com.thsword.netjob.dao.IAccountDao;
import com.thsword.netjob.dao.IAddressDao;
import com.thsword.netjob.dao.ICashRecordDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IOrderDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Account;
import com.thsword.netjob.pojo.app.AccountCenter;
import com.thsword.netjob.pojo.app.Address;
import com.thsword.netjob.pojo.app.CashRecord;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Order;
import com.thsword.netjob.pojo.app.OrderAccount;
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
		Address address = (Address) addressDao.queryEntityById(addressId);
		if(null == address){
			throw new ServiceException("地址不存在");
		}
		Member seller = (Member) memberDao.queryEntityById(sellerId);
		if(null==seller){
			throw new ServiceException("商家不存在");
		}
		Account pwdAccount = accountDao.queryPwdMember(memberId);
		if(null==pwdAccount){
			throw new ServiceException("账户异常");
		}
		order.setAddress(address.getProvinceName()+"-"+address.getCityName()+"-"+address.getArea()+" "+address.getDetailAddress());
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
		order.setStatus(Global.SYS_ORDER_STATUS_PAYING);
		order.setCreateBy(memberId);
		order.setUpdateBy(memberId);
		orderDao.addEntity(order);
		return order;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Order> orderList(String memberId,String sellerId,Integer status,Page page)throws Exception{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("memberId", memberId);
			map.put("sellerId", sellerId);
			map.put("status", status);
			map.put("page", page);
			return (List<Order>) orderDao.queryPageEntity(map);
		
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
			if(order.getStatus()!=Global.SYS_ORDER_STATUS_PAYING){
				throw new ServiceException("订单状态已更新不能付款");
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
			order.setStatus(Global.SYS_ORDER_STATUS_PAYED);
			orderDao.updateEntity(order);
			
			//添加中央账户记录
			AccountCenter accountCenter = new AccountCenter();
			accountCenter.setId(UUIDUtil.get32UUID());
			accountCenter.setBuyerId(order.getMemberId());
			accountCenter.setCreateBy("SYSTEM");
			accountCenter.setMoney(orderMoney);
			accountCenter.setOrderId(order.getId());
			accountCenter.setSellerId(sellerId);
			accountCenter.setStatus(Global.SYS_MEMBER_ACCOUNTCENTER_STATUS_1);
			accountCenter.setUpdateBy("SYSTEM");
			accountCenter.setTip(orderMoney.multiply(new BigDecimal(Global.getSetting(Global.SYS_MEMBER_SERVE_TIP_RATE))));
			accountCenterDao.addEntity(accountCenter);
		}
		
	}

	@Transactional
	@Override
	public void acceptOrders(String orderIds) throws Exception {
		JSONArray idArray = JSONArray.parseArray(orderIds);
		for (Object orderIdObj : idArray) {
			Order order = (Order) orderDao.queryEntityById(orderIdObj.toString());
			if(null==order){
				throw new ServiceException("订单不存在"+orderIdObj.toString());
			}
			if(order.getStatus()!=Global.SYS_ORDER_STATUS_PAYED){
				throw new ServiceException("订单状态已更新不能接单");
			}
			order.setStatus(Global.SYS_ORDER_STATUS_ACCEPTED);
			orderDao.updateEntity(order);
		}
	}
	
	@Transactional
	@Override
	public void rejectOrders(String orderIds) throws Exception {
		JSONArray idArray = JSONArray.parseArray(orderIds);
		for (Object orderIdObj : idArray) {
			Order order = (Order) orderDao.queryEntityById(orderIdObj.toString());
			BigDecimal totalMoney = order.getPrice().multiply(new BigDecimal(order.getCount()));
			if(null==order){
				throw new ServiceException("订单不存在");
			}
			if(order.getStatus()!=Global.SYS_ORDER_STATUS_PAYED&&order.getStatus()!=Global.SYS_ORDER_STATUS_PAYING){
				throw new ServiceException("订单状态已更新不能拒单");
			}
			//已支付的退款
			if(order.getStatus()==Global.SYS_ORDER_STATUS_PAYED){
				Member member = (Member) memberDao.queryEntityById(order.getMemberId());
				//付款方添加付款记录
				CashRecord delRecord = new CashRecord();
				delRecord.setId(UUIDUtil.get32UUID());
				delRecord.setMemberId(order.getMemberId());
				delRecord.setRecordType(Global.SYS_MEMBER_ACCOUNT_RECORD_TYPE_3);
				delRecord.setIsIn(Global.SYS_MEMBER_ACCOUNT_ISIN_1);
				delRecord.setIncome(order.getPrice());
				delRecord.setTradeNo(order.getTradeNo());
				delRecord.setOrderId(order.getId());
				delRecord.setCitycode(member.getCitycode());
				delRecord.setCreateBy(member.getId());
				delRecord.setUpdateBy(member.getId());
				delRecord.setTargetId(member.getId());
				delRecord.setTargetName(member.getName());
				cashRecordDao.addEntity(delRecord);
				
				//更新买家账户余额
				Account acc = accountDao.queryAccountByMemberId(member.getId());
				acc.setMoney(acc.getMoney().add(order.getPrice().multiply(new BigDecimal(order.getCount()))));
				accountDao.updateEntity(acc);
				
				//更新订单状态
				order.setStatus(Global.SYS_ORDER_STATUS_REJECT_REFUNDED);
				orderDao.updateEntity(order);
				
				//更新中央账户记录
				AccountCenter accountCenter = new AccountCenter();
				accountCenter.setOrderId(order.getId());
				accountCenter = (AccountCenter) accountCenterDao.queryEntity(accountCenter);
				if(null==accountCenter){
					throw new ServiceException("未发现入账记录,订单异常");
				}
				accountCenter.setStatus(Global.SYS_MEMBER_ACCOUNTCENTER_STATUS_3);
				accountCenterDao.updateEntity(accountCenter);
			}else{
				order.setStatus(Global.SYS_ORDER_STATUS_REJECT);
				orderDao.updateEntity(order);
			}
		}
	}
}
