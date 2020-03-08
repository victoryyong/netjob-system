package com.thsword.netjob.pojo.resp.comment;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Comment;
import com.thsword.utils.page.Page;

@Data
@Builder
public class ReplayListResp implements Serializable{
	
	List<Comment> list;
	
	Page page;

}
