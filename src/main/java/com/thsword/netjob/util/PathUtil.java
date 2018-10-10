package com.thsword.netjob.util;

import java.util.Date;

import com.thsword.netjob.global.Global;
import com.thsword.utils.date.DateUtil;



public class PathUtil{
	/**
	 * 读取绝对路径
	* @Title: getFullPath 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getFullPath (String relativePath){
		return Global.SYS_FILE_ROOT_PATH+"/"+relativePath;
	}
	
	/**
	 * 读取日期相对路径
	* @Title: getFullPath 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getDatePath (String type){
		return Global.SYS_FILE_PATH+"/"+type+"/"+DateUtil.getString(new Date(), "yyyyMMdd");
	}
	
}
