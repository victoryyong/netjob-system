package com.thsword.netjob.web.controller.app.introduce;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thsword.netjob.dao.IIntroduceDao;
import com.thsword.netjob.pojo.app.Introduce;
import com.thsword.netjob.service.IntroduceService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;

@RestController
@Api(tags = "NETJOB-FRIEND", description = "个人简介")
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
	@ApiOperation(value = "查询个人介绍", httpMethod = "POST")
	public Introduce query(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String memberId = request.getAttribute("memberId")+"";
			Introduce introduce = new Introduce();
			introduce.setMemberId(memberId);
			introduce = (Introduce) introduceService.queryEntity(IIntroduceDao.class, introduce);
			return introduce;
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
	@ApiOperation(value = "新增或编辑个人介绍", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "motto", value = "格言", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "advantage", value = "擅长", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "experience", value = "资历", dataType = "string", paramType = "query")
	})
	public BaseResponse edit(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String motto,
			@RequestParam(required = false) String advantage,
			@RequestParam(required = false) String experience
			) throws Exception {
			String memberId = request.getAttribute("memberId")+"";
			if(StringUtils.isEmpty(motto)&&
					StringUtils.isEmpty(advantage)&&
							StringUtils.isEmpty(experience)){
				throw new ServiceException("内容不能为空");
			}
			Introduce temp = new Introduce();
			temp.setMemberId(memberId);
			temp = (Introduce) introduceService.queryEntity(IIntroduceDao.class, temp);
			Introduce introduce = new Introduce();
			introduce.setMemberId(memberId);
			introduce.setMotto(motto);
			introduce.setAdvantage(advantage);
			introduce.setExperience(experience);
			if(null!=temp){
				introduceService.updateEntity(IIntroduceDao.class, introduce);
			}else{
				introduce.setId(UUIDUtil.get32UUID());
				introduce.setMemberId(memberId);
				introduce.setCreateBy(memberId);
				introduce.setUpdateBy(memberId);
				introduceService.addEntity(IIntroduceDao.class, introduce);
			}
			return BaseResponse.success();
	}
}
