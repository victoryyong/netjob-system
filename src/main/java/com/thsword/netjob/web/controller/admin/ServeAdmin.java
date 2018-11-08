package com.thsword.netjob.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IServeDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Serve;
import com.thsword.netjob.service.ServeService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.utils.page.Page;
/**
 * 审核管理

 * @Description审核

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class ServeAdmin {
	@Resource(name="serveService")
	ServeService serveService;
	/**
	 * 
	 * 需求列表

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@AuthAnnotation(permissions="admin.serve.list")
	@RequestMapping("admin/serve/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String type= request.getParameter("type");
			String status= request.getParameter("status");
			String title= request.getParameter("title");
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = request.getParameter("citycode");
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			//查询参数
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("t.c_createDate");
			map.put("page", page);
			map.put("type", type);
			map.put("status", status);
			map.put("title", title);
			map.put("citycode", citycode);
			@SuppressWarnings("unchecked")
			List<Serve> serves = (List<Serve>) serveService.queryPageEntity(IServeDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", serves);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@AuthAnnotation(permissions="admin.serve.delete")
	@RequestMapping("admin/serve/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			Integer level = (Integer) request.getAttribute("userLevel");
			String citycode = "";
			if(level==Global.SYS_USER_LEVEL_2){
				citycode = request.getAttribute("citycode")+"";
				if(StringUtils.isEmpty(citycode)){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "无代理权限",response,request);
					return;
				}
			}
			String id = request.getParameter("id");
			if(StringUtils.isEmpty(id)){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空",response,request);
				return;
			}
			serveService.deleteEntityById(IServeDao.class, id);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
