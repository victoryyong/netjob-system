package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 

 * @Description:积分账户

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
@ApiModel(value="网币账户")
public class AccountCoin {
	@ApiModelProperty(value="ID")
	private String id;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value="用户ID")
	private String memberId;
	/**
	 * 账户余额
	 */
	@ApiModelProperty(value="账户余额")
	private Long num;
	/**
	 * 状态（1-激活 2-冻结）
	 */
	@ApiModelProperty(value="状态（1-激活 2-冻结）")
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
