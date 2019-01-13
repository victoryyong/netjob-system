package com.thsword.netjob.util.wx;

import java.io.InputStream;

import com.thsword.netjob.global.Global;

public class WXPayConfigImpl extends WXPayConfig{
	private  String appId;
	private  String mchId;
	private  String key;
	//private static volatile WXPayConfigImpl wxPayConfig;

    private WXPayConfigImpl() {
    	this.appId=Global.getSetting(Global.WX_PAY_APPID);
    	this.mchId=Global.getSetting(Global.WX_PAY_MCHID);
    	this.key=Global.getSetting(Global.WX_PAY_SECRET_KEY);
    }

    public static WXPayConfigImpl getInstance() {
       /* if (wxPayConfig == null) {
            synchronized (WXPayConfigImpl.class) {
                if (wxPayConfig == null) {
                	wxPayConfig = new WXPayConfigImpl();
                }
            }
        }*/
        return new WXPayConfigImpl();
    }
	@Override
	public String getAppID() {
		// TODO Auto-generated method stub
		return appId;
	}

	@Override
	public String getMchID() {
		// TODO Auto-generated method stub
		return mchId;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWXPayDomain getWXPayDomain() {
		IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
 
            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
	}

}
