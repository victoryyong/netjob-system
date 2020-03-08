package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

/**
 * 服务
 * 
 * @Description:TODO
 * 
 * @author:yong
 * 
 * @time:2018年5月10日 下午8:52:49
 */
@ApiModel(value = "服务")
@Data
public class Serve {
	@ApiModelProperty(value = "ID")
	private String id;
	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题")
	private String title;
	/**
	 * 类型（1-服务 2-需求）
	 */
	@ApiModelProperty(value = "类型（1-服务 2-需求）")
	private Integer type;
	/**
	 * 缩略图
	 */
	@ApiModelProperty(value = "缩略图")
	private String image;
	/**
	 * 服务方式(线上1 线下2)
	 */
	@ApiModelProperty(value = "服务方式(线上1 线下2)")
	private Integer workType;
	/**
	 * 结算方式(线上1 线下2)
	 */
	@ApiModelProperty(value = "结算方式(线上1 线下2)")
	private Integer payType;
	/**
	 * 点击量
	 */
	@ApiModelProperty(value = "点击量")
	private Integer clicks;
	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	private Double price;
	/**
	 * 报价方式(1一口价 2商议)
	 */
	@ApiModelProperty(value = "报价方式(1一口价 2商议)")
	private Integer priceType;
	/**
	 * 有效期(/天)
	 */
	@ApiModelProperty(value = "有效期(/天)")
	private Integer validity;
	/**
	 * 需求时间
	 */
	@ApiModelProperty(value = "需求时间")
	private String serveTime;
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;
	/**
	 * 视频+图片集合["aaa","bbb","ccc"]
	 */
	@ApiModelProperty(value = "视频+图片集合[\"aaa\",\"bbb\",\"ccc\"]")
	private String links;
	/**
	 * 点赞数
	 */
	@ApiModelProperty(value = "点赞数")
	private Integer agrees;
	/*
	 * 一级菜单ID
	 */@ApiModelProperty(value = "一级菜单ID")
	private String firstMenuId;
	/*
	 * 一级菜单名称
	 */@ApiModelProperty(value = "一级菜单名称")
	private String firstMenuName;
	/**
	 * 类型id
	 */
	@ApiModelProperty(value = "类型id")
	private String menuId;
	/**
	 * 类型名称
	 */
	@ApiModelProperty(value = "类型名称")
	private String menuName;
	/**
	 * 省名
	 */
	@ApiModelProperty(value = "省名")
	private String provinceName;
	/**
	 * 城市编码
	 */
	@ApiModelProperty(value = "城市编码")
	private String citycode;
	/**
	 * 城市名
	 */
	@ApiModelProperty(value = "城市名")
	private String cityName;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String area;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String detailAddress;
	/**
	 * 关键字集合["aaa","bbb","ccc"]
	 */
	@ApiModelProperty(value = "关键字集合[\"aaa\",\"bbb\",\"ccc\"]")
	private String keywords;
	/**
	 * 是否发布
	 */
	@ApiModelProperty(value = "是否发布")
	private Boolean published;
	/**
	 * 发布时间
	 */
	@ApiModelProperty(value = "发布时间")
	private Date publishDate;
	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private String memberId;
	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称")
	private String memberName;
	
	/**
	 * 会员图像
	 */
	@ApiModelProperty(value = "会员图像")
	private String memberImage;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "审核状态")
	private Integer status;
	/** 经度 */
	@ApiModelProperty(value = "经度")
	private String longitude;
	/** 纬度 */
	@ApiModelProperty(value = "纬度")
	private String latitude;
	/** 距离 */
	@ApiModelProperty(value = "距离")
	private double distance;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateDate;
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updateBy;

}
