package com.thsword.netjob.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
* @ClassName: Role 
* @Description: TODO(登录模块系统角色类) 
* @author yong
* @date 2017年5月13日 下午3:26:37 
*
 */
public class Role implements Serializable {

	private static final long serialVersionUID = -6064325597118457281L;
	private String id;
	/**
	 * 角色名
	 */
	private String name;
	/**
	 * 角色码
	 */
	private String code;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 角色等级
	 */
	private String level;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 角色权限
	 */
	private List<Permission> perms;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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
	public List<Permission> getPerms() {
		return perms;
	}
	public void setPerms(List<Permission> perms) {
		this.perms = perms;
	}
}
