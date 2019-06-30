package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 

 * @Description:头条

 * @author:yong

 * @time:2018年5月7日 下午6:27:12
 */
@ApiModel(value="头条")
public class News {
	/**
	 * 主键
	 */
	 @ApiModelProperty(value="主键")
	private String id;
	/**
	 * 类型
	 */
	 @ApiModelProperty(value="类型")
	private Integer type;
	/**
	 * 内容
	 */
	 @ApiModelProperty(value="内容")
	private String content;
	/**
	 * 用户ID
	 */
	 @ApiModelProperty(value="用户ID")
	private String memberId;
	
	/**
	 * 市编码
	 */
	 @ApiModelProperty(value="市编码")
	private String citycode;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
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
