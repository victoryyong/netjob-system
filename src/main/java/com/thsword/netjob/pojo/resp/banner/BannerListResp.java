package com.thsword.netjob.pojo.resp.banner;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Banner;
import com.thsword.utils.page.Page;

@Data
@Builder
public class BannerListResp implements Serializable{
	
	List<Banner> list;
	
	Page page;

}
