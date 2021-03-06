package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: Active 
* @Description: TODO(个人动态) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
@ApiModel(value="个人动态")
public class Active implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value="ID")
	private String id;
	/**
	 * 标题
	 */
	@ApiModelProperty(value="标题")
	private String title;
	/**
	 * 文件地址[\"\",\"\"]
	 */
	 @ApiModelProperty(value="文件地址[\"\",\"\"]")
	private String links;
	/**
	 * 点赞数
	 */
	 @ApiModelProperty(value="点赞数")
	private Integer agrees;
	/**
	 * 用户id
	 */
	 @ApiModelProperty(value="用户ID")
	private String memberId;
	/**
	 *评论数 
	 */
	 @ApiModelProperty(value="评论数 ")
	private Integer comments;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
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
	public Integer getAgrees() {
		return agrees;
	}
	public void setAgrees(Integer agrees) {
		this.agrees = agrees;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Integer getComments() {
		return comments;
	}
	public void setComments(Integer comments) {
		this.comments = comments;
	}
}
