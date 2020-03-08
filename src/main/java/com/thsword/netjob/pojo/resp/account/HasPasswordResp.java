package com.thsword.netjob.pojo.resp.account;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HasPasswordResp implements Serializable{
	@ApiModelProperty(value="是否设置支付密码")
	boolean hasPassword;

}
