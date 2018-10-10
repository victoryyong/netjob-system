package com.thsword.netjob.web.filter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;

/***
 * 第三方应用APPID ACCESSKEY认证
 */
public class WXFilter extends HttpServlet implements HandlerInterceptor {
	private static final long serialVersionUID = 5836290769748648967L;
	@Resource(name="accessService")
	private AccessService accessService;
	public String[] allowUrls;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView modelView) throws Exception {
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		//随机数
		String echostr = request.getParameter("echostr");
		if(StringUtils.isEmpty(nonce)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "nonce不能为空",response,request);
			return false;
		}else if(StringUtils.isEmpty(signature)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "signature不能为空",response,request);
			return false;
		}else if(StringUtils.isEmpty(timestamp)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "timestamp不能为空",response,request);
			return false;
		}else if(StringUtils.isEmpty(echostr)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "echostr不能为空",response,request);
			return false;
		}else{
			Access access = new Access();
			access.setAppId("netjob_AFEDLOAKDOQKDOW");
			access = (Access) accessService.queryEntity(IAccessDao.class, access);
			if(null==access){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无效的appId",response,request);
				return false;
			}
			/* 
		     * 微信服务器发送过来的signature是通过某些处理然后进行SHA1加密的，我们来用它发过来的信息自己生成一个signature， 
		     * 然后两者之间进行比对，一致的话我们就是伙伴，不一致就拒绝它 
		     */  
		    String[] arr = {access.getSecretKey(),timestamp,nonce};
		    /* token是我们自己填写的，具体填写位置见下图；timestamp、nonce是微信服务器发送过来的，这里我们把他们都放到数组中。*/  
		    Arrays.sort(arr);//对数组中的数据进行字典排序。。。。要不然加密出来的东西是没用的  
		    StringBuilder content = new StringBuilder();  
		    for (int i = 0; i < arr.length; i++) {  
		        content.append(arr[i]);  
		    }//把字典排序好的数组读取成字符串。  
		    MessageDigest md = null;  
		    String tmpStr = null;  
		  
		    try {/*进行sha1加密*/  
		        md = MessageDigest.getInstance("SHA-1");  
		        byte[] digest = md.digest(content.toString().getBytes());  
		        tmpStr = byteToStr(digest).toLowerCase();//将加密后的byte转化为16进制字符串，这就是我们自己构造的signature  
		    } catch (NoSuchAlgorithmException e) {  
		        e.printStackTrace();  
		    }  
			if(signature.equals(tmpStr)){
				return true;
			}else{
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "sign异常",response,request);
				return false;
			}
		}
	}
	
	private static String byteToStr(byte[] byteArray) {  
	    String strDigest = "";  
	    for (int i = 0; i < byteArray.length; i++) {  
	        strDigest += byteToHexStr(byteArray[i]);//分别把没一个byte位转换成一个16进制字符，代码见下面  
	    }  
	    return strDigest;  
	}  
	
	private static String byteToHexStr(byte mByte) {  
	    char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',  
	            'B', 'C', 'D', 'E', 'F' };  
	    char[] tempArr = new char[2];  
	    tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
	    tempArr[1] = Digit[mByte & 0X0F];  
	    String s = new String(tempArr);  
	    return s;  
	}
    
	public String[] getAllowUrls() {
		return allowUrls;
	}
	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}
}
