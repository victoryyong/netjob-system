package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 

 * @Description:品牌秀

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
@ApiModel(value="品牌秀 ")
public class Brand {
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value="用户ID")
	private String memberId;
	 /**
	 * 用户名称
	 */
	@ApiModelProperty(value="用户名称")
	private String memberName;
	/**
	 *转载ID 
	 */
	@ApiModelProperty(value="转载ID ")
	private String brandId;
	/**
	 * 标题
	 */
	@ApiModelProperty(value="标题")
	private String title;
	/**
	 * 类型（1原创 2转载）
	 */
	@ApiModelProperty(value="类型（1原创 2转载）")
	private Integer type;
	/**
	 *点赞数
	 */
	@ApiModelProperty(value="点赞数")
	private Integer agrees;
	/**
	 *打赏数
	 */
	@ApiModelProperty(value="打赏数")
	private Integer tips;
	/**
	 * 作者
	 */
	@ApiModelProperty(value="作者")
	private String author;
	/**
	 * 作者ID
	 */
	@ApiModelProperty(value="作者ID")
	private String authorId;
	/**
	 * 城市编码
	 */
	@ApiModelProperty(value="城市编码")
	private String citycode;
	/**
	 * 市名
	 */
	@ApiModelProperty(value="市名")
	private String cityName;
	/**
	 * 省名
	 */
	@ApiModelProperty(value="省名")
	private String provinceName;
	/**
	 * 图文视频连接
	 * */
	@ApiModelProperty(value="图文视频连接")
	private String links;
	/**
	 * 正文
	 */
	@ApiModelProperty(value="正文")
	private String content;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value="审核状态")
	private Integer status;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
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
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
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
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
