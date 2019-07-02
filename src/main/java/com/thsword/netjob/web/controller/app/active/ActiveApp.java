package com.thsword.netjob.web.controller.app.active;

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
import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IActiveDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Active;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.service.ActiveService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@Api(tags = "NETJOB-ACTIVE", description = "动态接口")
@RestController
public class ActiveApp {
	@Resource(name = "activeService")
	ActiveService activeService;

	@RequestMapping("app/member/active/add")
	@ApiOperation(value = "添加个人动态", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "title", value = "标题", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "links", value = "地址连接集合格式[\"\",\"\"]", dataType = "string", paramType = "query") })
	public BaseResponse add(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String title,
			@RequestParam String links) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		String citycode = request.getAttribute("citycode") + "";
		Active active = new Active();
		active.setTitle(title);
		active.setLinks(links);
		active.setId(UUIDUtil.get32UUID());
		active.setMemberId(memberId);
		active.setCreateBy(memberId);
		active.setUpdateBy(memberId);
		activeService.addEntity(IActiveDao.class, active);
		JSONArray linkArray = JSONArray.parseArray(active.getLinks());
		for (Object object : linkArray) {
			Media media = new Media();
			media.setId(UUIDUtil.get32UUID());
			media.setBizId(active.getId());
			media.setCreateBy(memberId);
			media.setMemberId(memberId);
			media.setUpdateBy(memberId);
			media.setCitycode(citycode);
			media.setLink(object.toString());
			media.setResource(Global.SYS_MEMBER_ACTIVE_RESOURCE_1);
			if (Global.isImage(object.toString())) {
				media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_1);
			} else {
				media.setType(Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
			}
			activeService.addEntity(IMediaDao.class, media);
		}
		return BaseResponse.success();
	}

	@RequestMapping("app/visitor/active/list")
	@ApiOperation(value = "查询个人动态", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"), })
	public JSONObject list(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage)
			throws Exception {
		Page page = new Page(currentPage, pageSize);
		String memberId = request.getParameter("memberId");
		if (StringUtils.isEmpty(memberId)) {
			throw new ServiceException("用户ID不能为空");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberId", memberId);
		map.put("page", page);
		@SuppressWarnings("unchecked")
		List<Active> actives = (List<Active>) activeService.queryPageEntity(
				IActiveDao.class, map);
		JSONObject obj = new JSONObject();
		obj.put("page", page);
		obj.put("list", actives);
		return obj;
	}
}
