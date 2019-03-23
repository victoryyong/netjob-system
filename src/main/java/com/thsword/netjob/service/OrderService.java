package com.thsword.netjob.service;

import java.util.List;

import com.thsword.netjob.pojo.app.Order;
import com.thsword.utils.page.Page;

public interface OrderService extends IBaseService{
	/**
	 * 买家-服务下单
	 * @param serve
	 * @param serveId
	 */
	public Order doServeOrder(Order order)  throws Exception;
	
	/**
	 * 买家-付款订单
	 */
	public void payOrders(String memberId,String orderIds,String password) throws Exception;
	/**
	 * 买家、商家-查询订单列表
	 * @param memberId
	 * @param page
	 * @return
	 */
	public List<Order> orderList(String memberId,String sellerId,Integer status,Page page)throws Exception;
	/**
	 * 商家-接单
	 * @param orderIds
	 */
	public void acceptOrders(String orderIds)throws Exception;
	/**
	 * 商家-拒单
	 * @param orderIds
	 * @throws Exception
	 */
	void rejectOrders(String orderIds) throws Exception;
}
