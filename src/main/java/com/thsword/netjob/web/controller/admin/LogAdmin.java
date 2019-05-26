package com.thsword.netjob.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.ILogDao;
import com.thsword.netjob.pojo.Log;
import com.thsword.netjob.service.LogService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.utils.page.Page;
/**
 * 日志

 * @Description日志

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class LogAdmin {
	@Resource(name="logService")
	LogService logService;
	@AuthAnnotation(permissions="admin.log.edit")
	@RequestMapping("admin/log/list")
	public void getMapChild(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			List<Log> permissions = (List<Log>) logService.queryPageEntity(ILogDao.class, map);
			result.put("page", page);
			result.put("list", permissions);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
