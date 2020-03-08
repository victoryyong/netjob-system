package com.thsword.netjob.pojo.resp.active;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Active;
import com.thsword.utils.page.Page;

@Data
@Builder
public class ActiveListResp implements Serializable{
	
	Page page;
	
	List<Active> list;

}
