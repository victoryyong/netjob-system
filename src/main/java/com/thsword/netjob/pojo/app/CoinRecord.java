package com.thsword.netjob.pojo.app;

import java.util.Date;

/**
 * 

 * @Description:交易记录

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class CoinRecord {
	private String id;
	/**
	 * 用户ID
	 */
	private String memberId;
	/**
	 * 收款或付款人ID
	 */
	private String targetId;
	/**
	 * 收款或付款人名称
	 */
	private String targetName;
	/**
	 * 流水号
	 */
	private String tradeNo;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 收入
	 */
	private long income;
	/**
	 * 支出
	 */
	private long outcome;
	/**
	 * 交易类型(1充值 2打赏)
	 */
	private Integer recordType;
	/**
	 * 1-收入 2-支出
	 */
	private Integer isIn;
	/**
	 * 城市
	 */
	private String citycode;
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
	public long getIncome() {
		return income;
	}
	public void setIncome(long income) {
		this.income = income;
	}
	public long getOutcome() {
		return outcome;
	}
	public void setOutcome(long outcome) {
		this.outcome = outcome;
	}
	public Integer getRecordType() {
		return recordType;
	}
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
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
