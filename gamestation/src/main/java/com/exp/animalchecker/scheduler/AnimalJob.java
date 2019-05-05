package com.exp.animalchecker.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.exp.animalchecker.service.AnimalCheckerService;
import com.exp.common.util.ApplicationContextUtil;
import com.exp.common.util.SystemConf;

//@Component("taskJob")
@SuppressWarnings("unused")
public class AnimalJob {

	private static Log logger = LogFactory.getLog(AnimalJob.class);

//	@Scheduled(cron = "${schedule.cron.checkwebappusinghttp}")
	public void doJob() {
	}
}
