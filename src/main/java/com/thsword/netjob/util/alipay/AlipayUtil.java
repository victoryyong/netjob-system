package com.thsword.netjob.util.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AlipayUtil {
	private static AlipayUtil alipayUtil;
	private AlipayClient alipayClient;
	private static String gateWay;
	private static String appId;
	private static String privateKey;
	private static String responseType;
	private static String charact;
	private static String publicKey;
	private static String  encrypt;
	
	static{
		gateWay="https://openapi.alipaydev.com/gateway.do";
		appId="2016092100560528";
		privateKey="XDGJD3pYbxh/dFQnu+j0kA==";
		responseType="json";
		charact = "utf-8";
		publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjCo61jKxzxVflMWMwU5aW6wPOEorM6fbmHILCMFr9lNe1FUd/HAYQDZTEoK/q5N1RdHG+OeUHPS4jQdTa48u0hzlEjsLI8xgszlis5ejz7Ffo1OK21GT23ESH79zdrxY8A7kq3CWdmcpNxlFs8v7BEqfDo/5ms9Nzv/5irb3swx7SVVir39l22zmgLpYIW2dzMlVNv3pzL6m1tSj9CsWobygb0JUDuLOLAMpPk+i/2GSVvfgz+CjqNNT220Ze25P7+xdXfAVBP22cJ3l2YyXyEvI8y00ZxEgeQNPk9WW0ITS55Rk9tTEuHCzmhvBy2+ZkcXJATQyWyoZ9MCgW+NgwwIDAQAB";
		encrypt="RSA2";
	}
	
	private AlipayUtil() {
		init();
	}
	
	private void init(){
		alipayClient = new DefaultAlipayClient(gateWay,appId,privateKey,responseType,charact,publicKey,encrypt);
	}
	
	private static AlipayUtil getAlipayUtil(){
		if(null==alipayUtil){
			alipayUtil = new AlipayUtil();
		}
		return alipayUtil;
	}
}
