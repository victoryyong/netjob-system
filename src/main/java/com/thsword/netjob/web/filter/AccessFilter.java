package com.thsword.netjob.web.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thsword.netjob.dao.IAccessDao;
import com.thsword.netjob.pojo.app.Access;
import com.thsword.netjob.service.AccessService;
import com.thsword.netjob.service.RedisService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.SecurityUtils;

/***
 * 第三方应用APPID ACCESSKEY认证
 */
public class AccessFilter extends HttpServlet implements HandlerInterceptor {
	private static final long serialVersionUID = 5836290769748648967L;
	@Resource(name="accessService")
	private AccessService accessService;
	@Resource(name="redisService")
	private RedisService redisService;
	
	public String[] allowUrls;
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		String url = request.getRequestURI();
		if(null != allowUrls && allowUrls.length>=1)  {
			for (String allowUrl : allowUrls) {
				if(url.indexOf(allowUrl)>=0){
					break;
				}
			}
		}
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView modelViews) throws Exception {
	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		String url = request.getRequestURI();
		//token认证
		if(null != allowUrls && allowUrls.length>=1)  {
			for (String allowUrl : allowUrls) {
				if(url.indexOf(allowUrl)>=0){
					return true;
				}
			}
		}
		String appId = request.getHeader("appId");
		String sign = request.getHeader("sign");
		String timestamp = request.getHeader("timestamp");
		//随机数
		String nonceStr = request.getHeader("nonceStr");
		if(StringUtils.isEmpty(appId)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "appId不能为空",response,request);
			return false;
		}else if(StringUtils.isEmpty(sign)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "sign不能为空",response,request);
			return false;
		}else if(StringUtils.isEmpty(timestamp)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "timestamp不能为空",response,request);
			return false;
		}else if(StringUtils.isEmpty(nonceStr)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "nonceStr不能为空",response,request);
			return false;
		}else{
			Access access = new Access();
			access.setAppId(appId);
			access = (Access) accessService.queryEntity(IAccessDao.class, access);
			if(null==access){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无效的appId",response,request);
				return false;
			}
			String old_sign = redisService.get("app:sign:"+sign);
			//防止重复请求
			if(!StringUtils.isEmpty(old_sign)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "重复请求!",response,request);
				//return false;
				return true;
			}
			Map<String,Object> maps = new HashMap<String, Object>();
			Enumeration em = request.getParameterNames();
			 while (em.hasMoreElements()) {
			    String name = (String) em.nextElement();
			    String value = request.getParameter(name);
		    	if(StringUtils.isEmpty(value)){
		    		maps.put(name, null);
		    	}else{
		    		maps.put(name, value);
		    	}
			}
			maps.put("appId", access.getAppId());
			maps.put("secretKey", access.getSecretKey());
			maps.put("appId", access.getAppId());
			maps.put("timestamp", timestamp);
			maps.put("nonceStr", nonceStr);
			String paramString = getOrderByLexicographic(maps);
			Date time = new Date(Long.parseLong(timestamp));
			if(!time.before(new Date(System.currentTimeMillis()+2*60000))&&!time.after(new Date(System.currentTimeMillis()-2*60000))){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "超过有效期或服务器时间非标准时间",response,request);
				return false;
			}
			String authcSign = SecurityUtils.getSha1(paramString);
			if(sign.equals(authcSign)){
				redisService.set("app:sign:"+sign,sign);
				redisService.expire("app:sign:"+sign,150);
				return true;
			}else{
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "sign异常",response,request);
				return false;
			}
		}
	}
	
	/** 
     * 拼接排序好的参数名称和参数值 
     * @param paramNames 排序后的参数名称集合 
     * @param maps 参数key-value map集合 
     * @return String 拼接后的字符串 
     */  
    private static String splitParams(List<String> paramNames,Map<String,Object> maps){  
        StringBuilder paramStr = new StringBuilder();  
        for(String paramName : paramNames){  
            paramStr.append(paramName);  
            for(Map.Entry<String,Object> entry : maps.entrySet()){  
                if(paramName.equals(entry.getKey())){  
                    paramStr.append("="+String.valueOf(entry.getValue())+"&");  
                }  
            }  
        }  
        paramStr.deleteCharAt(paramStr.length()-1);
        return paramStr.toString();  
    }
	 /** 
     * 获取参数的字典排序 
     * @param maps 参数key-value map集合 
     * @return String 排序后的字符串 
     */  
    private static String getOrderByLexicographic(Map<String,Object> maps){  
    	//System.out.println(splitParams(lexicographicOrder(getParamsName(maps)),maps));
        return splitParams(lexicographicOrder(getParamsName(maps)),maps);  
    } 
    /** 
     * 获取参数名称 key 
     * @param maps 参数key-value map集合 
     * @return 
     */  
    private static List<String> getParamsName(Map<String,Object> maps){  
        List<String> paramNames = new ArrayList<String>();  
        for(Map.Entry<String,Object> entry : maps.entrySet()){  
            paramNames.add(entry.getKey());  
        }  
        return paramNames;  
    }
    /** 
     * 参数名称按字典排序 
     * @param paramNames 参数名称List集合 
     * @return 排序后的参数名称List集合 
     */  
    private static List<String> lexicographicOrder(List<String> paramNames){  
        Collections.sort(paramNames);  
        return paramNames;  
    }
    
	public String[] getAllowUrls() {
		return allowUrls;
	}
	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}
	public static void main(String[] args) {
		System.out.println(SecurityUtils.getSha1("AADFZFDD567TH7U5T3TERF3"));
	}
}
