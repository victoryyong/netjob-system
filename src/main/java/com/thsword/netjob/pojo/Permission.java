package com.thsword.netjob.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
* @ClassName: Permission 
* @Description: TODO(登录模块系统权限类) 
* @author yong
* @date 2017年5月13日 下午3:27:22 
*
 */
public class Permission implements Serializable {

	private static final long serialVersionUID = -6798280734027354246L;
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 *权限编码 
	 */
	private String code;
	/**
	 *菜单权限icon
	 */
	private String icon;
	/**
	* 权限描述
	*/
	private String description;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 权限url
	 */
	private String url;
	/**
	 * 父权限id
	 */
	private String parentId;
	/**
	 * 权限类型
	 */
	private Integer type;
	/**
	 * 权限级别
	 */
	private Integer level;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 子权限
	 */
	private List<Permission> children;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private String createBy;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public List<Permission> getChildren() {
		return children;
	}
	public void setChildren(List<Permission> children) {
		this.children = children;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Override
	public boolean equals(Object o){ 
		if(!(o instanceof Permission)) 
		return false; 
		Permission p = (Permission)o; 
		return p.getId().equals(this.getId());
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	} 
}
