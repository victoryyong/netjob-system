package com.thsword.netjob.pojo;

import java.io.Serializable;
import java.util.Date;

//token类
public class Token implements Serializable{
	private static final long serialVersionUID = 7800478174851195842L;

	/** tokenID */
	private String id;
	/** 用户ID */
	private String userId;
	/** 用户名称 */
	private String username;
	/** 签发者*/
	private String issuer;
	/** 面对的用户*/
	private String subject;
	/** token信息*/
	private String access_token;
	/** token信息*/
	private String old_token;
	/**  宽限时间  */
	private Date extendDate;
	/** token过期时间*/
	private Date expires;  
	/** session过期时间 记录*/
	private Date sessionDate;
	/** 创建时间*/
	private Date createDate;
	/** 创建者*/
	private String createBy;
	/** 私钥*/
	private String secretKey;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getOld_token() {
		return old_token;
	}
	public void setOld_token(String old_token) {
		this.old_token = old_token;
	}
	public Date getExtendDate() {
		return extendDate;
	}
	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
	public Date getSessionDate() {
		return sessionDate;
	}
	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
