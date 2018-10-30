package com.thsword.netjob.pojo.app;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: Collect 
* @Description: TODO(收藏对象) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
public class Collect implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	private String id;
	/**
	 * 用户ID
	 */
	private String memberId;
	/**
	 * 业务ID
	 */
	private String bizId;
	/**
	 * 收藏类型
	 */
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 更新时间
	 */
	private Date updateDate;
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
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
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
