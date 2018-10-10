package com.thsword.netjob.web.listener;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.thsword.netjob.dao.ISettingDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.Setting;
import com.thsword.netjob.service.SettingService;
@Component
public class NetJobListener implements ApplicationListener<ContextRefreshedEvent>{
	@Resource(name="settingService")
	SettingService settingService;
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		List<Setting> settings=null;
		try {
			settings = (List<Setting>) settingService.queryAllEntity(ISettingDao.class, null);
			Global.initSetting(settings);
			Global.initMaps();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
