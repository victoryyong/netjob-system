package com.thsword.netjob.web.controller.app.dict;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Dict;
import com.thsword.netjob.service.DictService;
import com.thsword.netjob.util.JsonResponseUtil;

@Controller
public class DictApp {
	@Resource(name = "dictService")
	DictService dictService;

	/**
	 * @Description:距离范围
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getDistanceRange")
	public void getDistanceRange(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_DISTANCE_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @Description:年龄范围
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getAgeRange")
	public void getAgeRange(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_AGE_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @Description:技能等级
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getSkillDegree")
	public void getSkillDegree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_SKILL_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @Description:诚信等级
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getCreditDegree")
	public void getCreditDegree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_CREDIT_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * @Description:服务类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getServeType")
	public void getServeType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_SERVE_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * @Description:性别类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getGenderType")
	public void getGenderType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_GENDER_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @Description:用户类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getMemberType")
	public void getMemberType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_MEMBER_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @Description:广告类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getBannerType")
	public void getBannerType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_BANNER_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @Description:获取搜索条件
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getSelectCondition")
	public void getSelectCondition(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject obj = new JSONObject();
			obj.put("creditDegrees", getChildren(Global.SYS_DICT_CREDIT_TYPE));
			obj.put("skillDegrees", getChildren(Global.SYS_DICT_SKILL_TYPE));
			obj.put("ageRanges", getChildren(Global.SYS_DICT_AGE_TYPE));
			obj.put("distanceRanges", getChildren(Global.SYS_DICT_DISTANCE_TYPE));
			obj.put("serveTypes", getChildren(Global.SYS_DICT_SERVE_TYPE));
			obj.put("genderTypes", getChildren(Global.SYS_DICT_GENDER_TYPE));
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public List<Dict> getChildren(String type){
		List<Dict> dicts = null;
		try {
			Dict dict = new Dict();
			dict.setType(type);
			dict = (Dict) dictService.queryEntity(IDictDao.class, dict);
			if(null!=dict){
				dicts = dict.getChildren();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dicts;
	}
	
}
