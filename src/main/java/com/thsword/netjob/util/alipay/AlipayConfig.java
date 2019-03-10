package com.thsword.netjob.util.alipay;

import com.thsword.netjob.global.Global;

public class AlipayConfig {

	private String appId;
	
	private String privateKey;
	
	private String publicKey;
	
	private AlipayConfig() {
		this.appId=Global.getSetting(Global.ALIPAY_PAY_APPID);
    	this.privateKey=Global.getSetting(Global.ALIPAY_PAY_PRIVATE_KEY);
    	this.publicKey=Global.getSetting(Global.ALIPAY_PAY_PUBLIC_KEY);
	}
	
	public static AlipayConfig getInstance(){
		return new AlipayConfig();
	}

	public String getAppId() {
		return appId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}
}
