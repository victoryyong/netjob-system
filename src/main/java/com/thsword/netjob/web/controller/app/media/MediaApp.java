package com.thsword.netjob.web.controller.app.media;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
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

import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.resp.media.MediaListResp;
import com.thsword.netjob.service.MemberService;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-MEDIA", description = "媒体文件")
public class MediaApp {
	@Resource(name = "memberService")
	MemberService memberService;

	/**
	 * 
	
	 * @Description:热播视频
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("app/visitor/index/videos")
	@ApiOperation(value = "热播视频", httpMethod = "POST")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10") })
	public MediaListResp list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String longitude,
			@RequestParam(required = false) String latitude,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage
			) throws Exception {
			Page page = new Page(currentPage, pageSize);
			String userId = request.getAttribute("userId")+"";
			if(!StringUtils.isEmpty(latitude)&&!StringUtils.isEmpty(latitude)){
				Member member = new Member();
				member.setId(userId);
				member.setLatitude(latitude);
				member.setLongitude(longitude);
				memberService.updateEntity(IMemberDao.class, member);
			};
			Map<String, Object> map = new HashMap<>();
			List<Integer> resources = new ArrayList();
			resources.add(Global.SYS_MEMBER_ACTIVE_RESOURCE_1);
			resources.add(Global.SYS_MEMBER_ACTIVE_RESOURCE_2);
			String citycode = request.getParameter("citycode");
			map.put("page", page);
			map.put("citycode", citycode);
			map.put("resources", resources);
			map.put("type", Global.SYS_MEMBER_ACTIVE_FILE_TYPE_2);
			List<Media> medias = (List<Media>) memberService.queryPageEntity(IMediaDao.class, map);
			if(StringUtils.isEmpty(medias)){
				map.remove("citycode");
				medias = (List<Media>) memberService.queryPageEntity(IMediaDao.class, map);
			}
			return MediaListResp.builder().list(medias).page(page).build();
	}
}
