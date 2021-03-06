package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: Address 
* @Description: TODO(地址对象) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
@ApiModel(value="地址")
public class Address implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value="用户ID")
	private String memberId;
	/**
	 * 省名
	 */
	@ApiModelProperty(value="省名")
	private String provinceName;
	/**
	 * 市名
	 */
	@ApiModelProperty(value="市名")
	private String cityName;
	/**
	 * 省
	 */
	@ApiModelProperty(value="省")
	private String province;
	/**
	 * 市
	 */
	@ApiModelProperty(value="市")
	private String citycode;
	/**
	 * 区
	 */
	@ApiModelProperty(value="区")
	private String area;
	/**
	 * 是否删除（0-否 1-是）
	 */
	@ApiModelProperty(value="是否删除（0-否 1-是）")
	private Boolean isDel;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value="详细地址")
	private String detailAddress;
	/**
	 * 收件人
	 */
	@ApiModelProperty(value="收件人")
	private String receiver;
	/**
	 * 是否默认地址（0-否 1-是）
	 */
	@ApiModelProperty(value="是否默认地址（0-否 1-是）")
	private Integer isDefault;
	/**
	 * 号码
	 */
	@ApiModelProperty(value="号码")
	private String phone;
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
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
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
	public Boolean getIsDel() {
		return isDel;
	}
	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
}
