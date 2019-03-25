package com.thsword.netjob.web.controller.app.order;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IOrderDao;
import com.thsword.netjob.pojo.app.Order;
import com.thsword.netjob.service.OrderService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.page.Page;

@Controller
public class OrderApp {
	@Resource(name = "orderService")
	OrderService orderService;
	private static final Log log = LogFactory.getLog(OrderApp.class);

	/**
	 * 买家-下单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/doOrder")
	public void doOrder(HttpServletRequest request, HttpServletResponse response,Order order) throws Exception {
		try {
			String citycode=request.getAttribute("citycode")+"";
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(order.getServeId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务id不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getAddressId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "地址ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getPrice())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "订单单价不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getCount())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "订单数目不能为空", response, request);
				return;
			}
			order.setCitycode(citycode);
			order.setMemberId(memberId);
			Order buildOrder = orderService.doServeOrder(order);
			JsonResponseUtil.successBodyResponse(buildOrder,response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"下单异常",response, request);
		}
	}
	
	/**
	 * 买家-支付订单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/payOrders")
	public void payOrders(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String orderIds = request.getParameter("orderIds");
			String password = request.getParameter("password");
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(orderIds)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "订单id不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(password)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "密码不能为空", response, request);
				return;
			}
			orderService.payOrders(memberId,orderIds,password);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"支付订单异常",response, request);
		}
	}
	
	/**
	 * 买家-签单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/signOrders")
	public void signOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String orderIds = request.getParameter("orderIds");
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(orderIds)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "订单id不能为空", response, request);
				return;
			}
			orderService.signOrders(memberId,orderIds);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"支付订单异常",response, request);
		}
	}
	
	/**
	 * 买家-申请退款
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/applyRefund")
	public void applyRefund(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String tradeNo = request.getParameter("tradeNo");
			String money = request.getParameter("money");
			String reason = request.getParameter("reason");
			String links = request.getParameter("links");
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(tradeNo)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "业务单号不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(money)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "金额不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(reason)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "原因不能为空", response, request);
				return;
			}
			orderService.applyRefund(memberId,tradeNo,new BigDecimal(money),reason,links);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"申请退款异常",response, request);
		}
	}
	
	/**
	 * 买家-申请维权
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/applyRight")
	public void applyRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String tradeNo = request.getParameter("tradeNo");
			String money = request.getParameter("money");
			String reason = request.getParameter("reason");
			String links = request.getParameter("links");
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(tradeNo)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "业务单号不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(money)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "金额不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(reason)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "原因不能为空", response, request);
				return;
			}
			orderService.applyRight(memberId,tradeNo,new BigDecimal(money),reason,links);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"申请维权异常",response, request);
		}
	}
	
	/**
	 * 商家-我的接单列表
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/bizOrderList")
	public void bizOrderList(HttpServletRequest request, HttpServletResponse response,Page page,@RequestParam(required=false,value="status") Integer status) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			List<Order> orders = orderService.orderList(null,memberId,null,status,page);
			JSONObject obj = new JSONObject();
			obj.put("page", page);
			obj.put("list", orders);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"查询业务订单异常",response, request);
		}
	}
	/**
	 * 商家-接单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/acceptOrders")
	public void acceptOrder(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="orderIds") String orderIds) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			orderService.acceptOrders(memberId,orderIds);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"接单异常",response, request);
		}
	}
	
	/**
	 * 商家-拒单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/cancelOrders")
	public void cancelOrder(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="orderIds") String orderIds) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			orderService.cancelOrders(memberId,orderIds);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"拒单异常",response, request);
		}
	}
	
	/**
	 * 商家-拒绝退款
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/rejectRefund")
	public void rejectRefund(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="orderId") String orderId) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			orderService.rejectRefund(memberId,orderId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"拒绝退款异常",response, request);
		}
	}
	/**
	 * 商家-接受退款
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/acceptRefund")
	public void acceptRefund(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="orderId") String orderId) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			orderService.acceptRefund(memberId,orderId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"接受退款异常",response, request);
		}
	}
	
	/**
	 * 商家-完成订单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/finishOrder")
	public void finishOrder(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="orderId") String orderId) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			orderService.finishOrder(memberId,orderId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"完成订单异常",response, request);
		}
	}
	
	/**
	 * 商家-举证
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/applyApprove")
	public void applyApprove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String tradeNo = request.getParameter("tradeNo");
			String money = request.getParameter("money");
			String reason = request.getParameter("reason");
			String links = request.getParameter("links");
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(tradeNo)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "业务单号不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(money)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "金额不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(reason)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "原因不能为空", response, request);
				return;
			}
			orderService.applyApprove(memberId,tradeNo,new BigDecimal(money),reason,links);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"举证异常",response, request);
		}
	}
	
	
	/**
	 * 我的-订单列表
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/orderList")
	public void orderList(HttpServletRequest request, HttpServletResponse response,Page page,@RequestParam(required=false,value="status") Integer status) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			List<Order> orders = orderService.orderList(memberId,null,status,null,page);
			JSONObject obj = new JSONObject();
			obj.put("page", page);
			obj.put("list", orders);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"查询订单列表异常",response, request);
		}
	}
	
	/**
	 * 订单详情
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order/orderDetail")
	public void orderDetail(HttpServletRequest request, HttpServletResponse response,Page page,@RequestParam(value="orderId") String orderId) throws Exception {
		try {
			Order order = (Order) orderService.queryEntityById(IOrderDao.class, orderId);
			JsonResponseUtil.successBodyResponse(order, response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"查询订单详情异常",response, request);
		}
	}
}
