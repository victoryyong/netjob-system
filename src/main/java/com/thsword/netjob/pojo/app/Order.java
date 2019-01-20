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
	 * 商家ID
	 */
	private String sellerId;
	/**
	 * 商家名称
	 */
	private String sellerName;
	/**
	 * 商家图片
	 */
	private String sellerImage;
	/**
	 * 订单号
	 */
	private String orderCode;
	/**
	 * 付款单号
	 */
	private String flowId;
	/**
	 *服务ID 
	 */
	private String serveId;
	/**
	 * 服务标题
	 */
	private String serveTitle;
	/**
	 * 服务标题
	 */
	private String serveImage;
	/**
	 * 服务标题
	 */
	private String firstMenuName;
	/**
	 * 服务标题
	 */
	private String menuName;
	/**
	 * 地址ID
	 */
	private String addressId;
	/**
	 * 单价
	 */
	private double price;
	/**
	 * 数量
	 */
	private Integer count;
	/**
	 * 业务方状态
	 */
	private Integer status;
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
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getServeTitle() {
		return serveTitle;
	}
	public void setServeTitle(String serveTitle) {
		this.serveTitle = serveTitle;
	}
	public String getServeImage() {
		return serveImage;
	}
	public void setServeImage(String serveImage) {
		this.serveImage = serveImage;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerImage() {
		return sellerImage;
	}
	public void setSellerImage(String sellerImage) {
		this.sellerImage = sellerImage;
	}
	public String getFirstMenuName() {
		return firstMenuName;
	}
	public void setFirstMenuName(String firstMenuName) {
		this.firstMenuName = firstMenuName;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
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
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
}
