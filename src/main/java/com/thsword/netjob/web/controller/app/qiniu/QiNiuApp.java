package com.thsword.netjob.web.controller.app.qiniu;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.thsword.netjob.dao.IAccessDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Access;
import com.thsword.netjob.service.AccessService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
/**
 * 菜单

 * @Description:TODO

 * @author:yong

 * @time:2018年5月10日 下午8:52:49
 */
@Controller
public class QiNiuApp {
	@Resource(name = "accessService")
	AccessService accessService;

	/**
	 * 
	
	 * 获取上传token
	
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	
	 * void
	
	 * @exception:
	
	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/getUpToken")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Access access = new Access();
			access.setAppId(Global.getSetting(Global.QINIU_UPLOAD_ACCESS_KEY));
			access = (Access) accessService.queryEntity(IAccessDao.class, access);
			if(null==access){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "系统key异常", response, request);
				return;
			}
			Auth auth = Auth.create(Global.getSetting(Global.QINIU_UPLOAD_ACCESS_KEY), access.getSecretKey());
			StringMap putPolicy = new StringMap();
			putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
			long expireSeconds = 3600;
			String upToken = auth.uploadToken(Global.QINIU_UPLOAD_BOCKET_NAME, null, expireSeconds, putPolicy);
			JsonResponseUtil.successBodyResponse(upToken, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
