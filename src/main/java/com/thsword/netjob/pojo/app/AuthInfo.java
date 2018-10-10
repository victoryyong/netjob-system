package com.thsword.netjob.pojo.app;

import java.util.Date;

/**
 * 

 * @Description:认证信息

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class AuthInfo {
	private String id;
	/**
	 * 认证会员ID
	 */
	private String memeberId;
	/**
	 * 认证类型
	 */
	private String type;
	/**
	 * 认证手机号码
	 */
	private String phone;
	/**
	 * 身份证号码
	 */
	private String idcard;
	/**
	 * 身份证正面图
	 */
	private String frontImage;
	/**
	 * 身份证反面图
	 */
	private String backImage;
	/**
	 * 技能认证图片
	 */
	private String skillImage;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getFrontImage() {
		return frontImage;
	}
	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}
	public String getBackImage() {
		return backImage;
	}
	public void setBackImage(String backImage) {
		this.backImage = backImage;
	}
	public String getSkillImage() {
		return skillImage;
	}
	public void setSkillImage(String skillImage) {
		this.skillImage = skillImage;
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
