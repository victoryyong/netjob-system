package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: Agree 
* @Description: TODO(点赞) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
@ApiModel(value="点赞")
public class Agree implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 用户名
	 */@ApiModelProperty(value="用户名")
	private String memberId;
	/**
	 * 点赞对象ID
	 */
	 @ApiModelProperty(value="点赞对象ID")
	private String agreeId;
	/**
	 * 对象类型 
	 */
	 @ApiModelProperty(value="对象类型 ")
	private Integer type;
	/**
	 * 创建时间
	 */
	 @ApiModelProperty(value="创建时间")
	private Date createDate;
	/**
	 * 创建人
	 */
	 @ApiModelProperty(value="创建人")
	private String createBy;
	/**
	 * 更新时间
	 */
	 @ApiModelProperty(value="更新时间")
	private Date updateDate;
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
	public String getAgreeId() {
		return agreeId;
	}
	public void setAgreeId(String agreeId) {
		this.agreeId = agreeId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
