package com.thsword.netjob.pojo.resp.menu;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Menu;

@Data
@Builder
public class MenuListResp implements Serializable{
	
	List<Menu> list;
}
