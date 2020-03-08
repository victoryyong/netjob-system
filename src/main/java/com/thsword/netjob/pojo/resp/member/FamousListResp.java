package com.thsword.netjob.pojo.resp.member;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Member;
import com.thsword.utils.page.Page;

@Data
@Builder
public class FamousListResp implements Serializable{
	List<Member> list;
	
	Page page;
}
