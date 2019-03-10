package com.thsword.netjob.util.alipay;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.thsword.netjob.global.Global;


public class Alipay {
	private  static AlipayConfig config;
	private boolean useSandbox;
	private  static String notifyUrl;
	private  String gateway;
	private  static String signType;
	private  static String formate;
	private  static String charset;
	{
	    	config = AlipayConfig.getInstance();
	    	useSandbox = Boolean.parseBoolean(Global.getSetting(Global.ALIPAY_PAY_USESANDBOX));
	    	notifyUrl = Global.WX_PAY_CALLBACK;
	    	gateway = useSandbox?"https://openapi.alipaydev.com/gateway.do":"https://openapi.alipay.com/gateway.do";
	    	signType = "RSA2";
	    	formate = "json";
	    	charset = "UTF-8";
    }
	/**
     * 作用：支付宝支付请求
     */
    public String doUnifiedOrder(String out_trade_no,String total_amount) throws Exception {
    	if(StringUtils.isEmpty(out_trade_no)){
    		throw new Exception("alipay out_trade_no is empty");
    	}
    	if(StringUtils.isEmpty(total_amount)){
    		throw new Exception("alipay total_amount is empty");
    	}
    	try {
			Double.parseDouble(total_amount);
		} catch (Exception e) {
			throw new Exception("alipay total_amout formate error");
		}
    	checkConfigParam();
    	// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
    	AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
    	// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
    	AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
    	model.setBody(Global.NETJOB_PAY_RECHARGE_NAMA);
    	model.setSubject(Global.NETJOB_PAY_APP_NAMA);
    	model.setTimeoutExpress("60m");
    	model.setOutTradeNo(out_trade_no);// outtradeno 生存订单
    	model.setTotalAmount(total_amount);
    	request.setBizModel(model);
    	request.setNotifyUrl(notifyUrl);//异步回调url
    	// 实例化客户端
    	AlipayClient alipayClient = new DefaultAlipayClient(gateway, config.getAppId(),
    			config.getPrivateKey(), formate, charset, config.getPublicKey(),
    		signType);
    	AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
    	return response.getBody();
    }
    
    /**
     * 作用：支付宝订单查询
     */
    public String orderQuery(String out_trade_no) throws Exception {
    	checkConfigParam();
    	// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
    	AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
    	// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
    	AlipayTradeQueryModel model = new AlipayTradeQueryModel();
    	model.setOutTradeNo(out_trade_no);
    	request.setBizModel(model);
    	// 实例化客户端
    	AlipayClient alipayClient = new DefaultAlipayClient(gateway, config.getAppId(),
    			config.getPrivateKey(), formate, charset, config.getPublicKey(),
    		signType);
    	AlipayTradeQueryResponse  response = alipayClient.execute(request);
    	return response.getBody();
    }
    
    public static boolean rsaCheck(Map<String, String> params) throws Exception{
    	return AlipaySignature.rsaCheckV1(params, config.getPublicKey(), charset,
				signType);
    }
    
    
    private void checkConfigParam()throws Exception{
            if (config == null) {
                throw new Exception("config is null");
            }
            if (StringUtils.isEmpty(config.getAppId())) {
                throw new Exception("appid in config is empty");
            }
            if (StringUtils.isEmpty(config.getPrivateKey())) {
                throw new Exception("privateKey in config is empty");
            }
            if (StringUtils.isEmpty(config.getPublicKey())){
                throw new Exception("publicKey config is null");
            }
    }
}
