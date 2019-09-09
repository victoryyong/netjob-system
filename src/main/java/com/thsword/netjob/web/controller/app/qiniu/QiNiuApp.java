package com.thsword.netjob.web.controller.app.qiniu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.thsword.netjob.dao.IAccessDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Access;
import com.thsword.netjob.service.AccessService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;

/**
 * 菜单
 * 
 * @Description:TODO
 * 
 * @author:yong
 * 
 * @time:2018年5月10日 下午8:52:49
 */
@RestController
@Api(tags = "NETJOB-QINIU", description = "七牛接口")
public class QiNiuApp {
	@Resource(name = "accessService")
	AccessService accessService;

	/**
	 * 
	 * 
	 * 获取上传token
	 * 
	 * @param request
	 * @param response
	 * @param member
	 * @throws Exception
	 * 
	 *             void
	 * 
	 * @exception:
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@ApiOperation(value = "获取上传token", httpMethod = "POST")
	@RequestMapping("app/visitor/getUpToken")
	public BaseResponse getUpToken() throws Exception {
		Access access = new Access();
		access.setAppId(Global.getSetting(Global.QINIU_UPLOAD_ACCESS_KEY));
		access = (Access) accessService.queryEntity(IAccessDao.class, access);
		if (null == access) {
			throw new ServiceException("系统key异常");
		}
		Auth auth = Auth.create(
				Global.getSetting(Global.QINIU_UPLOAD_ACCESS_KEY),
				access.getSecretKey());
		StringMap putPolicy = new StringMap();
		putPolicy
				.put("returnBody",
						"{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
		long expireSeconds = 3600;
		String upToken = auth.uploadToken(Global.QINIU_UPLOAD_BOCKET_NAME,
				null, expireSeconds, putPolicy);
		return BaseResponse.success(upToken);
	}
}
