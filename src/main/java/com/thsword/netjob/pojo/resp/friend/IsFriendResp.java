package com.thsword.netjob.pojo.resp.friend;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IsFriendResp implements Serializable{
	@ApiModelProperty(value="是否好友")
	boolean isFriend;

}
