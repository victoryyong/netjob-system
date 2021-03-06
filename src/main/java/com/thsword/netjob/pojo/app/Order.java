package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @Description:订单
 * 
 * @author:yong
 * 
 * @time:2018年5月7日 下午5:47:41
 */
@ApiModel(value = "订单")
public class Order {
	@ApiModelProperty(value = "id")
	private String id;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private String memberId;
	/**
	 * 商家ID
	 */
	@ApiModelProperty(value = "商家ID")
	private String sellerId;
	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String sellerName;
	/**
	 * 商家图片
	 */
	@ApiModelProperty(value = "商家图片")
	private String sellerImage;
	/**
	 * 交易号
	 */
	@ApiModelProperty(value = "交易号")
	private String tradeNo;
	/**
	 * 服务ID
	 */
	@ApiModelProperty(value = "服务ID")
	private String serveId;
	/**
	 * 服务标题
	 */
	@ApiModelProperty(value = "服务标题")
	private String serveTitle;
	/**
	 * 服务图片
	 */
	@ApiModelProperty(value = "服务图片")
	private String serveImage;
	/**
	 * 一级类型
	 */
	@ApiModelProperty(value = "一级类型")
	private String firstMenuId;
	/**
	 * 一级名称
	 */
	@ApiModelProperty(value = "一级名称")
	private String firstMenuName;
	/**
	 * 二级类型
	 */
	@ApiModelProperty(value = "firstMenuName")
	private String menuId;
	/**
	 * 二级名称
	 */
	@ApiModelProperty(value = "二级名称")
	private String menuName;
	/**
	 * 地址ID
	 */
	@ApiModelProperty(value = "地址ID")
	private String addressId;
	/**
	 * 地址详情
	 */
	@ApiModelProperty(value = "地址详情")
	private String address;
	/**
	 * 单价
	 */
	@ApiModelProperty(value = "单价")
	private BigDecimal price;
	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer count;
	/**
	 * 业务方状态
	 */
	@ApiModelProperty(value = "业务方状态")
	private Integer buyerStatus;
	/**
	 * 商家状态
	 */
	@ApiModelProperty(value = "商家状态")
	private Integer sellerStatus;
	/**
	 * 城市编码
	 */
	@ApiModelProperty(value = "城市编码")
	private String citycode;
	/**
	 * 备注说明
	 */
	@ApiModelProperty(value = "备注说明")
	private String remark;
	/**
	 * 说明图文链接
	 */
	@ApiModelProperty(value = "说明图文链接")
	private String remarkLink;
	/**
	 * 接单时间
	 */
	@ApiModelProperty(value = "接单时间")
	private Date acceptDate;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateDate;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
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

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getBuyerStatus() {
		return buyerStatus;
	}

	public void setBuyerStatus(Integer buyerStatus) {
		this.buyerStatus = buyerStatus;
	}

	public Integer getSellerStatus() {
		return sellerStatus;
	}

	public void setSellerStatus(Integer sellerStatus) {
		this.sellerStatus = sellerStatus;
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

	public String getFirstMenuId() {
		return firstMenuId;
	}

	public void setFirstMenuId(String firstMenuId) {
		this.firstMenuId = firstMenuId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
}
