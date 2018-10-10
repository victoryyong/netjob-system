package com.thsword.netjob.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志
 * 
 * @author yong 2014-4-6 17:10:12
 */
public class Log implements Serializable {

	private static final long serialVersionUID = -6064325597118457281L;
	
	private String id;
	//用户ID
	private String userId;
	// 操作用户
	private String username;
	// 描述
	private String content;
	//标题
	private String title ;
	//错误信息
	private String errorMsg;
	//是否成功
	private Integer status;
	//操作IP
	private String ip;
	//创建时间
	private Date createDate;

	public Log() {
		this.createDate = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
