package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: 企业认证 
* @Description: TODO(认证) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
@ApiModel(value="企业认证 ")
public class AuthCompany implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 用户ID
	 */	
	@ApiModelProperty(value="用户ID")
	private String memberId;
	/**
	 * 名称
	 */	
	@ApiModelProperty(value="名称")
	private String name;
	/**
	 * 真实名称
	 */	
	@ApiModelProperty(value="真实名称")
	private String realName;
	/**
	 * 证件号码
	 */	
	@ApiModelProperty(value="证件号码")
	private String code;
	/**
	 * 1 授权书 2其他
	 */	
	@ApiModelProperty(value="1 授权书 2其他")
	private Integer type;
	/**
	 * 城市
	 */	
	@ApiModelProperty(value="城市")
	private String citycode;
	/**
	 * 文件连接
	 */	
	@ApiModelProperty(value="文件连接")
	private String links;
	/**
	 * 状态（1-未审核 2-审核通过 3-审核未通过）
	 */	
	@ApiModelProperty(value="状态（1-未审核 2-审核通过 3-审核未通过）")
	private Integer status;
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
}
