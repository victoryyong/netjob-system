package com.thsword.netjob.pojo.app;

import java.util.Date;

/**
 * 

 * @Description:交易记录

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class Record {
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
	private String flowId;
	/**
	 * 交易金额
	 * */
	private String money;
	/**
	 * 交易类型(1收入 2支出)
	 */
	private Integer recordType;
	/**
	 * 账户类型(1现金 2网币)
	 */
	private Integer accountType;
	/**
	 * 支付方式 (1-现金账户 2-网币账户 3-微信 4-支付宝 5-银行账户)
	 */
	private Integer payType;
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
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
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
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
	public Integer getRecordType() {
		return recordType;
	}
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}
}
