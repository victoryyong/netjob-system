package com.thsword.netjob.web.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.util.AmapUtil;
import com.thsword.netjob.util.JsonResponseUtil;
/**
 * 地图区域信息

 * @Description地图区域信息

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class AmapAdmin {
	@RequestMapping("admin/amap/getCitys")
	public void getMapChild(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String adcode = request.getParameter("adcode");
			JSONObject obj = AmapUtil.getAmapInfo(adcode);
			JSONArray result = null;
			if(null!=obj){
				result = obj.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
			}
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@RequestMapping("admin/amap/getProvinces")
	public void getProvinces(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			JSONObject obj = AmapUtil.getAmapInfo(null);
			JSONArray result = null;
			if(null!=obj){
				result = obj.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
			}
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
