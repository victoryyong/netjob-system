package com.thsword.netjob.web.controller.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thsword.netjob.pojo.app.Banner;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.page.Page;
/**
 * 微信验证

 * @Description:微信验证

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class AuthcWX {
	/**
	 * 微信验证
	
	 * @Description:TODO
	
	 * @param request
	 * @param response
	 * @param banner
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 下午5:03:29
	 */
	@RequestMapping("wx/authc")
	public void authc(HttpServletRequest request,HttpServletResponse response,Banner banner,Page page) throws Exception{
		try {
			String echostr = request.getParameter("echostr");
			JsonResponseUtil.responseMsg(echostr, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
