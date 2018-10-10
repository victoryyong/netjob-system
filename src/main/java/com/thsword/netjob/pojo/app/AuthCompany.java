package com.thsword.netjob.pojo.app;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: 企业认证 
* @Description: TODO(认证) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
public class AuthCompany implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	private String id;
	private String memberId;
	/**
	 * 委托书
	 */
	private String deputes;
	/**
	 * 印业执照号
	 */
	private String tradeCode;
	
	private String tradeLinks;
	/**
	 * 许可证书号
	 */
	private String allowCode;
	
	private String allowLinks;
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
	public String getDeputes() {
		return deputes;
	}
	public void setDeputes(String deputes) {
		this.deputes = deputes;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getTradeLinks() {
		return tradeLinks;
	}
	public void setTradeLinks(String tradeLinks) {
		this.tradeLinks = tradeLinks;
	}
	public String getAllowCode() {
		return allowCode;
	}
	public void setAllowCode(String allowCode) {
		this.allowCode = allowCode;
	}
	public String getAllowLinks() {
		return allowLinks;
	}
	public void setAllowLinks(String allowLinks) {
		this.allowLinks = allowLinks;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
