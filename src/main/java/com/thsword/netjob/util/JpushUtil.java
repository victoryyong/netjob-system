package com.thsword.netjob.util;

import com.thsword.netjob.global.Global;

import cn.jmessage.api.user.UserClient;
import cn.jmessage.api.user.UserStateResult;

public class JpushUtil {
	private static UserClient userClient = null;
	private static String appId = "";
	private static String secretKey="";
	static{
		 appId = Global.getSetting(Global.JPUSH_ACCESS_KEY);
		 secretKey = Global.getSetting(Global.JPUSH_ACCESS_SECRET);
		 userClient = new UserClient(appId, secretKey);
	}
	public boolean isOnline(String userId){
		try {
			UserStateResult result = userClient.getUserState(userId);
			if(null!=result&&result.getOnline()){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	};
}