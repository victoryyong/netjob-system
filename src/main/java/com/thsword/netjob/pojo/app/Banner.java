package com.thsword.netjob.pojo.app;

import java.util.Date;

import com.thsword.netjob.global.Global;

/**
 * 

 * @Description:广告

 * @author:yong

 * @time:2018年5月7日 下午5:47:41
 */
public class Banner {
	private String id;
	/**
	 * 广告名称
	 */
	private String name;
	/**
	 * 广告类型
	 */
	private String type;
	/**
	 * 链接
	 */
	private String link;
	/**
	 * 广告缩略图
	 * */
	private String image;
	/**
	 * 
	 */
	private String subimg;
	/**
	 * 
	 */
	private String province;
	/**
	 * 省份名称
	 */
	private String provinceName;
	/**
	 * 城市名称
	 */
	private String cityName;
	/**
	 * 城市编码
	 */
	private String citycode;
	/**
	 * 一级菜单ID
	 */
	private String firstMenuId;
	/**
	 * 二级菜单ID
	 */
	private String secondMenuId;
	/**
	 * 二级菜单名称
	 */
	private String secondMenuName;
	
	private Menu secondMenu;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 排序号
	 */
	private Integer sort;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		setSubimg(image);
		this.image = Global.getSetting(Global.SYSTEM_ADMIN_DOMAIN)+image;
	}
	public void dbImage() {
		this.image = getSubimg();
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getFirstMenuId() {
		return firstMenuId;
	}
	public void setFirstMenuId(String firstMenuId) {
		this.firstMenuId = firstMenuId;
	}
	public String getSecondMenuId() {
		return secondMenuId;
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
	public void setSecondMenuId(String secondMenuId) {
		this.secondMenuId = secondMenuId;
	}
	public String getSecondMenuName() {
		return secondMenuName;
	}
	public void setSecondMenuName(String secondMenuName) {
		this.secondMenuName = secondMenuName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
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
	public Menu getSecondMenu() {
		return secondMenu;
	}
	public void setSecondMenu(Menu secondMenu) {
		this.secondMenu = secondMenu;
		if(null!=secondMenu)
		this.secondMenuName = secondMenu.getName();
	}
	public String getSubimg() {
		return subimg;
	}
	public void setSubimg(String subimg) {
		this.subimg = subimg;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
}
