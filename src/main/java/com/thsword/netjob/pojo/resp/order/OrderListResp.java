package com.thsword.netjob.pojo.resp.order;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Order;
import com.thsword.utils.page.Page;

@Data
@Builder
public class OrderListResp implements Serializable{
	
	Page page;
	
	List<Order> list;

}
