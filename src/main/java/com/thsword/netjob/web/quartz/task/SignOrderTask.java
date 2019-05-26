package com.thsword.netjob.web.quartz.task;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class SignOrderTask extends QuartzJobBean{
	
	private static final Log log = LogFactory.getLog(SignOrderTask.class);
	
	@Autowired
	OrderService orderService;
	@Autowired
	IOrderDao orderDao;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			log.info("begin cancel unaccepted orders");
			//待签收订单自动签收
			Order temp = new Order();
			temp.setSellerStatus(Global.SYS_ORDER_SELLER_STATUS_FINISH);
			List<Order> orders = (List<Order>) orderDao.queryAllEntity(temp);
			log.info("unSigned orders is:"+JSONObject.toJSONString(orders));
			if(CollectionUtils.isNotEmpty(orders)){
				for (Order order : orders) {
					if (DateTime.now().isAfter(new DateTime(order.getCreateDate()).plusDays(20))){
						JSONArray orderIds = new JSONArray();
						orderIds.add(order.getId());
						orderService.signOrders(order.getMemberId(), orderIds.toJSONString());
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
