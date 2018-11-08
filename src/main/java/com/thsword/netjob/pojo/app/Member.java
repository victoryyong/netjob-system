package com.thsword.netjob.pojo.app;

import java.util.Date;
/**
 * 

 * @Description:会员

 * @author:Administrator

 * @time:2018年5月7日 下午5:47:07
 */
public class Member {
	
	private String id;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 背景
	 */
	private String background;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 性别
	 */
	private Integer gender;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 微信ID
	 */
	private String wxId;
	/**
	 * 腾讯ID
	 */
	private String qqId;
	/**
	 * 图片
	 */
	private String image;
	
	/**
	 * 粉丝数目
	 */
	private Integer fans;
	/**
	 * 交易量
	 */
	private Integer trades;
	
	/**
	 * 注册地址
	 */
	private String address;
	
	/**
	 *访客数 
	 */
	private Integer visitors;
	
	/**
	 *点赞数 
	 */
	private Integer agrees;
	
	/**
	 * 诚信评分
	 */
	private Integer creditScore;
	/**
	 * 技能评分
	 */
	private Integer skillScore;
	/**
	 * 手机认证
	 */
	private boolean phoneAuth;
	/**
	 * 个人认证
	 */
	private boolean personAuth;
	/**
	 * 企业认证
	 */
	private boolean companyAuth;
	/**
	 * 是否显示认证信息
	 */
	private Boolean showAuth;
	/**
	 * 会员类型
	 */
	private String type;
	
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 城市编号
	 */
	private String citycode;
	
	/**
	 * 
	 */
	private String provinceName;
	
	/**
	 * 
	 */
	private String province;
	
	/**
	 * 
	 */
	private String cityName;
	
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	public Integer getVisitors() {
		return visitors;
	}

	public void setVisitors(Integer visitors) {
		this.visitors = visitors;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getQqId() {
		return qqId;
	}

	public void setQqId(String qqId) {
		this.qqId = qqId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getAgrees() {
		return agrees;
	}

	public void setAgrees(Integer agrees) {
		this.agrees = agrees;
	}

	public Integer getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}

	public Integer getSkillScore() {
		return skillScore;
	}

	public void setSkillScore(Integer skillScore) {
		this.skillScore = skillScore;
	}

	public Boolean getShowAuth() {
		return showAuth;
	}

	public void setShowAuth(Boolean showAuth) {
		this.showAuth = showAuth;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public Integer getTrades() {
		return trades;
	}

	public void setTrades(Integer trades) {
		this.trades = trades;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public boolean isPhoneAuth() {
		return phoneAuth;
	}

	public void setPhoneAuth(boolean phoneAuth) {
		this.phoneAuth = phoneAuth;
	}

	public boolean isPersonAuth() {
		return personAuth;
	}

	public void setPersonAuth(boolean personAuth) {
		this.personAuth = personAuth;
	}

	public boolean isCompanyAuth() {
		return companyAuth;
	}

	public void setCompanyAuth(boolean companyAuth) {
		this.companyAuth = companyAuth;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
