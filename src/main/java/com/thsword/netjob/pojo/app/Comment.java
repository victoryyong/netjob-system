package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel(value="评论")
public class Comment {
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 业务ID
	 */
	@ApiModelProperty(value="业务ID")
	private String bizId;
	/**
	 * 回复数
	 */
	@ApiModelProperty(value="回复数")
	private Integer replys;
	/**
	 * 评论人ID
	 */
	@ApiModelProperty(value="评论人ID")
	private String memberId;
	/**
	 * 评论人名称
	 */
	@ApiModelProperty(value="评论人名称")
	private String memberName;
	/**
	 * 会员图片
	 */
	@ApiModelProperty(value="会员图片")
	private String memberImage;
	/**
	 * 评论内容
	 */
	@ApiModelProperty(value="评论内容")
	private String content;
	/**
	 * 评论图片
	 */
	@ApiModelProperty(value="评论图片")
	private String image;
	/**
	 * 父节点
	 */
	@ApiModelProperty(value="父节点")
	private String parentId;
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
	
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getMemberImage() {
		return memberImage;
	}
	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	}
	public Integer getReplys() {
		return replys;
	}
	public void setReplys(Integer replys) {
		this.replys = replys;
	}
}
