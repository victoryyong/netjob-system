package com.thsword.netjob.web.quartz;

import javax.annotation.Resource;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

@Component
public class DynamicJobFactory {
 
    @Resource
    private Scheduler scheduler;
 
    /**
     * 添加job
     *
     * @param className
     * @param cronExpression
     * @throws Exception
     */
    public void addJob(String className, String cronExpression) throws Exception {
        Class clazz = Class.forName(className);
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity("jobName:" + className, Scheduler.DEFAULT_GROUP)
                //如果需要给任务传递参数，可以用setJobData来设置参数
                .setJobData(new JobDataMap())
                .build();
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setName("jobTrigger:" + className);
        cronTrigger.setCronExpression(cronExpression);
        JobKey jobKey = new JobKey("jobName:" + className, Scheduler.DEFAULT_GROUP);
        if (!scheduler.checkExists(jobKey)) {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        }else{
        	TriggerKey triggerKey = new TriggerKey("jobTrigger:" + className, Scheduler.DEFAULT_GROUP);
            CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            if (trigger == null) { 
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cronExpression)) {
                trigger.setCronExpression(cronExpression);
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
    }
    
    /**
     * 删除job
     *
     * @param className
     * @param cronExpression
     * @throws Exception
     */
    public void deleteJob(String className) throws Exception {
        Class clazz = Class.forName(className);
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity("jobName:" + className, Scheduler.DEFAULT_GROUP)
                //如果需要给任务传递参数，可以用setJobData来设置参数
                .setJobData(new JobDataMap())
                .build();
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setName("jobTrigger:" + className);
        JobKey jobKey = new JobKey("jobName:" + className, Scheduler.DEFAULT_GROUP);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
    }
 
    /**
     * 暂停job
     *
     * @param className
     * @throws Exception
     */
    public void pauseJob(String className) throws Exception {
        JobKey jobKey = new JobKey("jobName:" + className, Scheduler.DEFAULT_GROUP);
        scheduler.pauseJob(jobKey);
    }
 
    /**
     * 启动job
     *
     * @param className
     * @throws Exception
     */
    public void resumeJob(String className) throws Exception {
        JobKey jobKey = new JobKey("jobName:" + className, Scheduler.DEFAULT_GROUP);
        scheduler.resumeJob(jobKey);
    }
 
    /**
     * 停止job
     *
     * @param className
     * @throws Exception
     */
    public void stopJob(String className) throws Exception {
        JobKey jobKey = new JobKey("jobName:" + className, Scheduler.DEFAULT_GROUP);
        scheduler.deleteJob(jobKey);
    }
 
    /**
     * 修改job的执行时间
     *
     * @param className
     * @param cronExpression
     * @throws Exception
     */
    public void updateJobTime(String className, String cronExpression) throws Exception {
        TriggerKey triggerKey = new TriggerKey("jobTrigger:" + className, Scheduler.DEFAULT_GROUP);
        CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cronExpression)) {
            trigger.setCronExpression(cronExpression);
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
 
    /**
     * 获取job信息
     *
     * @param className
     * @return JobDetail
     * @throws Exception
     */
    public JobDetail getJobDetail(String className) throws Exception {
        JobKey jobKey = new JobKey("jobName:" + className, Scheduler.DEFAULT_GROUP);
        JobDetail detail = scheduler.getJobDetail(jobKey);
        return detail;
    }
 
    /**
     * 启动所有的任务
     *
     * @throws Exception
     */
    public void startJobs() throws Exception {
        scheduler.start();
    }
 
    /**
     * shutdown所有的任务
     *
     * @throws Exception
     */
    public void shutdownJobs() throws Exception {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
