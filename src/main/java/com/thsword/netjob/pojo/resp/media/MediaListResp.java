package com.thsword.netjob.pojo.resp.media;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Media;
import com.thsword.utils.page.Page;

@Data
@Builder
public class MediaListResp implements Serializable{
	
	List<Media> list;
	
	Page page;
}
