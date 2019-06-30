package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 个人品牌
* @ClassName: Access 
* @Description: TODO(个人品牌) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
@ApiModel(value="个人品牌")
public class Introduce implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 会员ID
	 */
	@ApiModelProperty(value="会员ID")
	private String memberId;
	/**
	 * 资历
	 */
	 @ApiModelProperty(value="资历")
	private String experience;
	
	/**
	 *格言 
	 */
	 @ApiModelProperty(value="格言 ")
	private String motto;
	
	/**
	 * 擅长
	 */
	 @ApiModelProperty(value="擅长")
	private String advantage;
	
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
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public String getAdvantage() {
		return advantage;
	}
	public void setAdvantage(String advantage) {
		this.advantage = advantage;
	}
}
