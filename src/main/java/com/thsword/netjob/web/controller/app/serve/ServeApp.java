package com.thsword.netjob.web.controller.app.serve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Dict;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.ServeService;
import com.thsword.netjob.util.AmapUtil;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;
/**
 * 服务

 * @Description:TODO

 * @author:yong

 * @time:2018年5月10日 下午8:52:49
 */
@Controller
public class ServeApp {
	@Resource(name = "serveService")
	ServeService serveService;

	/**
	 * 
	
	 * 添加需求

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/serve/add")
	public void add(HttpServletRequest request, HttpServletResponse response,Serve serve) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(serve.getTitle())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "标题不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getLongitude())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "经度不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getLatitude())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "纬度不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getType())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空", response, request);
				return;
			}else{
				if (!serve.getType().equals(Global.SYS_MEMBER_SERVE_TYPE_1)&&!serve.getType().equals(Global.SYS_MEMBER_SERVE_TYPE_2)) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型参数错误", response, request);
					return;
				}
			}
			if (StringUtils.isEmpty(serve.getImage())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "缩略图不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getWorkType())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务方式不能为空", response, request);
				return;
			}else{
				if (!serve.getWorkType().equals(Global.SYS_MEMBER_WORK_TYPE_1)&&!serve.getWorkType().equals(Global.SYS_MEMBER_WORK_TYPE_2)) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务方式参数错误", response, request);
					return;
				}
			}
			if (StringUtils.isEmpty(serve.getPayType())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "付款方式不能为空", response, request);
				return;
			}else{
				if (!serve.getPayType().equals(Global.SYS_MEMBER_PAY_TYPE_1)&&!serve.getPayType().equals(Global.SYS_MEMBER_PAY_TYPE_2)) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "付款方式参数错误", response, request);
					return;
				}
			}
			if (StringUtils.isEmpty(serve.getPriceType())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "价格类型不能为空", response, request);
				return;
			}
			if (serve.getPriceType().equals(Global.SYS_MEMBER_PRICE_TYPE_1)) {
				if (StringUtils.isEmpty(serve.getPrice())) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "价格不能为空", response, request);
					return;
				}
			}else if(!serve.getPriceType().equals(Global.SYS_MEMBER_PRICE_TYPE_2)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "定价方式参数错误", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getValidity())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "有效期不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getServeTime())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务时间不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getMenuId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getLinks())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "文件不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getProvinceName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "省份不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getCitycode())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "市区不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getCityName())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "市区不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getArea())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "区县不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(serve.getDetailAddress())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "详细地址不能为空", response, request);
				return;
			}
			serve.setId(UUIDUtil.get32UUID());
			serve.setMemberId(memberId);
			serve.setCreateBy(memberId);
			serve.setUpdateBy(memberId);
			serveService.addEntity(IServeDao.class, serve);
			try {
				JSONArray linkArray = JSONArray.parseArray(serve.getLinks());
				for (Object object : linkArray) {
					Media media = new Media();
					media.setId(UUIDUtil.get32UUID());
					media.setBizId(serve.getId());
					media.setCreateBy(memberId);
					media.setMemberId(memberId);
					media.setUpdateBy(memberId);
					media.setLink(object.toString());
					media.setResource(Global.SYS_MEMBER_ACTIVE_RESOURCE_3);
					if(Global.isImage(object.toString())){
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
					}else{
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
					}
					serveService.addEntity(IMediaDao.class, media);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 需求列表

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getParameter("memberId");
			String type = request.getParameter("type");
			String gender = request.getParameter("gender");
			String menuId = request.getParameter("menuId");
			String citycode = request.getParameter("citycode");
			//查询参数
			Map<String, Object> map = new HashMap<String, Object>();
			
			String ageId = request.getParameter("ageId");
			if(!StringUtils.isEmpty(ageId)){
				//最大年级
				Integer maxAge = 0;
				//最小年级
				Integer minAge = 0;
				
				Dict ageDict = (Dict) serveService.queryEntityById(IDictDao.class, ageId);
				if(null!=ageDict){
					String ageRange = ageDict.getValue();
					if(!StringUtils.isEmpty(ageRange)&&ageRange.indexOf("-")>0){
						int index = ageRange.indexOf("-");
						maxAge = Integer.parseInt(ageRange.substring(index+1));
						minAge = Integer.parseInt(ageRange.substring(0, index));
						map.put("maxAge", maxAge);
						map.put("minAge", minAge);
					}
				}
			}
			
			String distanceId = request.getParameter("distanceId");
			if(!StringUtils.isEmpty(distanceId)){
				String longitude = request.getParameter("longitude");
				String latitude = request.getParameter("latitude");
				if (StringUtils.isEmpty(longitude)||StringUtils.isEmpty(latitude)) {
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "经纬度不能为空", response, request);
					return;
				}
				//最大经度
				double maxLongitude = 0l;
				//最大纬度
				double maxLatitude = 0l;
				//最小经度
				double minLongitude = 0l;
				//最小纬度
				double minLatitude = 0l;
				
				Dict disDict = (Dict) serveService.queryEntityById(IDictDao.class, distanceId);
				if(null!=disDict){
					int mile = Integer.parseInt(disDict.getValue());
					//{minLat, minLng, maxLat, maxLng};
					double[] disRanges = AmapUtil.getAround(Double.parseDouble(latitude),Double.parseDouble(longitude), mile*1000);
					minLatitude = disRanges[0];
					minLongitude = disRanges[1];
					maxLatitude = disRanges[2];
					maxLongitude = disRanges[3];
					map.put("minLatitude", minLatitude);
					map.put("minLongitude", minLongitude);
					map.put("maxLatitude", maxLatitude);
					map.put("maxLongitude", maxLongitude);
				}
			}
			
			String skillId = request.getParameter("skillId");
			if(!StringUtils.isEmpty(skillId)){
				//最大技能评分
				Integer maxSkill = 0;
				//最小技能评分
				Integer minSkill = 0;
				
				Dict skillDict = (Dict) serveService.queryEntityById(IDictDao.class, skillId);
				if(null!=skillDict){
					String skillRange = skillDict.getValue();
					if(!StringUtils.isEmpty(skillRange)&&skillRange.indexOf("-")>0){
						int index = skillRange.indexOf("-");
						maxSkill = Integer.parseInt(skillRange.substring(index+1));
						minSkill = Integer.parseInt(skillRange.substring(0, index));
						map.put("maxSkill", maxSkill);
						map.put("minSkill", minSkill);
					}
				}
			}
			
			String creditId = request.getParameter("creditId");
			if(!StringUtils.isEmpty(creditId)){
				//最大诚信评分
				Integer maxCredit=0;
				//最小诚信评分
				Integer minCredit=0;
				
				Dict creditDict = (Dict) serveService.queryEntityById(IDictDao.class, creditId);
				if(null!=creditDict){
					String creditRange = creditDict.getValue();
					if(!StringUtils.isEmpty(creditRange)&&creditRange.indexOf("-")>0){
						int index = creditRange.indexOf("-");
						maxCredit = Integer.parseInt(creditRange.substring(index+1));
						minCredit = Integer.parseInt(creditRange.substring(0, index));
						map.put("maxCredit", maxCredit);
						map.put("minCredit", minCredit);
					}
				}
			}
			
			
			//String serveTime = request.getParameter("serveTime");
			/*if (StringUtils.isEmpty(type)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "type不能为空", response, request);
				return;
			}*/
			if (StringUtils.isEmpty(page.getSort())) {
				page.setSort("t.c_createDate");
				page.setDir(Page.DIR_TYPE_DESC);
			}else{
				if(page.getSort().equals("c_distance")){
					String longitude = request.getParameter("longitude");
					String latitude = request.getParameter("latitude");
					if (StringUtils.isEmpty(longitude)||StringUtils.isEmpty(latitude)) {
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "经纬度不能为空", response, request);
						return;
					}
					map.put("longitude", longitude);
					map.put("latitude", latitude);
					map.put("dis_sort", "c_distance");
				}
				page.setSort(page.getSort()+" "+page.getDir()+",t.c_createDate");
				page.setDir(Page.DIR_TYPE_DESC);
			}
			map.put("page", page);
			map.put("type", type);
			map.put("gender", gender);
			map.put("menuId", menuId);
			map.put("memberId", memberId);
			map.put("citycode", citycode);
			@SuppressWarnings("unchecked")
			List<Serve> serves = (List<Serve>) serveService.queryPageEntity(IServeDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", serves);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 同城推荐

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/recommend")
	public void recommend(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String citycode = request.getParameter("citycode");
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("t.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			map.put("page", page);
			map.put("citycode", citycode);
			@SuppressWarnings("unchecked")
			List<Serve> serves = (List<Serve>) serveService.queryPageEntity(IServeDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", serves);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * 点击量
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/click")
	public void click(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String serveId = request.getParameter("serveId");
			serveService.addClick(serveId);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
