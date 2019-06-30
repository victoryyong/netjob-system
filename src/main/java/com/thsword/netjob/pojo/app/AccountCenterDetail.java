package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 

 * @Description:中央账户详细

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
@ApiModel("中央账户详细")
public class AccountCenterDetail {
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 金额
	 * */
	@ApiModelProperty(value="金额")
	private BigDecimal money;
	/**
	 * 服务费
	 */
	@ApiModelProperty(value="服务费")
	private BigDecimal tip;
	/**
	 * 订单ID
	 */
	@ApiModelProperty(value="订单ID")
	private String orderId;
	/**
	 * 交易单号
	 */
	@ApiModelProperty(value="交易单号")
	private String tradeNo;
	/**
	 * 卖家ID
	 */
	@ApiModelProperty(value="卖家ID")
	private String sellerId;
	/**
	 * 买家ID
	 */
	@ApiModelProperty(value="买家ID")
	private String buyerId;
	/**
	 * 状态
	 */
	@ApiModelProperty(value="状态")
	private Integer status;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value="创建时间")
	private Date createDate;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value="更新时间")
	private Date updateDate;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value="创建人")
	private String createBy;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value="更新人")
	private String updateBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public BigDecimal getTip() {
		return tip;
	}
	public void setTip(BigDecimal tip) {
		this.tip = tip;
	}
}
