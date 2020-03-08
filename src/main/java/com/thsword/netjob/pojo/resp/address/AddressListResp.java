package com.thsword.netjob.pojo.resp.address;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.Address;

@Data
@Builder
public class AddressListResp implements Serializable{
	
	List<Address> list;

}
