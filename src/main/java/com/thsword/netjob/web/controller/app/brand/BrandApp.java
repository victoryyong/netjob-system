package com.thsword.netjob.web.controller.app.brand;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.thsword.netjob.dao.IBrandDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Brand;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.resp.brand.BrandListResp;
import com.thsword.netjob.service.BrandService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-BRAND", description = "品牌秀接口")
public class BrandApp {
	@Resource(name = "brandService")
	BrandService brandService;

	/**
	 * @Description:新增品牌秀
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/brand/add")
	@ApiOperation(value = "新增品牌秀", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "标题", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "links", value = "连接", dataType = "string", paramType = "query", required = true) })
	public BaseResponse query(HttpServletRequest request,
			@RequestParam String title, @RequestParam String content,
			@RequestParam String links) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		String memberName = request.getAttribute("memberName") + "";
		Member member = (Member) brandService.queryEntityById(IMemberDao.class,
				memberId);
		Brand brandShow = new Brand();
		brandShow.setId(UUIDUtil.get32UUID());
		brandShow.setTitle(title);
		brandShow.setContent(content);
		brandShow.setLinks(links);
		brandShow.setType(Global.SYS_MEMBER_BRANDSHOW_RESOURCE_1);
		brandShow.setMemberId(memberId);
		brandShow.setAuthor(memberName);
		brandShow.setAuthorId(memberId);
		brandShow.setCreateBy(memberId);
		brandShow.setUpdateBy(memberId);
		brandShow.setCitycode(member.getCitycode());
		brandShow.setProvinceName(member.getProvinceName());
		brandShow.setCityName(member.getCityName());

		brandService.addEntity(IBrandDao.class, brandShow);
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
			if (Global.isImage(object.toString())) {
				media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
			} else {
				media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
			}
			brandService.addEntity(IMediaDao.class, media);
		}
		return BaseResponse.success();
	}

	/**
	 * @Description:转载品牌秀
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/brand/reproduce")
	@ApiOperation(value = "转载品牌秀", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "brandId", value = "标题", dataType = "string", paramType = "query", required = true) })
	public BaseResponse query(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String brandId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		String citycode = request.getAttribute("citycode") + "";
		Brand brand = (Brand) brandService.queryEntityById(IBrandDao.class,
				brandId);
		if (null == brand) {
			throw new ServiceException("转载内容不存在");
		}
		brand.setId(UUIDUtil.get32UUID());
		brand.setBrandId(brandId);
		brand.setType(Global.SYS_MEMBER_BRANDSHOW_RESOURCE_2);
		String reproduceId = brand.getAuthorId();
		if (!StringUtils.isEmpty(reproduceId)) {
			brand.setAuthorId(reproduceId);
			Member member = (Member) brandService.queryEntityById(
					IMemberDao.class, reproduceId);
			if (null != member) {
				if (member.getId().equals(memberId)) {
					throw new ServiceException("不能转载自己的作品");
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
			if (Global.isImage(object.toString())) {
				media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
			} else {
				media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
			}
			brandService.addEntity(IMediaDao.class, media);
		}
		return BaseResponse.success();
	}

	/**
	 * @Description:品牌秀列表
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/brand/list")
	@ApiOperation(value = "品牌秀列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "memberId", value = "会员ID", dataType = "string", paramType = "query"), })
	public BrandListResp list(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false) String memberId) throws Exception {
		Page page = new Page(currentPage, pageSize);
		if (StringUtils.isEmpty(memberId)) {
			throw new ServiceException("用户ID不能为空");
		}
		Map<String, Object> map = new HashMap<>();
		page.setSort("t.c_createDate");
		map.put("memberId", memberId);
		map.put("page", page);
		@SuppressWarnings("unchecked")
		List<Brand> brands = (List<Brand>) brandService.queryPageEntity(
				IBrandDao.class, map);
		return BrandListResp.builder().list(brands).page(page).build();
	}
}
