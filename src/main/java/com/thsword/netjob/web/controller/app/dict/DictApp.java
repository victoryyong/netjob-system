package com.thsword.netjob.web.controller.app.dict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Dict;
import com.thsword.netjob.service.DictService;

@RestController
@Api(tags = "NETJOB-DICT", description = "配置接口")
public class DictApp {
	@Resource(name = "dictService")
	DictService dictService;

	/**
	 * @Description:距离范围
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getDistanceRange")
	@ApiOperation(value = "距离范围", httpMethod = "POST")
	public JSONObject getDistanceRange(HttpServletRequest request, HttpServletResponse response) throws Exception {
			JSONObject obj = new JSONObject();
			obj.put("list", getChildren(Global.SYS_DICT_DISTANCE_TYPE));
			return obj;
	}
	
	/**
	 * @Description:年龄范围
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getAgeRange")
	@ApiOperation(value = "年龄范围", httpMethod = "POST")
	public JSONObject getAgeRange(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_AGE_TYPE));
		return obj;
	}
	
	/**
	 * @Description:技能等级
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getSkillDegree")
	@ApiOperation(value = "技能等级", httpMethod = "POST")
	public JSONObject getSkillDegree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_SKILL_TYPE));
		return obj;
	}
	
	/**
	 * @Description:诚信等级
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getCreditDegree")
	@ApiOperation(value = "诚信等级", httpMethod = "POST")
	public JSONObject getCreditDegree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_CREDIT_TYPE));
		return obj;
	}
	/**
	 * @Description:服务类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getServeType")
	@ApiOperation(value = "服务类型", httpMethod = "POST")
	public JSONObject getServeType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_SERVE_TYPE));
		return obj;
	}
	/**
	 * @Description:性别类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getGenderType")
	@ApiOperation(value = "性别类型", httpMethod = "POST")
	public JSONObject getGenderType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_GENDER_TYPE));
		return obj;
	}
	
	/**
	 * @Description:用户类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getMemberType")
	@ApiOperation(value = "用户类型", httpMethod = "POST")
	public JSONObject getMemberType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_MEMBER_TYPE));
		return obj;
	}
	
	/**
	 * @Description:广告类型
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getBannerType")
	@ApiOperation(value = "广告类型", httpMethod = "POST")
	public JSONObject getBannerType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", getChildren(Global.SYS_DICT_BANNER_TYPE));
		return obj;
	}
	
	/**
	 * @Description:获取搜索条件
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/dict/getSelectCondition")
	@ApiOperation(value = "获取搜索条件", httpMethod = "POST")
	public JSONObject getSelectCondition(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("creditDegrees", getChildren(Global.SYS_DICT_CREDIT_TYPE));
		obj.put("skillDegrees", getChildren(Global.SYS_DICT_SKILL_TYPE));
		obj.put("ageRanges", getChildren(Global.SYS_DICT_AGE_TYPE));
		obj.put("distanceRanges", getChildren(Global.SYS_DICT_DISTANCE_TYPE));
		obj.put("serveTypes", getChildren(Global.SYS_DICT_SERVE_TYPE));
		obj.put("genderTypes", getChildren(Global.SYS_DICT_GENDER_TYPE));
		return obj;
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
