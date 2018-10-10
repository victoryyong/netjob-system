package com.thsword.netjob.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IBannerDao;
import com.thsword.netjob.pojo.app.Banner;
import com.thsword.netjob.service.BannerService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;
/**
 * 广告

 * @Description:广告

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class BannerAdmin {
	@Resource(name="bannerService")
	BannerService bannerService;
	/**
	 * 广告列表
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param banner
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 下午5:03:29
	 */
	@SuppressWarnings("unchecked")
	@AuthAnnotation(permissions="admin.banner.list")
	@RequestMapping("admin/banner/list")
	public void list(HttpServletRequest request,HttpServletResponse response,Banner banner,Page page) throws Exception{
		try {
			String name = request.getParameter("name");
			Map<String, Object> map = new HashMap<String, Object>();
			if(!StringUtils.isEmpty(name))map.put("name", name);
			map.put("page", page);
			List<Banner> banners = (List<Banner>) bannerService.queryPageEntity(IBannerDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", banners);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 新增广告
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param pageUtil
	 * @param role
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 下午2:57:43
	 */
	@AuthAnnotation(permissions="admin.banner.add")
	@RequestMapping("admin/banner/add")
	public void add(HttpServletRequest request,HttpServletResponse response,Banner banner) throws Exception{
		try {
			if(StringUtils.isEmpty(banner.getName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "名称不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(banner.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(banner.getStatus())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "状态不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(banner.getSort())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "排序号不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(banner.getImage())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "广告图不能为空",response,request);
				return;
			}
			String userId = request.getAttribute("userId")+"";
			banner.setId(UUIDUtil.get32UUID());
			banner.setCreateBy(userId);
			banner.setUpdateBy(userId);
			banner.dbImage();
			bannerService.addEntity(IBannerDao.class, banner);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 修改广告
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param banner
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 下午3:45:48
	 */
	@AuthAnnotation(permissions="admin.banner.edit")
	@RequestMapping("admin/banner/edit")
	public void edit(HttpServletRequest request,HttpServletResponse response,Banner banner)throws Exception{
		try {
			if(StringUtils.isEmpty(banner.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "ID不能为空",response,request);
				return;
			}
			bannerService.updateEntity(IBannerDao.class, banner);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.banner.delete")
	@RequestMapping("admin/banner/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			//JSONArray array = new JSONArray();
			String bannerIds = request.getParameter("bannerIds");
			if(StringUtils.isEmpty(bannerIds)){
				JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
				return;
			}
			String[] bannerArray = bannerIds.split(",");
			//Banner tempBanner = null;
			for (String bannerId : bannerArray) {
				//tempBanner = (Banner) bannerService.queryEntityById(IBannerDao.class, bannerId);
				bannerService.deleteEntityById(IBannerDao.class, bannerId);
				//array.add(JSONObject.toJSON(tempBanner));
			}
			//request.setAttribute("params", JSONObject.toJSONString(array));
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
