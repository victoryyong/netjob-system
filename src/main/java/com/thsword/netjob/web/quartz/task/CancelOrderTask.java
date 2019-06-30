package com.thsword.netjob.web.quartz.task;

import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IOrderDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Order;
import com.thsword.netjob.service.OrderService;
/**
 * 签收订单
 * @author Lenovo
 *
 */
@Log4j2
public class CancelOrderTask extends QuartzJobBean{
	
	
	@Autowired
	OrderService orderService;
	@Autowired
	IOrderDao orderDao;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			log.info("begin cancel unaccepted orders");
			//待接单订单自动取消订单
			Order temp = new Order();
			temp.setBuyerStatus(Global.SYS_ORDER_BUYER_STATUS_PAYED);
			List<Order> orders = (List<Order>) orderDao.queryAllEntity(temp);
			log.info("unaccepted orders is:"+JSONObject.toJSONString(orders));
			if(CollectionUtils.isNotEmpty(orders)){
				for (Order order : orders) {
					if (DateTime.now().isAfter(new DateTime(order.getCreateDate()).plusDays(3))){
						JSONArray orderIds = new JSONArray();
						orderIds.add(order.getId());
						orderService.cancelOrders(order.getSellerId(), orderIds.toJSONString());
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
