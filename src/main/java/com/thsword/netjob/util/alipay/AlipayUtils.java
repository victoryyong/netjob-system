package com.thsword.netjob.util.alipay;

import java.util.Map;



public class AlipayUtils {
	/**
     * 作用：支付宝支付请求
     */
    public static String doUnifiedOrder(String out_trade_no,String total_amount) throws Exception {
    	Alipay alipay = new Alipay();
    	return alipay.doUnifiedOrder(out_trade_no, total_amount);
    }
    
    /**
     * 作用：支付宝支付请求
     */
    public static String orderQuery(String out_trade_no) throws Exception {
    	Alipay alipay = new Alipay();
    	return alipay.orderQuery(out_trade_no);
    }
    /**
     * 验签
     * @param params
     * @return
     * @throws Exception
     */
    public static boolean rsaCheck(Map<String, String> params) throws Exception{
    	return Alipay.rsaCheck(params);
    }
}
