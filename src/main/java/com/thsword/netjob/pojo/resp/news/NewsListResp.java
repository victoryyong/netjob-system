package com.thsword.netjob.pojo.resp.news;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.News;
import com.thsword.utils.page.Page;

@Data
@Builder
public class NewsListResp implements Serializable{
	
	Page page;
	
	List<News> list;

}
