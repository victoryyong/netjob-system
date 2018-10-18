package com.thsword.netjob.pojo.app;

import java.util.Date;

/**
 * 

 * @Description:品牌秀

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class Brand {
	private String id;
	/**
	 * 用户ID
	 */
	private String memberId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 类型（1原创 2转载）
	 */
	private String type;
	/**
	 *点赞数
	 */
	private Integer agrees;
	/**
	 *打赏数
	 */
	private Integer tips;
	/**
	 * 作者
	 */
	private String author;
	/**
	 * 图文视频连接
	 * */
	private String links;
	/**
	 * 正文
	 */
	private String content;
	/**
	 * 审核状态
	 */
	private Integer status;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
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
	public Integer getAgrees() {
		return agrees;
	}
	public void setAgrees(Integer agrees) {
		this.agrees = agrees;
	}
	public Integer getTips() {
		return tips;
	}
	public void setTips(Integer tips) {
		this.tips = tips;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
