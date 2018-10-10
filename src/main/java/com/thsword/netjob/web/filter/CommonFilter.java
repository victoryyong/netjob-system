package com.thsword.netjob.web.filter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;


/***
 * token和权限认证拦截
 */
public class CommonFilter extends HttpServlet implements HandlerInterceptor {
	private static final long serialVersionUID = 5836290769748648967L;
	private static final Log log = LogFactory.getLog(CommonFilter.class);

	public String[] logUrls;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		String url = request.getRequestURI();
		if (null != logUrls && logUrls.length >= 1) {
			for (String logUrl : logUrls) {
				if (url.indexOf(logUrl) >= 0) {
					log.info(">>>>>>> Finshed handling request[" + url + "]");
				}
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj, ModelAndView modelView)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
                response.addHeader("Access-Control-Expose-Headers", "*");
		response.setCharacterEncoding("UTF-8");
		if(request.getMethod().equals("OPTIONS")){
	                    response.setHeader("Access-Control-Allow-Methods", "*");
	                    response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	                    response.setHeader("Access-Control-Expose-Headers", "*");
	                    JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "options check",response,request);
	                    return false;
        }
		String url = request.getRequestURI();
		if (null != logUrls && logUrls.length >= 1) {
			for (String logUrl : logUrls) {
				if (url.indexOf(logUrl) >= 0) {
					log.info("logBegin Request[" + url + "]");
				}
			}
		}
		return true;
	}

	public String[] getLogUrls() {
		return logUrls;
	}

	public void setLogUrls(String[] logUrls) {
		this.logUrls = logUrls;
	}
	
}
