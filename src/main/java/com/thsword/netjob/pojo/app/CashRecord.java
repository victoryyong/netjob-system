package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 

 * @Description:账户交易记录

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
@ApiModel(value="账户交易记录 ")
public class CashRecord {
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value="用户ID")
	private String memberId;
	/**
	 * 收款或付款人ID
	 */
	@ApiModelProperty(value="收款或付款人ID")
	private String targetId;
	/**
	 * 收款或付款人名称
	 */
	@ApiModelProperty(value="收款或付款人名称")
	private String targetName;
	/**
	 * 收入
	 */
	@ApiModelProperty(value="收入")
	private BigDecimal income;
	/**
	 * 支出
	 */
	@ApiModelProperty(value="支出")
	private BigDecimal outcome;
	/**
	 * 交易号
	 */
	@ApiModelProperty(value="交易号")
	private String tradeNo;
	/**
	 * 订单ID
	 */
	@ApiModelProperty(value="订单ID")
	private String orderId;
	/**
	 * 交易类型
	 */
	@ApiModelProperty(value="交易类型")
	private Integer recordType;
	/**
	 * 1-收入 2-支出
	 */
	@ApiModelProperty(value="1-收入 2-支出")
	private Integer isIn;
	/**
	 * 城市
	 */
	@ApiModelProperty(value="城市")
	private String citycode;
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public Integer getRecordType() {
		return recordType;
	}
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public BigDecimal getOutcome() {
		return outcome;
	}
	public void setOutcome(BigDecimal outcome) {
		this.outcome = outcome;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
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
	public Integer getIsIn() {
		return isIn;
	}
	public void setIsIn(Integer isIn) {
		this.isIn = isIn;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
