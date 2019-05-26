package com.thsword.netjob.service.impl;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.IJobTaskDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.JobTask;
import com.thsword.netjob.service.JobTaskService;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.netjob.web.quartz.DynamicJobFactory;
import com.thsword.utils.object.UUIDUtil;
@Service(value = "jobTaskService")
public class JobTaskServiceImpl extends BaseServiceImpl implements JobTaskService{
	private static final Log log = LogFactory.getLog(JobTaskServiceImpl.class);
	@Autowired
	DynamicJobFactory dynamicJobFactory;
	@Autowired
	IJobTaskDao jobTaskDao;
	
	@Override
	@Transactional
	public void addJobTask(JobTask task) throws ServiceException{
		try {
			JobTask temp = new JobTask();
			temp.setBean(task.getBean());
			temp = (JobTask) jobTaskDao.queryEntity(temp);
			if(null!=temp){
				throw new ServiceException("该定时任务已存在");
			}
			task.setId(UUIDUtil.get32UUID());
			jobTaskDao.addEntity(task);
			dynamicJobFactory.addJob(task.getBean(), task.getExpression());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException("添加定时任务异常");
		}
	}
	
	@Override
	@Transactional
	public void updateJobTask(JobTask task) throws ServiceException{
		try {
			JobTask taskBean = (JobTask) jobTaskDao.queryEntityById(task.getId());
			if(null==taskBean){
				throw new ServiceException("该定时任务不存在");
			}
			//更新原定时任务
			if(taskBean.getBean().equals(task.getBean())){
				dynamicJobFactory.updateJobTime(task.getBean(), task.getExpression());
			//删除原定时任务、添加新定时任务	
			}else{
				dynamicJobFactory.deleteJob(taskBean.getBean());
				dynamicJobFactory.addJob(task.getBean(), task.getExpression());
			}
			task.setStatus(null);
			jobTaskDao.updateEntity(task);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException("更新定时任务异常");
		}
	}
	
	@Override
	@Transactional
	public void pauseJobTask(JobTask task) throws ServiceException{
		try {
			JobTask taskBean = (JobTask) jobTaskDao.queryEntityById(task.getId());
			if(null==taskBean){
				throw new ServiceException("该定时任务不存在");
			}
			dynamicJobFactory.pauseJob(taskBean.getBean());
			JobTask temp = new JobTask();
			temp.setId(task.getId());
			temp.setStatus(Global.SYS_TASK_STATUS_0);
			jobTaskDao.updateEntity(temp);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException("暂停定时任务异常");
		}
	}
	
	@Override
	@Transactional
	public void startJobTask(JobTask task) throws ServiceException{
		try {
			JobTask taskBean = (JobTask) jobTaskDao.queryEntityById(task.getId());
			if(null==taskBean){
				throw new ServiceException("该定时任务不存在");
			}
			dynamicJobFactory.resumeJob(taskBean.getBean());
			JobTask temp = new JobTask();
			temp.setId(task.getId());
			temp.setStatus(Global.SYS_TASK_STATUS_1);
			jobTaskDao.updateEntity(temp);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException("启动定时任务异常");
		}
	}
	
	@Override
	@Transactional
	public void deleteJobTask(JobTask task) throws ServiceException{
		try {
			JobTask temp = (JobTask) jobTaskDao.queryEntityById(task.getId());
			if(null==temp){
				throw new ServiceException("定时任务不存在");
			}
			dynamicJobFactory.deleteJob(temp.getBean());
			jobTaskDao.deleteEntityById(task.getId());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException("删除定时任务异常");
		}
	}
	
}
