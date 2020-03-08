package com.thsword.netjob.pojo.resp.member;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BindPhoneResp implements Serializable{
	@ApiModelProperty(value="认证token")
	String token;

}
