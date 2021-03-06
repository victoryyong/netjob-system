package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值订单
 * 
 * @author Lenovo
 *
 */
@ApiModel(value = "充值订单")
public class OrderAccount implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value = "ID")
	private String id;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private String memberId;
	/**
	 * 订单目标ID
	 */
	@ApiModelProperty(value = "订单目标ID")
	private String targetId;
	/**
	 * 交易号
	 */
	@ApiModelProperty(value = "交易号")
	private String tradeNo;
	/**
	 * 金额
	 */
	@ApiModelProperty(value = "金额")
	private BigDecimal total_fee;
	/**
	 * 网币数目
	 */
	@ApiModelProperty(value = "网币数目")
	private long num;
	/**
	 * 充值方式(1-微信 2-支付宝 3-银行卡 4-现金账户)
	 */
	@ApiModelProperty(value = "充值方式(1-微信 2-支付宝 3-银行卡 4-现金账户)")
	private Integer way;
	/**
	 * 充值类型（1-现金账户 2-保证金 3-网币）
	 */
	@ApiModelProperty(value = "充值类型（1-现金账户 2-保证金 3-网币）")
	private Integer type;
	/**
	 * 1-待支付 2-支付成功 3-支付超时 4-已关闭
	 */
	@ApiModelProperty(value = " 1-待支付 2-支付成功 3-支付超时 4-已关闭")
	private Integer status;
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

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public Integer getWay() {
		return way;
	}

	public void setWay(Integer way) {
		this.way = way;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
}
