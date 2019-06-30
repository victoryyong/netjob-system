package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 

 * @Description:文件

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
@ApiModel(value="文件")
public class Media {
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 文件类型
	 */@ApiModelProperty(value="文件类型")
	private Integer type;
	/**
	 * 文件来源
	 */@ApiModelProperty(value="文件来源")
	private Integer resource;
	/**
	 * 城市列表
	 */@ApiModelProperty(value="城市列表")
	private String citycode;
	/**
	 * 文件链接
	 * */@ApiModelProperty(value="文件链接")
	private String link;
	/**
	 * 业务ID 
	 */@ApiModelProperty(value="业务ID ")
	private String bizId;
	/**
	 * 点击数
	 */@ApiModelProperty(value="点击数")
	private Integer clicks;
	/**
	 * 点赞
	 */@ApiModelProperty(value="点赞")
	private Integer agrees;
	
	/**
	 * 打赏数
	 */@ApiModelProperty(value="打赏数")
	private Double tips;
	
	/**
	 * 用户ID
	 */@ApiModelProperty(value="用户ID")
	private String memberId;
	/**
	 * 创建时间
	 */@ApiModelProperty(value=" 创建时间")
	private Date createDate;
	/**
	 * 更新时间
	 */@ApiModelProperty(value="value")
	private Date updateDate;
	/**
	 * 创建人
	 */@ApiModelProperty(value="创建人")
	private String createBy;
	/**
	 * 更新人
	 */@ApiModelProperty(value="更新人")
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
	public Integer getResource() {
		return resource;
	}
	public void setResource(Integer resource) {
		this.resource = resource;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
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
	public Integer getAgrees() {
		return agrees;
	}
	public void setAgrees(Integer agrees) {
		this.agrees = agrees;
	}
	public Double getTips() {
		return tips;
	}
	public void setTips(Double tips) {
		this.tips = tips;
	}
	public Integer getClicks() {
		return clicks;
	}
	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
}
