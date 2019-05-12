package com.thsword.netjob.web.listener;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.thsword.netjob.dao.IJobTaskDao;
import com.thsword.netjob.dao.ISettingDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.JobTask;
import com.thsword.netjob.pojo.Setting;
import com.thsword.netjob.service.JobTaskService;
import com.thsword.netjob.service.SettingService;
import com.thsword.netjob.web.quartz.DynamicJobFactory;
@Component
public class NetJobListener implements ApplicationListener<ContextRefreshedEvent>{
	private static final Log log = LogFactory.getLog(NetJobListener.class);
	
	@Resource(name="settingService")
	SettingService settingService;
	@Resource(name="jobTaskService")
	JobTaskService jobTaskService;
	@Resource
    DynamicJobFactory dynamicJobFactory;
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		List<Setting> settings=null;
		List<JobTask> tasks = null;
		try {
			//启动配置
			settings = (List<Setting>) settingService.queryAllEntity(ISettingDao.class, null);
			Global.initSetting(settings);
			//启动定时任务
			JobTask temp = new JobTask();
			temp.setStatus(Global.SYS_TASK_STATUS_1);
			tasks = (List<JobTask>) jobTaskService.queryAllEntity(IJobTaskDao.class, temp);
	        for (JobTask task : tasks) {
	        	log.error("调用定时任务Start"+task.getBean());
	            dynamicJobFactory.addJob(task.getBean(), task.getExpression());
	            log.error("调用定时任务End");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
