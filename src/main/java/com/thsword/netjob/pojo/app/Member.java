package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;
/**
 * 

 * @Description:会员

 * @author:Administrator

 * @time:2018年5月7日 下午5:47:07
 */
@ApiModel(value="会员")
@Data
public class Member {
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value="用户名")
	private String name;
	/**
	 * 真实姓名
	 */
	@ApiModelProperty(value="真实姓名")
	private String realName;
	/**
	 * 背景
	 */
	@ApiModelProperty(value="背景")
	private String background;
	/**
	 * 年龄
	 */
	@ApiModelProperty(value="年龄")
	private Integer age;
	/**
	 * 性别
	 */
	@ApiModelProperty(value="性别")
	private Integer gender;
	/**
	 * 密码
	 */
	@ApiModelProperty(value="密码")
	private String password;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(value="邮箱")
	private String email;
	/**
	 * 电话
	 */
	@ApiModelProperty(value="电话")
	private String phone;
	/**
	 * 微信ID
	 */
	@ApiModelProperty(value="微信ID")
	private String wxId;
	/**
	 * 腾讯ID
	 */
	@ApiModelProperty(value="腾讯ID")
	private String qqId;
	/**
	 * 极光IM id
	 */
	@ApiModelProperty(value="极光IM id")
	private String imId;
	/**
	 * 图片
	 */
	@ApiModelProperty(value="图片")
	private String image;
	
	/**
	 * 粉丝数目
	 */
	@ApiModelProperty(value="粉丝数目")
	private Integer fans;
	/**
	 * 交易量
	 */
	@ApiModelProperty(value="交易量")
	private Integer trades;
	
	/**
	 * 注册地址
	 */
	@ApiModelProperty(value="注册地址")
	private String address;
	
	/**
	 *访客数 
	 */
	@ApiModelProperty(value="访客数 ")
	private Integer visitors;
	
	/**
	 *点赞数 
	 */
	@ApiModelProperty(value="点赞数 ")
	private Integer agrees;
	
	/**
	 * 诚信评分
	 */
	 @ApiModelProperty(value="诚信评分")
	private Integer creditScore;
	/**
	 * 技能评分
	 */
	 @ApiModelProperty(value="技能评分")
	private Integer skillScore;
	/**
	 * 手机认证
	 */
	 @ApiModelProperty(value="手机认证")
	private Boolean phoneAuth;
	/**
	 * 个人认证
	 */
	 @ApiModelProperty(value="个人认证")
	private Boolean personAuth;
	/**
	 * 企业认证
	 */
	 @ApiModelProperty(value="企业认证")
	private Boolean companyAuth;
	/**
	 * 是否显示认证信息
	 */
	 @ApiModelProperty(value="是否显示认证信息")
	private Boolean showAuth;
	
	/**
	 * 是否好友
	 */
	 @ApiModelProperty(value="是否好友")
	private Boolean myFriend;
	/**
	 * 粉丝数
	 */@ApiModelProperty(value="粉丝数")
	private Boolean myFans;
	/**
	 * 距离
	 */
	 @ApiModelProperty(value="距离")
	private Double distance;
	/**
	 * 会员类型
	 */
	 @ApiModelProperty(value="会员类型")
	private String type;
	
	/**
	 * 状态
	 */
	 @ApiModelProperty(value="状态")
	private Integer status;
	/**
	 * 城市编号
	 */
	 @ApiModelProperty(value="城市编号")
	private String citycode;
	
	/**
	 * 省名
	 */
	 @ApiModelProperty(value="省名")
	private String provinceName;
	/**
	 * 经度
	 */
	 @ApiModelProperty(value="经度")
	private String longitude;
	/**
	 * 纬度
	 */
	 @ApiModelProperty(value="纬度")
	private String latitude;
	/**
	 * 城市名
	 */
	 @ApiModelProperty(value="城市名")
	private String cityName;
	
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
	 */
	 @ApiModelProperty(value="更新时间")
	private Date updateDate;
	
	/**
	 * 更新人
	 */
	 @ApiModelProperty(value="更新人")
	private String updateBy;
}
