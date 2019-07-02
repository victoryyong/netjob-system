package com.thsword.netjob.web.controller.app.collect;

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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.ICollectDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Collect;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.CollectService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

@RestController
@Api(tags = "NETJOB-COLLECT", description = "收藏接口")
public class CollectApp {
	@Resource(name = "collectService")
	CollectService collectService;

	/**
	 * 
	 * @Description:收藏
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/collect")
	@ApiOperation(value = "收藏", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "类型（1-会员 2-服务 3-需求）", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "bizId", value = "业务ID", dataType = "string", paramType = "query") })
	public BaseResponse collect(HttpServletRequest request,
			HttpServletResponse response, @RequestParam int type,
			@RequestParam String bizId) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Collect temp = new Collect();
		temp.setMemberId(memberId);
		temp.setBizId(bizId);
		temp = (Collect) collectService.queryEntity(ICollectDao.class, temp);
		if (null != temp) {
			throw new ServiceException("请勿重复收藏");
		}
		// 会员收藏
		if (type == Global.SYS_COLLECT_TYPE_1) {
			Member member = (Member) collectService.queryEntityById(
					IMemberDao.class, bizId);
			if (null == member) {
				throw new ServiceException("收藏对象不存在");
			}
			member.setFans(member.getFans() + 1);
			collectService.updateEntity(IMemberDao.class, member);
			// 服务或者需求收藏
		} else if (type == Global.SYS_COLLECT_TYPE_2
				|| type == Global.SYS_COLLECT_TYPE_3) {
			Serve serve = (Serve) collectService.queryEntityById(
					IServeDao.class, bizId);
			if (null == serve) {
				throw new ServiceException("收藏对象不存在");
			}
		}
		Collect collect = new Collect();
		collect.setType(type);
		collect.setBizId(bizId);
		collect.setId(UUIDUtil.get32UUID());
		collect.setMemberId(memberId);
		collect.setCreateBy(memberId);
		collect.setUpdateBy(memberId);
		collectService.addEntity(ICollectDao.class, collect);
		return BaseResponse.success();
	}

	/**
	 * 
	 * @Description:我的收藏列表
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/getCollects")
	@ApiOperation(value = "我的收藏列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "type", value = "收藏类型（1-会员 2-服务 3-需求）", dataType = "string", paramType = "query"), })
	public JSONObject collects(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam int type) throws Exception {
		Page page = new Page(currentPage, pageSize);
		String memberId = request.getAttribute("memberId") + "";
		Map<String, Object> map = new HashMap<>();
		page.setSort("c.c_createDate");
		map.put("memberId", memberId);
		map.put("type", type);
		map.put("page", page);
		JSONObject obj = new JSONObject();
		if (type == Global.SYS_COLLECT_TYPE_1) {
			List<Member> members = (List<Member>) collectService
					.queryPageMembers(map);
			obj.put("list", members);
		} else if (type == Global.SYS_COLLECT_TYPE_2
				|| type == Global.SYS_COLLECT_TYPE_3) {
			List<Serve> serves = (List<Serve>) collectService
					.queryPageServes(map);
			obj.put("list", serves);
		}
		obj.put("page", page);
		return obj;
	}

	/**
	 * 
	 * @Description:是否已收藏
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/isCollected")
	@ApiOperation(value = "是否已收藏", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "bizId", value = "业务ID", dataType = "string", paramType = "query") })
	public JSONObject isCollected(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String bizId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		JSONObject obj = new JSONObject();
		Collect temp = new Collect();
		temp.setBizId(bizId);
		temp.setMemberId(memberId);
		temp = (Collect) collectService.queryEntity(ICollectDao.class, temp);
		if (null != temp) {
			obj.put("isCollected", true);
		} else {
			obj.put("isCollected", false);
		}
		return obj;
	}

	/**
	 * 取消收藏
	 * 
	 * @Description:TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception:
	 * @author: yong
	 * @time:2018年5月14日 上午11:45:35
	 */
	@RequestMapping("app/member/unCollect")
	@ApiOperation(value = "取消收藏", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "bizId", value = "业务ID", dataType = "string", paramType = "query") })
	public BaseResponse unCollect(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String bizId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Collect temp = new Collect();
		temp.setBizId(bizId);
		temp.setMemberId(memberId);
		temp = (Collect) collectService.queryEntity(ICollectDao.class, temp);
		if (null != temp) {
			collectService.deleteEntityById(ICollectDao.class, temp.getId());
			// 会员收藏
			if (temp.getType() == Global.SYS_COLLECT_TYPE_1) {
				Member member = (Member) collectService.queryEntityById(
						IMemberDao.class, temp.getBizId());
				member.setFans(member.getFans() - 1);
				collectService.updateEntity(IMemberDao.class, member);
				// 服务或者需求收藏
			}
		}
		return BaseResponse.success();
	}
}
