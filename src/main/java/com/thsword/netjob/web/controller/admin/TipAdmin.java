package com.thsword.netjob.web.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.dao.ITipDao;
import com.thsword.netjob.pojo.app.Tip;
import com.thsword.netjob.service.IBaseService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
/**
 * 提示

 * @Description提示

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class TipAdmin {
	@Resource(name="baseService")
	IBaseService baseService;
	@RequestMapping("admin/tip/add")
	public void add(HttpServletRequest request,HttpServletResponse response,Tip tip) throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			if(StringUtils.isEmpty(tip.getName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "名称不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(tip.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "类型不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(tip.getContent())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "内容不能为空",response,request);
				return;
			}
			tip.setId(UUIDUtil.get32UUID());
			tip.setCreateBy(userId);
			tip.setUpdateBy(userId);
			baseService.addEntity(ITipDao.class, tip);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("admin/tip/edit")
	public void edit(HttpServletRequest request,HttpServletResponse response,Tip tip) throws Exception{
		try {
			if(StringUtils.isEmpty(tip.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空",response,request);
				return;
			}
			baseService.updateEntity(IDictDao.class, tip);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("admin/tip/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,Tip tip) throws Exception{
		try {
			if(StringUtils.isEmpty(tip.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空",response,request);
				return;
			}
			baseService.deleteEntity(IDictDao.class, tip);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("admin/tip/info")
	public void info(HttpServletRequest request,HttpServletResponse response,Tip tip) throws Exception{
		try {
			if(StringUtils.isEmpty(tip.getType())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空",response,request);
				return;
			}
			baseService.queryEntity(ITipDao.class, tip);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
