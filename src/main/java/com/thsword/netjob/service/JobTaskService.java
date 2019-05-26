package com.thsword.netjob.service;

import com.thsword.netjob.pojo.JobTask;
import com.thsword.netjob.web.exception.ServiceException;

public interface JobTaskService extends IBaseService{

	void addJobTask(JobTask task) throws ServiceException;
	
	void deleteJobTask(JobTask task) throws ServiceException;
	
	void updateJobTask(JobTask task) throws ServiceException;
	
	void pauseJobTask(JobTask task) throws ServiceException;
	
	void startJobTask(JobTask task) throws ServiceException;

}
