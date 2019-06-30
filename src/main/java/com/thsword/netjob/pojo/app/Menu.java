package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

import com.thsword.netjob.global.Global;

/**
 * 

 * @Description:服务、需求类型

 * @author:yong

 * @time:2018年5月7日 下午6:27:12
 */
@ApiModel(value="服务、需求类型")
public class Menu {
	/**
	 * 主键
	 */
	@ApiModelProperty(value="主键")
	private String id;
	/**
	 * 父元素ID 
	 */
	@ApiModelProperty(value="父元素ID ")
	private String parentId;
	/**
	 * 类型名称
	 */
	@ApiModelProperty(value="类型名称")
	private String name;
	/**
	 * 图标
	 */
	@ApiModelProperty(value="图标")
	private String icon;
	 /**
	 * 无用属性
	 */
	@ApiModelProperty(value="图标")
	private String subicon;
	/**
	 * 几级菜单
	 */
	@ApiModelProperty(value="几级菜单")
	private Integer level;
	/**
	 * 状态
	 */
	@ApiModelProperty(value="状态")
	private Integer status;
	/**
	 * 点击数
	 */
	@ApiModelProperty(value="点击数")
	private String clicks;
	/**
	 * 是否热门
	 */
	@ApiModelProperty(value="是否热门")
	private Boolean hot;
	/**
	 * 是否新加
	 */
	@ApiModelProperty(value="是否新加")
	private Boolean fresh;
	/**
	 * 排序号
	 */
	@ApiModelProperty(value="排序号")
	private int sort;
	/**
	 * 子集
	 */
	@ApiModelProperty(value="子集")
	private List<Menu> children;
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
	 */@ApiModelProperty(value="更新时间")
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		setSubicon(icon);
		this.icon = Global.getSetting(Global.SYSTEM_ADMIN_DOMAIN)+icon;
	}
	public void dbIcon(){
		this.icon=getSubicon();
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public Boolean getHot() {
		return hot;
	}
	public void setHot(Boolean hot) {
		this.hot = hot;
	}
	public Boolean getFresh() {
		return fresh;
	}
	public void setFresh(Boolean fresh) {
		this.fresh = fresh;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public List<Menu> getChildren() {
		return children;
	}
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	public String getSubicon() {
		return subicon;
	}
	public void setSubicon(String subicon) {
		this.subicon = subicon;
	}
}
