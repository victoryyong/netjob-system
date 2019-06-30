package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @ClassName: RefundApprove
 * @Description: TODO(退款/维权/举证)
 * @author yong
 * @date 2017年5月13日 下午3:26:16
 *
 */
@ApiModel(value = "退款/维权/举证")
public class RefundApprove implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value = "ID")
	private String id;
	/**
	 * 买家ID
	 */
	@ApiModelProperty(value = "买家ID")
	private String buyerId;
	/**
	 * 卖家ID
	 */
	@ApiModelProperty(value = "卖家ID")
	private String sellerId;
	/**
	 * 订单ID
	 */
	@ApiModelProperty(value = "订单ID")
	private String orderId;
	/**
	 * 交易号
	 */
	@ApiModelProperty(value = "交易号")
	private String tradeNo;
	/**
	 * 金额
	 */
	@ApiModelProperty(value = "金额")
	private BigDecimal money;
	/**
	 * 原因
	 */
	@ApiModelProperty(value = "原因")
	private String reason;
	/**
	 * 1-退款/2-维权/3-举证
	 */
	@ApiModelProperty(value = "1-退款/2-维权/3-举证 ")
	private Integer type;
	/**
	 * 文件链接
	 */
	@ApiModelProperty(value = "文件链接")
	private String links;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateDate;
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

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
}
