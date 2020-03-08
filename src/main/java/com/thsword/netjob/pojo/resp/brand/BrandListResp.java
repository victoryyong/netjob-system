package com.thsword.netjob.pojo.resp.brand;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Brand;
import com.thsword.utils.page.Page;

@Data
@Builder
public class BrandListResp implements Serializable{
	
	List<Brand> list;
	
	Page page;

}
