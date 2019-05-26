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
import com.thsword.netjob.dao.ISettingDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Setting;
import com.thsword.netjob.service.SettingService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.AuthAnnotation.ISADMIN;
import com.thsword.netjob.web.annotation.LogControllerAnnotation;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

/**
 * 系统设置
 * 
 * @Description:系统设置
 * 
 * @author:yong
 * 
 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class SettingAdmin {
	@Resource(name="settingService")
	SettingService settingService;
	/**
	 * @throws Exception 
	 * 系统设置
	* @Title:  
	* @Description: 系统设置列表
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@AuthAnnotation(permissions="admin.setting.list")
	@RequestMapping("admin/setting/list")
	@SuppressWarnings("unchecked")
	public void list(HttpServletRequest request,HttpServletResponse response,Page pageUtil,Setting setting) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) JSONObject.toJSON(setting);
			map.put("page", pageUtil);
			List<Setting> settings = (List<Setting>) settingService.queryPageEntity(ISettingDao.class,map);
			result.put("page", pageUtil);
			result.put("list", settings);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 新增系统设置
	* @Title: addRole 
	* @Description: 新增系统设置
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@AuthAnnotation(isAdmin=ISADMIN.YES)
	@RequestMapping("admin/setting/add")
	@LogControllerAnnotation(description="新增系统配置")
	public void add(HttpServletRequest request,HttpServletResponse response,Setting setting)throws Exception{
		try {
			String userId = (String) request.getAttribute("userId");
			if(StringUtils.isEmpty(setting.getKey())||
					StringUtils.isEmpty(setting.getValue())||
					StringUtils.isEmpty(setting.isOpen())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			Setting temp = (Setting) settingService.queryEntity(ISettingDao.class, setting);
			if(null!=temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"该key已存在",response,request);
				return;
			}
			setting.setCreateBy(userId);
			setting.setUpdateBy(userId);
			setting.setId(UUIDUtil.get32UUID());
			settingService.addEntity(ISettingDao.class, setting);
			List<Setting> settings = (List<Setting>) settingService.queryAllEntity(ISettingDao.class, null);
			Global.initSetting(settings);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 编辑系统设置
	* @Title: edit 
	* @Description: 编辑系统设置
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@AuthAnnotation(isAdmin=ISADMIN.YES)
	@RequestMapping("admin/setting/edit")
	@LogControllerAnnotation(description="编辑系统配置")
	public void edit(HttpServletRequest request,HttpServletResponse response,Setting setting)throws Exception{
		try {
			if(StringUtils.isEmpty(setting.getId())||
					StringUtils.isEmpty(setting.getKey())||
					StringUtils.isEmpty(setting.getValue())||
					StringUtils.isEmpty(setting.isOpen())){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			Setting oldSetting = (Setting) settingService.queryEntityById(ISettingDao.class,setting.getId());
			settingService.updateEntity(ISettingDao.class, setting);
			if(null!=oldSetting&&!oldSetting.getValue().equals(setting.getValue())){
				List<Setting> settings = (List<Setting>) settingService.queryAllEntity(ISettingDao.class, null);
				Global.initSetting(settings);
			}
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 删除系统设置
	* @Title: list
	* @Description: 删除系统设置
	* @param @param request
	* @param @param response
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@AuthAnnotation(isAdmin=ISADMIN.YES)
	@RequestMapping("admin/setting/delete")
	@LogControllerAnnotation(description="删除系统设置")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String id = request.getParameter("id");
			if(StringUtils.isEmpty(id)){
					JsonResponseUtil.codeResponse(ErrorUtil.REQUEST_INVALID_PARAM, response,request);
					return;
			}
			settingService.deleteEntityById(ISettingDao.class, id);
			List<Setting> settings = (List<Setting>) settingService.queryAllEntity(ISettingDao.class, null);
			Global.initSetting(settings);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
