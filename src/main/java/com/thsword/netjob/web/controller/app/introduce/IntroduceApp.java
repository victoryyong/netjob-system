package com.thsword.netjob.web.controller.app.introduce;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thsword.netjob.dao.IIntroduceDao;
import com.thsword.netjob.pojo.app.Introduce;
import com.thsword.netjob.service.IntroduceService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;

@Controller
public class IntroduceApp {
	@Resource(name = "introduceService")
	IntroduceService introduceService;

	/**
	 * 
	
	 * @Description:查询个人介绍
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/introduce/info")
	public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if(StringUtils.isEmpty(memberId)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "memberId不能为空", response, request);
				return;
			}
			Introduce introduce = new Introduce();
			introduce.setMemberId(memberId);
			introduce = (Introduce) introduceService.queryEntity(IIntroduceDao.class, introduce);
			JsonResponseUtil.successBodyResponse(introduce, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 
	
	 * @Description:新增或编辑个人介绍
	
	 * @param request
	 * @param response
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/introduce/addOrEdit")
	public void edit(HttpServletRequest request, HttpServletResponse response,Introduce introduce) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			/*if(StringUtils.isEmpty(introduce.getMemberId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "memberId不能为空", response, request);
				return;
			}*/
			if(StringUtils.isEmpty(introduce.getMotto())&&
					StringUtils.isEmpty(introduce.getAdvantage())&&
							StringUtils.isEmpty(introduce.getExperience())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "内容不能为空", response, request);
				return;
			}
			Introduce temp = new Introduce();
			temp.setMemberId(memberId);
			temp = (Introduce) introduceService.queryEntity(IIntroduceDao.class, temp);
			if(null!=temp){
				introduceService.updateEntity(IIntroduceDao.class, introduce);
			}else{
				
				introduce.setId(UUIDUtil.get32UUID());
				introduce.setMemberId(memberId);
				introduce.setCreateBy(memberId);
				introduce.setUpdateBy(memberId);
				introduceService.addEntity(IIntroduceDao.class, introduce);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
