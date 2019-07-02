package com.thsword.netjob.web.controller.app.agree;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IActiveDao;
import com.thsword.netjob.dao.IAgreeDao;
import com.thsword.netjob.dao.IBrandDao;
import com.thsword.netjob.dao.IMediaDao;
import com.thsword.netjob.dao.IMemberDao;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Active;
import com.thsword.netjob.pojo.app.Agree;
import com.thsword.netjob.pojo.app.Brand;
import com.thsword.netjob.pojo.app.Media;
import com.thsword.netjob.pojo.app.Member;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.AgreeService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;

@RestController
@Api(tags = "NETJOB-AGREE", description = "点赞接口")
public class AgreeApp {
	@Resource(name = "agreeService")
	AgreeService agreeService;

	@RequestMapping("app/member/agree")
	@ApiOperation(value = "点赞", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "agreeId", value = "点赞对象ID", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "点赞类型（1-会员 2-品牌秀 3-服务&需求 4-动态 5-媒体文件（热播视频/图片））", dataType = "int", paramType = "query") })
	public BaseResponse agree(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String agreeId,
			@RequestParam int type) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Agree temp = new Agree();
		temp.setMemberId(memberId);
		temp.setAgreeId(agreeId);
		temp = (Agree) agreeService.queryEntity(IAgreeDao.class, temp);
		if (null != temp) {
			throw new ServiceException("请勿重复点赞");
		} else {
			if (type == Global.SYS_AGREE_TYPE_1) {
				Member member = (Member) agreeService.queryEntityById(
						IMemberDao.class, agreeId);
				if (member == null) {
					throw new ServiceException("点赞对象不存在");

				}
				member.setAgrees(member.getAgrees() + 1);
				agreeService.updateEntity(IMemberDao.class, member);
			} else if (type == Global.SYS_AGREE_TYPE_2) {
				Brand brand = (Brand) agreeService.queryEntityById(
						IBrandDao.class, agreeId);
				if (brand == null) {
					throw new ServiceException("点赞对象不存在");

				}
				brand.setAgrees(brand.getAgrees() + 1);
				agreeService.updateEntity(IBrandDao.class, brand);
			} else if (type == Global.SYS_AGREE_TYPE_3) {
				Serve serve = (Serve) agreeService.queryEntityById(
						IServeDao.class, agreeId);
				if (serve == null) {
					throw new ServiceException("点赞对象不存在");

				}
				serve.setAgrees(serve.getAgrees() + 1);
				agreeService.updateEntity(IServeDao.class, serve);
			} else if (type == Global.SYS_AGREE_TYPE_4) {
				Active active = (Active) agreeService.queryEntityById(
						IActiveDao.class, agreeId);
				if (active == null) {
					throw new ServiceException("点赞对象不存在");
				}
				active.setAgrees(active.getAgrees() + 1);
				agreeService.updateEntity(IActiveDao.class, active);
			} else if (type == Global.SYS_AGREE_TYPE_5) {
				Media media = (Media) agreeService.queryEntityById(
						IMediaDao.class, agreeId);
				if (media == null) {
					throw new ServiceException("点赞对象不存在");
				}
				media.setAgrees(media.getAgrees() + 1);
				agreeService.updateEntity(IMediaDao.class, media);
			} else {
				throw new ServiceException("类型异常");
			}
			Agree agree = new Agree();
			agree.setMemberId(memberId);
			agree.setAgreeId(agreeId);
			agree.setId(UUIDUtil.get32UUID());
			agree.setCreateBy(memberId);
			agree.setUpdateBy(memberId);
			agreeService.addEntity(IAgreeDao.class, agree);
		}
		return BaseResponse.success();
	}

	@RequestMapping("app/member/unAgree")
	@ApiOperation(value = "取消点赞", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "agreeId", value = "点赞对象ID（会员/品牌秀/服务&需求/动态/媒体文件）", dataType = "string", paramType = "query") })
	public BaseResponse unAgree(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String agreeId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Agree temp = new Agree();
		temp.setMemberId(memberId);
		temp.setAgreeId(agreeId);
		temp = (Agree) agreeService.queryEntity(IAgreeDao.class, agreeId);
		if (null != temp) {
			if (temp.getType() == Global.SYS_AGREE_TYPE_1) {
				Member member = (Member) agreeService.queryEntityById(
						IMemberDao.class, agreeId);
				if (member != null) {
					member.setAgrees(member.getAgrees() - 1);
					agreeService.updateEntity(IMemberDao.class, member);
				}
			} else if (temp.getType() == Global.SYS_AGREE_TYPE_2) {
				Brand brand = (Brand) agreeService.queryEntityById(
						IBrandDao.class, agreeId);
				if (brand != null) {
					brand.setAgrees(brand.getAgrees() - 1);
					agreeService.updateEntity(IBrandDao.class, brand);
				}
			} else if (temp.getType() == Global.SYS_AGREE_TYPE_3) {
				Serve serve = (Serve) agreeService.queryEntityById(
						IServeDao.class, agreeId);
				if (serve != null) {
					serve.setAgrees(serve.getAgrees() - 1);
					agreeService.updateEntity(IServeDao.class, serve);
				}
			} else if (temp.getType() == Global.SYS_AGREE_TYPE_4) {
				Active active = (Active) agreeService.queryEntityById(
						IActiveDao.class, agreeId);
				if (active != null) {
					active.setAgrees(active.getAgrees() - 1);
					agreeService.updateEntity(IActiveDao.class, active);
				}
			} else if (temp.getType() == Global.SYS_AGREE_TYPE_5) {
				Media media = (Media) agreeService.queryEntityById(
						IMediaDao.class, agreeId);
				if (media != null) {
					media.setAgrees(media.getAgrees() - 1);
					agreeService.updateEntity(IMediaDao.class, media);
				}
			}
			agreeService.deleteEntity(IAgreeDao.class, temp);
		}
		return BaseResponse.success();
	}

	@RequestMapping("app/member/isAgreed")
	@ApiOperation(value = "查询是否已点赞", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "agreeId", value = "点赞对象ID（会员/品牌秀/服务&需求/动态/媒体文件）", dataType = "string", paramType = "query") })
	public JSONObject isAgreed(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String agreeId)
			throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Agree history = new Agree();
		history.setMemberId(memberId);
		history.setAgreeId(agreeId);
		history = (Agree) agreeService.queryEntity(IAgreeDao.class, history);
		JSONObject obj = new JSONObject();
		if (null == history) {
			obj.put("isAgree", false);
		} else {
			obj.put("isAgree", true);
		}
		return obj;
	}
}
