package com.thsword.netjob.pojo.resp.collect;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IsCollectResp implements Serializable{
	
	boolean isCollected;

}
