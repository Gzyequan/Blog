package com.yequan.common.quartz.proxy;

import com.yequan.common.quartz.model.SchedulerJob;
import com.yequan.common.quartz.proxy.taskInterface.SyncJobInterface;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Auther: yq
 * @Date: 2019/4/25 10:21
 * @Description: 同步定时任务代理
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncJobProxy extends QuartzJobBean {

    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        SyncJobInterface syncJobInterface = (SyncJobInterface) jobDataMap.get(SchedulerJob.QUARTZ_TASK_KEY);
        syncJobInterface.execute();
    }

}
