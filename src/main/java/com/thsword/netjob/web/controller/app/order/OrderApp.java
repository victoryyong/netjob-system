package com.thsword.netjob.web.controller.app.order;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Order;
import com.thsword.netjob.service.NewsService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;

@Controller
public class OrderApp {
	@Resource(name = "newsService")
	NewsService newsService;

	/**
	 * 下单
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/order")
	public void list(HttpServletRequest request, HttpServletResponse response,Order order) throws Exception {
		try {
			String citycode=request.getAttribute("citycode")+"";
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(order.getAddressId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "地址ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getSellerId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "商户ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getCount())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "订单数目不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getPrice())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "单价不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getServeId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getServeTitle())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务标题不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getServeImage())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务图片不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getFirstMenuName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务一级类型名不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(order.getMenuName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务二级类型名不能为空", response, request);
				return;
			}
			order.setId(UUIDUtil.get32UUID());
			//order.setFlowId(UUIDUtil.get32FlowID());
			order.setStatus(Global.SYS_ORDER_STATUS_PAYING);
			//order.setOrderCode(UUIDUtil.get32ORDERID());
			order.setCitycode(citycode);
			order.setMemberId(memberId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
