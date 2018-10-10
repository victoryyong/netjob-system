package com.thsword.netjob.pojo.app;

import java.util.Date;

/**
 * 

 * @Description:访客信息

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class Visitor {
	private String id;
	/**
	 * 认证会员ID
	 */
	private String memeberId;
	/**
	 * 认证类型
	 */
	private String viewerId;
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
	public String getMemeberId() {
		return memeberId;
	}
	public void setMemeberId(String memeberId) {
		this.memeberId = memeberId;
	}
	public String getViewerId() {
		return viewerId;
	}
	public void setViewerId(String viewerId) {
		this.viewerId = viewerId;
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
}
