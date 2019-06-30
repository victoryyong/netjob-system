package com.thsword.netjob.pojo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 
* @ClassName: Friend 
* @Description: TODO(好友) 
* @author yong
* @date 2017年5月13日 下午3:26:16 
*
 */
@ApiModel(value="好友")
public class Friend implements Serializable {

	private static final long serialVersionUID = -6756090072891967330L;
	@ApiModelProperty(value="id")
	private String id;
	/**
	 * 名称
	 */
	@ApiModelProperty(value="名称")
	private String name;
	/**
	 * 图片
	 */
	@ApiModelProperty(value=" 图片")
	private String image;
	/**
	 * 会员ID
	 */
	@ApiModelProperty(value="会员ID")
	private String memberId;
	/**
	 * 好友ID
	 */
	@ApiModelProperty(value="好友ID")
	private String friendId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value="创建时间")
	private Date createDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
