package com.thsword.netjob.pojo.resp.serve;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Serve;
import com.thsword.utils.page.Page;

@Data
@Builder
public class ServeListResp implements Serializable{
	
	Page page;
	
	List<Serve> list;

}
