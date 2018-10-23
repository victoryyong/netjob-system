package com.thsword.netjob.web.controller.app.agree;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thsword.netjob.pojo.app.Agree;
import com.thsword.netjob.service.AgreeService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;

@Controller
public class AccountApp {
	@Resource(name = "agreeService")
	AgreeService agreeService;

	@RequestMapping("app/agree/add")
	public void record(HttpServletRequest request, HttpServletResponse response,Agree agree) throws Exception {
		try {
			Agree agree =
			if(StringUtils.isEmpty(type)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "点赞类型不能为空", response, request);
				return;
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
