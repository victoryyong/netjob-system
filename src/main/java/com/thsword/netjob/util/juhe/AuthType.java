package com.thsword.netjob.util.juhe;

public enum AuthType {
	PersonOne("1","一代身份证"),
	PersonTwoFace("2","二代身份证正面"),
	PersonTwoBack("3","二代身份证反面"),
	BusinessLicence("2008","营业执照");
	private String code;
	private String desc;
	
	private AuthType(String code,String desc) {
		this.code=code;
		this.desc=desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
