package com.thsword.netjob.pojo.resp.auth;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import com.thsword.netjob.pojo.app.AuthPerson;

@Data
@Builder
public class AuthPersonListResp implements Serializable{
	
	List<AuthPerson> list;

}
