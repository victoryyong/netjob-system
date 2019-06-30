package com.thsword.netjob.util;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.user.UserInfoResult;

import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Member;

/**
 * IM 极光IM
 * @author Lenovo
 *
 */
@Slf4j
public class JmessageUtil {
	private static String appId="";
	private static String secretKey="";
	static{
		appId=Global.getSetting(Global.JPUSH_ACCESS_KEY);
		secretKey=Global.getSetting(Global.JPUSH_ACCESS_SECRET);
	}
	/**
	 * 注册极光账号
	 */
	 public static void RegisterUsers(List<Member> members) {
	        JMessageClient client = new JMessageClient(appId, secretKey);
	        try {
	        	List<RegisterInfo> users = new ArrayList<RegisterInfo>();
	        	for (Member member : members) {
	        		RegisterInfo user = RegisterInfo.newBuilder()
	        				.setNickname(member.getName())
	        				.setPassword(member.getPassword())
	        				.setUsername(member.getId())
	        				.setGender(member.getGender())
	        				.setAddress(member.getAddress()).build();
	        		users.add(user);
				}
	            RegisterInfo[] regUsers = new RegisterInfo[users.size()];
	            String res = client.registerUsers(users.toArray(regUsers));
	            log.info(res);
	        } catch (APIConnectionException e) {
	            log.error("Connection error. Should retry later. ", e);
	        } catch (APIRequestException e) {
	            log.error("Error response from JPush server. Should review and fix it. ", e);
	            log.info("HTTP Status: " + e.getStatus());
	            log.info("Error Message: " + e.getMessage());
	        }
	    }
	 
	 public static void deleteUser(String username){
		 JMessageClient client = new JMessageClient(appId, secretKey);
		 try {
			 client.deleteUser(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 	/**
		 * 注册极光账号
		 */
		 public static void RegisterUser(Member member) {
		        JMessageClient client = new JMessageClient(appId, secretKey);
		        try {
		            List<RegisterInfo> users = new ArrayList<RegisterInfo>();
		            RegisterInfo user = RegisterInfo.newBuilder()
	        				.setNickname(member.getName())
	        				.setPassword(member.getPassword())
	        				.setUsername(member.getId())
	        				.setGender(member.getGender())
	        				.setAddress(member.getAddress()).build();
	        		users.add(user);
		            RegisterInfo[] regUsers = new RegisterInfo[users.size()];
		            String res = client.registerUsers(users.toArray(regUsers));
		            log.info(res);
		        } catch (APIConnectionException e) {
		            log.error("Connection error. Should retry later. ", e);
		        } catch (APIRequestException e) {
		            log.error("Error response from JPush server. Should review and fix it. ", e);
		            log.info("HTTP Status: " + e.getStatus());
		            log.info("Error Message: " + e.getMessage());
		        }
		    }
	 	/**
	 	 * 获取极光账号
	 	 */
	    public static String getUserInfo(Member member) {
	        JMessageClient client = new JMessageClient(appId, secretKey);
	        try {
	            UserInfoResult res = client.getUserInfo(member.getId());
	            log.info(res.getUsername());
	            return res.getUsername();
	        } catch (APIConnectionException e) {
	            log.error("Connection error. Should retry later. ", e);
	        } catch (APIRequestException e) {
	            log.error("Error response from JPush server. Should review and fix it. ", e);
	            log.info("HTTP Status: " + e.getStatus());
	            log.info("Error Message: " + e.getMessage());
	        }
	        return null;
	    }
}
