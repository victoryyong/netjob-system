package com.thsword.netjob.pojo.resp.member;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInfoResp implements Serializable{
	@ApiModelProperty(value="待付款数")
	Integer payingNums;
	@ApiModelProperty(value="待签收数")
	Integer signingNums;
	@ApiModelProperty(value="待评价数")
	Integer commentingNums;
	@ApiModelProperty(value="待服务数")
	Integer serveingNums;
	@ApiModelProperty(value="待维权数")
	Integer rightingNums;
}
