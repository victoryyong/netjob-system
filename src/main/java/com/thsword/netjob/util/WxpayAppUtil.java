package com.thsword.netjob.util;

import java.util.HashMap;
import java.util.Map;

import com.thsword.netjob.global.Global;
import com.thsword.netjob.util.wx.WXPay;
import com.thsword.netjob.util.wx.WXPayConfigImpl;
import com.thsword.netjob.util.wx.WXPayUtil;

public class WxpayAppUtil {
	/**
     *统一下单
     */
	public static Map<String, String> doUnifiedOrder(String total_fee,String out_trade_no,String ip,String attach) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", Global.NETJOB_PAY_RECHARGE_NAMA);
        data.put("out_trade_no", out_trade_no);
        data.put("device_info", "");
        data.put("attach", attach);
        data.put("fee_type", "CNY");
        data.put("total_fee", total_fee);
        data.put("spbill_create_ip", ip);
        data.put("trade_type", "APP");
        try {
        	WXPay wxpay = new WXPay();
            return wxpay.unifiedOrder(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
     *关闭订单
     */
    public static Map<String, String> doOrderClose(String out_trade_no) {
        System.out.println("关闭订单");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
        	WXPay wxpay = new WXPay();
        	return wxpay.closeOrder(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 查询订单
     */
    public static Map<String, String> doOrderQuery(String out_trade_no) {
        System.out.println("查询订单");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
        	WXPay wxpay = new WXPay();
        	return wxpay.orderQuery(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 撤销
     */
    public static Map<String, String> doOrderReverse(String out_trade_no) {
        System.out.println("撤销");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        try {
        	WXPay wxpay = new WXPay();
        	return wxpay.reverse(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退款
     */
    public static Map<String, String> doRefund(String out_trade_no,String out_refund_no,int total_fee,int refund_fee) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", out_trade_no);
        data.put("out_refund_no", out_trade_no);
        data.put("total_fee", total_fee+"");
        data.put("refund_fee", total_fee+"");
        data.put("refund_fee_type", "CNY");
        data.put("op_user_id", WXPayConfigImpl.getInstance().getMchID());

        try {
        	WXPay wxpay = new WXPay();
            return wxpay.refund(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询退款
     */
    public static Map<String, String> doRefundQuery(String out_trade_no) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_refund_no", out_trade_no);
        try {
        	WXPay wxpay = new WXPay();
        	return wxpay.refundQuery(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对账单下载
     */
    public static Map<String, String> doDownloadBill() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("bill_date", "20161102");
        data.put("bill_type", "ALL");
        try {
        	WXPay wxpay = new WXPay();
        	return wxpay.downloadBill(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doGetSandboxSignKey() throws Exception {
        WXPayConfigImpl config = WXPayConfigImpl.getInstance();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(data, config.getKey());
        data.put("sign", sign);
        WXPay wxPay = new WXPay();
        return wxPay.requestWithoutCert("/sandboxnew/pay/getsignkey", data, 10000, 10000);
    }
}
