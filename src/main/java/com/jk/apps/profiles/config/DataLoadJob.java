package com.jk.apps.profiles.config;
import com.jk.apps.profiles.service.DataLoadService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.sql.DataSource;

@Configuration
public class DataLoadJob extends QuartzJobBean{
    @Autowired
    private DataLoadService dataLoadService;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String folderPath = context.getJobDetail().getJobDataMap().getString("folderPath");
        int dataSyncPurpose = context.getJobDetail().getJobDataMap().getInt("dataSyncPurpose");
        dataLoadService.loadData(folderPath, dataSyncPurpose);
    }
}
