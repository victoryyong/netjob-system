package com.thsword.netjob.web.controller.app.agree;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

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
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;

@Controller
public class AgreeApp {
	@Resource(name = "agreeService")
	AgreeService agreeService;

	@RequestMapping("app/member/agree")
	public void agree(HttpServletRequest request, HttpServletResponse response,Agree agree) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if(StringUtils.isEmpty(agree.getAgreeId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "ID不能为空", response, request);
				return;
			}
			agree.setMemberId(memberId);
			Agree history = (Agree) agreeService.queryEntity(IAgreeDao.class, agree);
			if(null!=history){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "请勿重复点赞", response, request);
				return;
			}else{
				if(agree.getType()==Global.SYS_AGREE_TYPE_1){
					Member member = (Member) agreeService.queryEntityById(IMemberDao.class, agree.getAgreeId());
					if(member==null){
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞对象不存在", response, request);
						return;
					}
					member.setAgrees(member.getAgrees()+1);
					agreeService.updateEntity(IMemberDao.class, member);
				}else if(agree.getType()==Global.SYS_AGREE_TYPE_2){
					Brand brand = (Brand) agreeService.queryEntityById(IBrandDao.class, agree.getAgreeId());
					if(brand==null){
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞对象不存在", response, request);
						return;
					}
					brand.setAgrees(brand.getAgrees()+1);
					agreeService.updateEntity(IBrandDao.class, brand);
				}else if(agree.getType()==Global.SYS_AGREE_TYPE_3){
					Serve serve = (Serve) agreeService.queryEntityById(IServeDao.class, agree.getAgreeId());
					if(serve==null){
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞对象不存在", response, request);
						return;
					}
					serve.setAgrees(serve.getAgrees()+1);
					agreeService.updateEntity(IServeDao.class, serve);
				}else if(agree.getType()==Global.SYS_AGREE_TYPE_4){
					/*Brand brand = (Brand) agreeService.queryEntityById(IBrandDao.class, agree.getAgreeId());
				if(brand==null){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞对象不存在", response, request);
					return;
				}*/
				}else if(agree.getType()==Global.SYS_AGREE_TYPE_5){
					Media media = (Media) agreeService.queryEntityById(IMediaDao.class, agree.getAgreeId());
					if(media==null){
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞对象不存在", response, request);
						return;
					}
					media.setAgrees(media.getAgrees()+1);
					agreeService.updateEntity(IMediaDao.class, media);
				}else{
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型异常", response, request);
					return;
				}
				agree.setId(UUIDUtil.get32UUID());
				agree.setCreateBy(memberId);
				agree.setUpdateBy(memberId);
				agreeService.addEntity(IAgreeDao.class, agree);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("app/member/unAgree")
	public void unAgree(HttpServletRequest request, HttpServletResponse response,Agree agree) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if(StringUtils.isEmpty(agree.getAgreeId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "ID不能为空", response, request);
				return;
			}
			agree.setMemberId(memberId);
			Agree history = (Agree) agreeService.queryEntity(IAgreeDao.class, agree);
			if(null!=history){
				if(history.getType()==Global.SYS_AGREE_TYPE_1){
					Member  member = (Member) agreeService.queryEntityById(IMemberDao.class, agree.getAgreeId());
					if(member!=null){
						member.setAgrees(member.getAgrees()-1);
						agreeService.updateEntity(IMemberDao.class, member);
					}
				}else if(history.getType()==Global.SYS_AGREE_TYPE_2){
					Brand brand = (Brand) agreeService.queryEntityById(IBrandDao.class, agree.getAgreeId());
					if(brand!=null){
						brand.setAgrees(brand.getAgrees()-1);
						agreeService.updateEntity(IBrandDao.class, brand);
					}
				}else if(history.getType()==Global.SYS_AGREE_TYPE_3){
					Serve serve = (Serve) agreeService.queryEntityById(IServeDao.class, agree.getAgreeId());
					if(serve!=null){
						serve.setAgrees(serve.getAgrees()-1);
						agreeService.updateEntity(IServeDao.class, serve);
					}
				}else if(history.getType()==Global.SYS_AGREE_TYPE_4){
					Active active = (Active) agreeService.queryEntityById(IActiveDao.class, agree.getAgreeId());
					if(active==null){
						JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞对象不存在", response, request);
						return;
					}
				}else if(history.getType()==Global.SYS_AGREE_TYPE_5){
					Media media = (Media) agreeService.queryEntityById(IMediaDao.class, agree.getAgreeId());
					if(media!=null){
						media.setAgrees(media.getAgrees()-1);
						agreeService.updateEntity(IMediaDao.class, media);
					}
				}
				agreeService.deleteEntity(IAgreeDao.class, history);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("app/member/isAgreed")
	public void isAgreed(HttpServletRequest request, HttpServletResponse response,Agree agree) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			agree.setMemberId(memberId);
			Agree history = (Agree) agreeService.queryEntity(IAgreeDao.class, agree);
			JSONObject obj = new JSONObject();
			if(null==history){
				obj.put("isAgree", false);
			}else{
				obj.put("isAgree", true);
			}
			JsonResponseUtil.successBodyResponse(obj,response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
