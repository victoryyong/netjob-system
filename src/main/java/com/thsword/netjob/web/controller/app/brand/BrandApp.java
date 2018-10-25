package com.thsword.netjob.web.controller.app.brand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IBrandDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Brand;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.service.BrandService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Controller
public class BrandApp {
	@Resource(name = "brandService")
	BrandService brandService;

	/**
	 * @Description:新增品牌秀
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/brand/add")
	public void query(HttpServletRequest request, HttpServletResponse response,Brand brandShow) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String memberName = request.getAttribute("memberName")+"";
			Member member = (Member) brandService.queryEntityById(IMemberDao.class, memberId);
			if(StringUtils.isEmpty(brandShow.getTitle())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "标题不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(brandShow.getLinks())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "文件不能为空", response, request);
				return;
			}
			if(StringUtils.isEmpty(brandShow.getContent())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "内容不能为空", response, request);
				return;
			}
			brandShow.setId(UUIDUtil.get32UUID());
			brandShow.setType(Global.SYS_MEMBER_BRANDSHOW_RESOURCE_1);
			brandShow.setMemberId(memberId);
			brandShow.setAuthor(memberName);
			brandShow.setCreateBy(memberId);
			brandShow.setUpdateBy(memberId);
			brandShow.setCitycode(member.getCitycode());
			brandShow.setProvinceName(member.getProvinceName());
			brandShow.setCityName(member.getCityName());
			
			brandService.addEntity(IBrandDao.class, brandShow);
			try {
				JSONArray linkArray = JSONArray.parseArray(brandShow.getLinks());
				for (Object object : linkArray) {
					Media media = new Media();
					media.setId(UUIDUtil.get32UUID());
					media.setBizId(brandShow.getId());
					media.setCreateBy(memberId);
					media.setMemberId(memberId);
					media.setUpdateBy(memberId);
					media.setCitycode(member.getCitycode());
					media.setLink(object.toString());
					media.setResource(Global.SYS_MEMBER_ACTIVE_RESOURCE_2);
					if(Global.isImage(object.toString())){
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
					}else{
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
					}
					brandService.addEntity(IMediaDao.class, media);
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
	 * @Description:转载品牌秀
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/brand/reproduce")
	public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			String citycode = request.getAttribute("citycode")+"";
			String brandId = request.getParameter("brandId");
			if(StringUtils.isEmpty(brandId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "转载ID不能为空", response, request);
				return;
			}
			Brand brand = (Brand) brandService.queryEntityById(IBrandDao.class, brandId);
			if(null==brand){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "转载内容不存在", response, request);
				return;
			}
			brand.setId(UUIDUtil.get32UUID());
			brand.setBrandId(brandId);
			brand.setType(Global.SYS_MEMBER_BRANDSHOW_RESOURCE_2);
			String reproduceId = brand.getMemberId();
			if(!StringUtils.isEmpty(reproduceId)){
				Member member = (Member) brandService.queryEntityById(IMemberDao.class, reproduceId);
				if(null!=member){
					if(member.getId().equals(memberId)){
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "不能转载自己的作品", response, request);
						return;
					}
					brand.setProvinceName(member.getProvinceName());
					brand.setCityName(member.getCityName());
					brand.setAuthor(member.getName());
				}
			}
			brand.setMemberId(memberId);
			brand.setCreateBy(memberId);
			brand.setUpdateBy(memberId);
			brandService.addEntity(IBrandDao.class, brand);
			try {
				JSONArray linkArray = JSONArray.parseArray(brand.getLinks());
				for (Object object : linkArray) {
					Media media = new Media();
					media.setId(UUIDUtil.get32UUID());
					media.setBizId(brand.getId());
					media.setCreateBy(memberId);
					media.setMemberId(memberId);
					media.setUpdateBy(memberId);
					media.setCitycode(citycode);
					media.setLink(object.toString());
					media.setResource(Global.SYS_MEMBER_ACTIVE_RESOURCE_2);
					if(Global.isImage(object.toString())){
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
					}else{
						media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
					}
					brandService.addEntity(IMediaDao.class, media);
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
	 * @Description:品牌秀列表
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/brand/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String memberId = request.getParameter("memberId");
			if(StringUtils.isEmpty(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "用户ID不能为空", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<>();
			page.setSort("t.c_createDate");
			map.put("memberId", memberId);
			map.put("page", page);
			@SuppressWarnings("unchecked")
			List<Brand> brands = (List<Brand>) brandService.queryPageEntity(IBrandDao.class, map);
			JSONObject obj = new JSONObject();
			obj.put("list", brands);
			obj.put("page", page);
			JsonResponseUtil.successBodyResponse(obj, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
