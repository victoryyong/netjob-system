package com.thsword.netjob.service;

import java.math.BigDecimal;
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
	 * 买家-签收
	 * @param memberId
	 * @param orderIds
	 * @throws Exception
	 */
	public void signOrders(String memberId, String orderIds) throws Exception;
	/**
	 * 买家-申请退款
	 * @param memberId
	 * @param tradeNo
	 * @param money
	 * @param reason
	 * @param links
	 * @throws Exception
	 */
	public void applyRefund(String memberId, String tradeNo, BigDecimal money,
			String reason, String links)throws Exception;
	/**
	 * 买家-维权
	 * @param memberId
	 * @param tradeNo
	 * @param money
	 * @param reason
	 * @param links
	 * @throws Exception
	 */
	public void applyRight(String memberId, String tradeNo, BigDecimal money,
			String reason, String links)throws Exception;
	/**
	 * 商家-接单
	 * @param orderIds
	 */
	public void acceptOrders(String memberId,String orderIds)throws Exception;
	/**
	 * 商家-完成订单
	 * @param orderId
	 */
	public void finishOrder(String memberId,String orderId)throws Exception;
	/**
	 * 商家-拒单
	 * @param orderIds
	 * @throws Exception
	 */
	void cancelOrders(String memberId,String orderIds) throws Exception;
	/**
	 * 接受退款
	 * @param orderId
	 */
	public void acceptRefund(String memberId,String orderId)throws Exception;
	/**
	 * 拒绝退款
	 * @param orderId
	 */
	public void rejectRefund(String memberId,String orderId) throws Exception;
	/**
	 * 商家-举证
	 * @param memberId
	 * @param tradeNo
	 * @param money
	 * @param reason
	 * @param links
	 * @throws Exception
	 */
	public void applyApprove(String memberId, String tradeNo, BigDecimal money,
			String reason, String links)throws Exception;
	/**
	 * 买家、商家-查询订单列表
	 * @param memberId
	 * @param sellerId
	 * @param buyerStatus
	 * @param sellerStatus
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Order> orderList(String memberId,String sellerId,Integer buyerStatus,Integer sellerStatus,Page page)throws Exception;
	
	/**
	 * 评论订单
	 * @param memberId
	 * @param bizId
	 */
	public void commentOrder(String memberId, String bizId);

}
