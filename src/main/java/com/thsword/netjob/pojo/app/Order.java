package com.thsword.netjob.pojo.app;

import java.util.Date;

/**
 * 

 * @Description:订单

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class Order {
	private String id;
	/**
	 * 用户ID
	 */
	private String memberId;
	/**
	 * 订单号
	 */
	private String orderCode;
	/**
	 *服务ID 
	 */
	private String serveId;
	/**
	 * 服务地址ID
	 */
	private String addressId;
	/**
	 * 服务标题
	 */
	private String title;
	/**
	 * 单价
	 */
	private double price;
	/**
	 * 数量
	 */
	private Integer count;
	/**
	 * 城市编码
	 */
	private String citycode;
	/**
	 * 备注说明
	 */
	private String remark;
	/**
	 * 说明图文链接
	 */
	private String remarkLink;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 更新人
	 */
	private String updateBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getServeId() {
		return serveId;
	}
	public void setServeId(String serveId) {
		this.serveId = serveId;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getRemarkLink() {
		return remarkLink;
	}
	public void setRemarkLink(String remarkLink) {
		this.remarkLink = remarkLink;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
