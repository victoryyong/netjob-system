package com.thsword.netjob.web.controller.app.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.thsword.netjob.dao.IOrderDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Order;
import com.thsword.netjob.pojo.resp.order.OrderListResp;
import com.thsword.netjob.service.OrderService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-ORDER", description = "订单接口")
public class OrderApp {
	@Resource(name = "orderService")
	OrderService orderService;

	/**
	 * 买家-下单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@ApiOperation(value = "买家-下单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "serveId", value = "菜单ID", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "price", value = "菜单ID", dataType = "int", paramType = "query", required = true),
			@ApiImplicitParam(name = "count", value = "菜单ID", dataType = "int", paramType = "query", required = true) })
	@RequestMapping("app/member/order/doOrder")
	public Order doOrder(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String serveId,
			@RequestParam BigDecimal price, Integer count) throws Exception {
		String citycode = request.getAttribute("citycode") + "";
		String memberId = request.getAttribute("memberId") + "";
		Order order = new Order();
		order.setServeId(serveId);
		order.setPrice(price);
		order.setCount(count);
		order.setCitycode(citycode);
		order.setMemberId(memberId);
		Order buildOrder = orderService.doServeOrder(order);
		return buildOrder;
	}

	/**
	 * 买家-删除订单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/deleteOrder")
	@ApiOperation(value = "买家-删除订单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "string", paramType = "query", required = true) })
	public BaseResponse deleteOrder(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String orderId)
			throws Exception {
		if (StringUtils.isEmpty(orderId)) {
			throw new ServiceException("订单id不能为空");
		}
		Order order = (Order) orderService.queryEntityById(IOrderDao.class,
				orderId);
		if (null == order) {
			throw new ServiceException("订单不存在");
		}
		if (order.getBuyerStatus() != Global.SYS_ORDER_BUYER_STATUS_PAYING) {
			throw new ServiceException("禁止删除");
		}
		orderService.deleteEntityById(IOrderDao.class, orderId);
		return BaseResponse.success();
	}

	/**
	 * 买家-支付订单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/payOrders")
	@ApiOperation(value = "买家-支付订单", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderIds", value = "订单IDS", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", required = true) })
	public BaseResponse payOrders(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String orderIds,
			@RequestParam String password) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (StringUtils.isEmpty(orderIds)) {
			throw new ServiceException("订单id不能为空");
		}
		if (StringUtils.isEmpty(password)) {
			throw new ServiceException("密码不能为空");
		}
		orderService.payOrders(memberId, orderIds, password);
		return BaseResponse.success();
	}

	/**
	 * 买家-签单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/signOrders")
	@ApiOperation(value = "买家-签单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderIds", value = "订单IDS", dataType = "string", paramType = "query", required = true), })
	public BaseResponse signOrder(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String orderIds)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (StringUtils.isEmpty(orderIds)) {
			throw new ServiceException("订单id不能为空");
		}
		orderService.signOrders(memberId, orderIds);
		return BaseResponse.success();
	}

	/**
	 * 买家-申请退款
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/applyRefund")
	@ApiOperation(value = "买家-申请退款", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tradeNo", value = "交易号", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "money", value = "订单金额", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "reason", value = "原因", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "links", value = "文件链接", dataType = "string", paramType = "query"), })
	public BaseResponse applyRefund(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String tradeNo,
			@RequestParam String money, @RequestParam String reason,
			@RequestParam(required = false) String links) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (StringUtils.isEmpty(tradeNo)) {
			throw new ServiceException("业务单号不能为空");
		}
		if (StringUtils.isEmpty(money)) {
			throw new ServiceException("金额不能为空");
		}
		if (StringUtils.isEmpty(reason)) {
			throw new ServiceException("原因不能为空");
		}
		orderService.applyRefund(memberId, tradeNo, new BigDecimal(money),
				reason, links);
		JsonResponseUtil.successCodeResponse(response, request);
		return BaseResponse.success();
	}

	/**
	 * 买家-申请维权
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/applyRight")
	@ApiOperation(value = "买家-申请维权", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tradeNo", value = "交易号", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "money", value = "订单金额", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "reason", value = "原因", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "links", value = "文件链接", dataType = "string", paramType = "query"), })
	public BaseResponse applyRight(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String tradeNo,
			@RequestParam String money, @RequestParam String reason,
			@RequestParam(required = false) String links) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (StringUtils.isEmpty(tradeNo)) {
			throw new ServiceException("业务单号不能为空");
		}
		if (StringUtils.isEmpty(money)) {
			throw new ServiceException("金额不能为空");
		}
		if (StringUtils.isEmpty(reason)) {
			throw new ServiceException("原因不能为空");
		}
		orderService.applyRight(memberId, tradeNo, new BigDecimal(money),
				reason, links);
		return BaseResponse.success();
	}

	/**
	 * 商家-我的接单列表
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/bizOrderList")
	@ApiOperation(value = "商家-我的接单列表", httpMethod = "POST")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "status", value = "状态", dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "statusList", value = "状态列表", dataType = "String", paramType = "query"),
		})
	public OrderListResp bizOrderList(HttpServletRequest request,
			HttpServletResponse response, Page page,
			@RequestParam(required = false, value = "status") Integer status,
			@RequestParam(required = false, value = "status") List<Integer> statusList)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if(null!=status && 0!=status){
			statusList.add(status);
		}
		List<Order> orders = orderService.orderList(null, memberId, null,
				statusList, page);
		return OrderListResp.builder().list(orders).page(page).build();
	}

	/**
	 * 商家-接单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/acceptOrders")
	@ApiOperation(value = "商家-接单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderIds", value = "订单IDS", dataType = "String", paramType = "query", required = true), })
	public BaseResponse acceptOrder(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "orderIds") String orderIds) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		orderService.acceptOrders(memberId, orderIds);
		return BaseResponse.success();
	}

	/**
	 * 商家-拒单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/cancelOrders")
	@ApiOperation(value = "商家-拒单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderIds", value = "订单IDS", dataType = "String", paramType = "query", required = true), })
	public BaseResponse cancelOrder(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "orderIds") String orderIds) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		orderService.cancelOrders(memberId, orderIds);
		return BaseResponse.success();
	}

	/**
	 * 商家-拒绝退款
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/rejectRefund")
	@ApiOperation(value = "商家-拒单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "query", required = true), })
	public BaseResponse rejectRefund(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "orderId") String orderId) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		orderService.rejectRefund(memberId, orderId);
		return BaseResponse.success();
	}

	/**
	 * 商家-接受退款
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/acceptRefund")
	@ApiOperation(value = "商家-接受退款", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "query", required = true), })
	public BaseResponse acceptRefund(HttpServletRequest request,
			@RequestParam(value = "orderId") String orderId) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		orderService.acceptRefund(memberId, orderId);
		return BaseResponse.success();
	}

	/**
	 * 商家-完成订单
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/finishOrder")
	@ApiOperation(value = "商家-完成订单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "query", required = true), })
	public BaseResponse finishOrder(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String orderId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		orderService.finishOrder(memberId, orderId);
		return BaseResponse.success();
	}

	/**
	 * 商家-举证
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/applyApprove")
	@ApiOperation(value = "商家-举证", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tradeNo", value = "交易号", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "money", value = "订单金额", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "reason", value = "原因", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "links", value = "文件链接", dataType = "string", paramType = "query"), })
	public BaseResponse applyApprove(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String tradeNo,
			@RequestParam String money, @RequestParam String reason,
			@RequestParam(required = false) String links) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		if (StringUtils.isEmpty(tradeNo)) {
			throw new ServiceException("业务单号不能为空");
		}
		if (StringUtils.isEmpty(money)) {
			throw new ServiceException("金额不能为空");
		}
		if (StringUtils.isEmpty(reason)) {
			throw new ServiceException("原因不能为空");
		}
		orderService.applyApprove(memberId, tradeNo, new BigDecimal(money),
				reason, links);
		return BaseResponse.success();
	}

	/**
	 * 我的-订单列表
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/orderList")
	@ApiOperation(value = "订单详情", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query", required = false),
			@ApiImplicitParam(name = "statusList", value = "状态列表", dataType = "String", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query"), })
	public OrderListResp orderList(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "status") Integer status,
			@RequestParam(required = false, value = "statusList") List<Integer> statusList,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Page page = new Page(currentPage, pageSize);
		if(null!=status&&0!=status){
			statusList.add(status);
		}
		List<Order> orders = orderService.orderList(memberId, null, statusList,
				null, page);
		return OrderListResp.builder().list(orders).page(page).build();
	}

	/**
	 * 订单详情
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/orderDetail")
	@ApiOperation(value = "订单详情", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "query", required = true), })
	public Order orderDetail(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "orderId") String orderId) throws Exception {
		Order order = (Order) orderService.queryEntityById(IOrderDao.class,
				orderId);
		return order;
	}
}
