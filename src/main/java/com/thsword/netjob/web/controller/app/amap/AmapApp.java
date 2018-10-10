package com.thsword.netjob.web.controller.app.amap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.util.AmapUtil;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
/**
 * 地图区域信息

 * @Description地图区域信息

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class AmapApp {
	@RequestMapping("app/visitor/getCitys")
	public void getMapChild(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String adcode = request.getParameter("adcode");
			if(StringUtils.isEmpty(adcode)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "adcode不能为空",response,request);
				return;
			}
			JSONObject obj = AmapUtil.getAmapInfo(adcode);
			JSONArray result = null;
			if(null!=obj){
				result = obj.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
			}
			obj.clear();
			obj.put("list", result);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@RequestMapping("app/visitor/getProvinces")
	public void getProvinces(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			JSONObject obj = AmapUtil.getAmapInfo(null);
			JSONArray result = null;
			if(null!=obj){
				result = obj.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
			}
			obj.clear();
			obj.put("list", result);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@RequestMapping("app/visitor/getAreas")
	public void getAreas(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String citycode = request.getParameter("citycode");
			if(StringUtils.isEmpty(citycode)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "citycode不能为空",response,request);
				return;
			}
			JSONObject obj = AmapUtil.getAmapInfo(citycode);
			JSONArray result = null;
			if(null!=obj){
				result = obj.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
			}
			obj.clear();
			obj.put("list", result);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("app/visitor/getMaps")
	public void getMaps(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			JSONObject obj = Global.initMaps();
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
