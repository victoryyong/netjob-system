package com.thsword.netjob.pojo.resp.friend;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Friend;
import com.thsword.utils.page.Page;

@Data
@Builder
public class FriendListResp implements Serializable{
	
	List<Friend> list;
	
	Page page;

}
