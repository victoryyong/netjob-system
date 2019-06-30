package com.thsword.netjob.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IJobTaskDao;
import com.thsword.netjob.pojo.JobTask;
import com.thsword.netjob.service.JobTaskService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.annotation.AuthAnnotation;
import com.thsword.netjob.web.annotation.LogControllerAnnotation;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.page.Page;
@Controller
@Log4j2
public class JobTaskAdmin {
	@Autowired
	JobTaskService jobTaskService;
	@AuthAnnotation(permissions="admin.task.list")
	@RequestMapping("admin/task/list")
	public void list(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", page);
			List<JobTask> tasks = (List<JobTask>) jobTaskService.queryPageEntity(IJobTaskDao.class, map);
			result.put("page", page);
			result.put("list", tasks);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@AuthAnnotation(permissions="admin.task.add")
	@RequestMapping("admin/task/add")
	@LogControllerAnnotation(description="添加任务")
	public void add(HttpServletRequest request,HttpServletResponse response,JobTask task) throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			if(StringUtils.isEmpty(task.getBean())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "job bean 不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(task.getExpression())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "job expression 不能为空",response,request);
				return;
			}
			task.setCreateBy(userId);
			task.setUpdateBy(userId);
			jobTaskService.addJobTask(task);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"新增异常",response, request);
		}
	}
	@AuthAnnotation(permissions="admin.task.edit")
	@RequestMapping("admin/task/edit")
	@LogControllerAnnotation(description="编辑任务")
	public void edit(HttpServletRequest request,HttpServletResponse response,JobTask task) throws Exception{
		try {
			if(StringUtils.isEmpty(task.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "job bean 不能为空",response,request);
				return;
			}
			jobTaskService.updateJobTask(task);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"编辑异常",response, request);
		}
	}
	@AuthAnnotation(permissions="admin.task.delete")
	@RequestMapping("admin/task/delete")
	@LogControllerAnnotation(description="删除任务")
	public void delete(HttpServletRequest request,HttpServletResponse response,JobTask task) throws Exception{
		try {
			if(StringUtils.isEmpty(task.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "job id 不能为空",response,request);
				return;
			}
			jobTaskService.deleteJobTask(task);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"删除异常",response, request);
		}
	}
	@AuthAnnotation(permissions="admin.task.edit")
	@RequestMapping("admin/task/pause")
	@LogControllerAnnotation(description="暂停任务")
	public void pause(HttpServletRequest request,HttpServletResponse response,JobTask task) throws Exception{
		try {
			if(StringUtils.isEmpty(task.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "job id 不能为空",response,request);
				return;
			}
			jobTaskService.pauseJobTask(task);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"暂停异常",response, request);
		}
	}
	@AuthAnnotation(permissions="admin.task.edit")
	@RequestMapping("admin/task/start")
	@LogControllerAnnotation(description="启动任务")
	public void start(HttpServletRequest request,HttpServletResponse response,JobTask task) throws Exception{
		try {
			if(StringUtils.isEmpty(task.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "job id 不能为空",response,request);
				return;
			}
			jobTaskService.startJobTask(task);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (ServiceException e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,e.getMessage(),response, request);
		}catch (Exception e) {
			log.info(e.getMessage(),e);
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL,"启动异常",response, request);
		}
	}
}
