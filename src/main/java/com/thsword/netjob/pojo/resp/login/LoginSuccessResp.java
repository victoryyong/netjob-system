package com.thsword.netjob.pojo.resp.login;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Member;

@Data
@Builder
public class LoginSuccessResp implements Serializable{
	@ApiModelProperty(value="token")
	String token;
	Member member;

}
