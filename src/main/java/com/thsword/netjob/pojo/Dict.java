package com.thsword.netjob.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 
* @ClassName: Dict 
* @Description: TODO(数据字典) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
public class Dict implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 字典类型
	 */
	private String type;
	/**
	 * 字典值
	 */
	private String value;
	/**
	 * 排序
	 */
	private String sort;
	
	private List<Dict> children;
	/**
	 * 字典父节点
	 */
	private String parentId;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public List<Dict> getChildren() {
		return children;
	}
	public void setChildren(List<Dict> children) {
		this.children = children;
	}
}
