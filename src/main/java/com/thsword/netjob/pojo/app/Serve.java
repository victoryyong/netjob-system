package com.thsword.netjob.pojo.app;

import java.util.Date;

import org.springframework.stereotype.Controller;
/**
 * 服务

 * @Description:TODO

 * @author:yong

 * @time:2018年5月10日 下午8:52:49
 */
@Controller
public class Serve {
	private String id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 类型（1-服务 2-需求）
	 */
	private Integer type;
	/**
	 * 缩略图
	 */
	private String image;
	/**
	 * 服务方式(线上1 线下2)
	 */
	private Integer workType;
	/**
	 * 结算方式(线上1 线下2)
	 */
	private Integer payType;
	/**
	 * 点击量
	 */
	private Integer clicks;
	/**
	 * 价格
	 */
	private Double price;
	/**
	 * 报价方式(1一口价 2商议)
	 */
	private Integer priceType;
	/**
	 * 有效期(/天)
	 */
	private Integer validity;
	/**
	 * 需求时间
	 */
	private String serveTime;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 视频+图片集合["aaa","bbb","ccc"]
	 */
	private String links;
	/**
	 * 点赞数
	 */
	private Integer agrees;
	/*
	 *一级菜单ID 
	 */
	private String firstMenuId;
	/*
	 * 一级菜单名称
	 */
	private String firstMenuName;
	/**
	 * 类型id
	 */
	private String menuId;
	/**
	 * 类型名称
	 */
	private String menuName;
	private String provinceName;
	private String citycode;
	private String cityName;
	private String area;
	private String detailAddress;
	/**
	 * 关键字集合["aaa","bbb","ccc"]
	 */
	private String keywords;
	private Boolean published;
	private Date publishDate;
	private String memberId;
	private String memberName;
	/**
	 * 审核状态
	 */
	private Integer status;
	/** 经度*/
	private String longitude;
	/** 纬度*/
	private String latitude ;
	/** 距离*/
	private double distance;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 更新时间
	 */
	private Date updateDate;
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
	public Integer getWorkType() {
		return workType;
	}
	public void setWorkType(Integer workType) {
		this.workType = workType;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getPriceType() {
		return priceType;
	}
	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	
	public String getServeTime() {
		return serveTime;
	}
	public void setServeTime(String serveTime) {
		this.serveTime = serveTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public Integer getClicks() {
		return clicks;
	}
	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getFirstMenuId() {
		return firstMenuId;
	}
	public void setFirstMenuId(String firstMenuId) {
		this.firstMenuId = firstMenuId;
	}
	public String getFirstMenuName() {
		return firstMenuName;
	}
	public void setFirstMenuName(String firstMenuName) {
		this.firstMenuName = firstMenuName;
	}
	public Integer getAgrees() {
		return agrees;
	}
	public void setAgrees(Integer agrees) {
		this.agrees = agrees;
	}
}
